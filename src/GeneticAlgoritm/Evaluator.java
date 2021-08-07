package GeneticAlgoritm;

import Common.DataQuery;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Evaluator {
    public int PopulationSize = 100;
    public float MutationRate = 0.01f;
    public float MutationMagnitude = 0.01f;
    public float NewWeightAssignmentRate = 0.15f;

    public int NUMBER_OF_HIDDEN_LAYERS = 1;
    public int NUMBER_OF_NODES_IN_HIDDEN_LAYERS = 3;

    public Roulette<Player> PopulationSelectorBag = new Roulette<>();

    public List<DataQuery> GradingData = DataQuery.ReadDataset3File("data3.training.txt");
    public List<DataQuery> TestingData = DataQuery.ReadDataset3File("data3.testing.txt");



    public List<Player> CurrentGeneration;
    public List<Player> NextGeneration;

    public float BestScore = 0.0f;
    public float Average = 0.0f;

    public boolean quietMode = false;
    public boolean newHighscore = true;


    /**
     * Constructor
     */
    public Evaluator() {
        this.CurrentGeneration = new ArrayList<Player>();
        this.NextGeneration = new ArrayList<Player>();
    }

    boolean enable_dump = true;
    int sampling_rate = 50;
    int sampling_counter = 0;


    /**
     * This function will run the actual loop
     * @param Iterations Number of iterations to run
     */
    public void RunSimulation(int Iterations) throws Exception {
        var output = "Best, Worst, Average\n";
        sampling_counter = 0;
        InitializePopulation();
        for (int i = 0; i < Iterations; i++) {


            if (newHighscore && !quietMode) {
                System.out.println(String.format("New Best Score : Iteration %d | Best score : %f", i + 1, this.CurrentGeneration.get(0).Fitness));
                newHighscore = false;
            }
            GradeAndSortPopulation();


            if (enable_dump) {
                sampling_counter++;
                if (sampling_counter >= sampling_rate) {
                    sampling_counter = 0;
                    output += this.CurrentGeneration.get(0).Fitness + ", " + this.CurrentGeneration.get(this.CurrentGeneration.size() - 1).Fitness + ", " + this.Average + "\n";
                }
            }

            CreateNextGeneration();
            PrepareNextIteration();

        }

        if (enable_dump) {

            FileWriter f = new FileWriter("dataset_3.csv");
            f.write(output);
            f.close();
        }
    }

    /**
     * This function will initialize the population by added randomly generated
     * candidates into the simulation
     */
    public void InitializePopulation() {
        if (!quietMode) System.out.println("Creating baseline population");
        for (int i = 0; i < this.PopulationSize; i++) {
            this.CurrentGeneration.add(new Player(this.NUMBER_OF_HIDDEN_LAYERS, this.NUMBER_OF_NODES_IN_HIDDEN_LAYERS));
        }
    }

    /**
     * This function will grade the candidates in the simulation and sort the list.
     * Also it its finds a candidate with a fitness greater than the current best score,
     * it will set the new best score
     */
    public void GradeAndSortPopulation() {
        var total = 0.0f;
        for (var player : this.CurrentGeneration) {
            player.RunExamination(GradingData);
            this.PopulationSelectorBag.AddEntry(player, player.Fitness);
            total += player.Fitness;
        }
        this.Average = (float)total / (float)CurrentGeneration.size();
        Collections.sort(this.CurrentGeneration);
        Collections.reverse(this.CurrentGeneration);
        if (this.BestScore < this.CurrentGeneration.get(0).Fitness) {
            this.BestScore = this.CurrentGeneration.get(0).Fitness;
            newHighscore = true;
        }
    }

    /**
     * This will create the next generation by mating randomly selected candidates from the
     * current generation. Then the children will be mutated. Pretty standard procedure
     * @throws Exception
     */
    public void CreateNextGeneration() throws Exception {
        NextGeneration.add(CurrentGeneration.get(0));
        while (NextGeneration.size() < this.PopulationSize) {
            var parentA = PopulationSelectorBag.GetRandom();
            var parentB = PopulationSelectorBag.GetRandom();
            var childa = parentA.MateWith(parentB).get(0);
            var childb = parentA.MateWith(parentB).get(1);

            // MUTATE THE BABIES
            if (Shared.GlobalRandom.nextFloat() <= MutationRate) childa.Mutate(this.NewWeightAssignmentRate, this.MutationMagnitude);
            if (Shared.GlobalRandom.nextFloat() <= MutationRate) childb.Mutate(this.NewWeightAssignmentRate, this.MutationMagnitude);

            this.NextGeneration.add(childa);
            this.NextGeneration.add(childb);
        }
    }

    /**
     * Reset the variable used in the current iteration and prepare them for the next
     * iteration
     */
    public void PrepareNextIteration() {
        this.CurrentGeneration.clear();
        this.CurrentGeneration.addAll(this.NextGeneration);
        this.NextGeneration.clear();
        this.PopulationSelectorBag.Clear();
    }

}

package GeneticAlgorithmLogic;

import Common.DataQuery;
import GeneticAlgoritm.Shared;
import GeneticAlgoritm.Roulette;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class Simulation {
    public List<Candidate> CurrentGeneration;
    public List<Candidate> NextGeneration;

    public final float MutationRate = 0.01f;

    public Candidate BestCandidate;
    public float BestScore;
    public float AverageScore;
    public int PopulationSize;
    public int InputSize;

    private File OutputFile;
    private String OutputBuffer;
    private int SamplingCounter;
    private int SamplingRate;

    private Roulette<Candidate> CandidateBag;

    public Simulation(int PopulationSize, int InputSize, File OutputFile, int SamplingRate) {
        this.CandidateBag = new Roulette<>();
        this.PopulationSize = PopulationSize;
        this.InputSize = InputSize;
        this.SamplingRate = SamplingRate;
        this.OutputFile = OutputFile;

        this.CurrentGeneration = new ArrayList<>();
        this.NextGeneration = new ArrayList<>();

        InitializePopulation();
    }

    public void InitializePopulation() {
        for (int i = 0; i < PopulationSize; i++) {
            CurrentGeneration.add(new Candidate(InputSize));
        }
    }

    public void Run(int iterations, List<DataQuery> training_data) throws Exception {
        this.OutputBuffer = "Generation, BestScore, AverageScore, WorstScore\n";

        for (int i = 0; i < iterations; i++) {
            // Grade the current generation and add it to the bag
            // also be sure to clear the bag before we get started
            CandidateBag.Clear();
            var total = 0.0f;
            for (var c : this.CurrentGeneration) {
                var score = c.EvaluateMultiple(training_data);
                total += score;
                CandidateBag.AddEntry(c, score);
                if (score > BestScore) {
                    BestCandidate = c;
                    BestScore = score;

                    System.out.println("Iteration: "+i+" | Found new best score: " + BestScore + " : " + BestCandidate.GetGeneString());
                }
            }
            this.AverageScore = total / PopulationSize;
//            this.NextGeneration.add(this.CurrentGeneration.get(0));

            if (SamplingCounter >= SamplingRate) {
                this.OutputBuffer += i + ", " + BestScore + ", " + AverageScore + ", " + CurrentGeneration.get(CurrentGeneration.size() - 1).Accuracy + "\n";
                SamplingCounter = 0;
            }
            SamplingCounter++;

            // Prepare the next generation
            NextGeneration.clear();
            while (NextGeneration.size() != PopulationSize) {
                var ca = this.CandidateBag.GetRandom();
                var cb = this.CandidateBag.GetRandom();
                var child = ca.MateWith(cb);
                if (Shared.GetRandomWeight() < MutationRate) {
                    child.Mutate();
                }
                NextGeneration.add(child);
            }

            // Transfer the next generation to
            // the current generation
            this.CurrentGeneration.clear();
            this.CurrentGeneration.addAll(NextGeneration);

        }

        FileWriter f = new FileWriter(OutputFile);
        f.write(OutputBuffer);
        f.close();
    }


}

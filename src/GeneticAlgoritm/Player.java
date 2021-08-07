package GeneticAlgoritm;

import Common.DataQuery;

import java.util.ArrayList;
import java.util.List;

public class Player implements Comparable<Player> {
    public float Fitness = 0.0f;
    public NeuralNetwork Brain;

    /**
     * Constructor
     */
    public Player(int NumberOfHiddenLayers, int NumberOfNodePerHiddenLayer) {
        this.Brain = new NeuralNetwork(NumberOfHiddenLayers, NumberOfNodePerHiddenLayer, 2, 6);
    }

    /**
     * This function will run an examination on the brain component.
     * @param Queries list of examination tests
     * @return Number of correct answers divided by the total questions
     */
    public float RunExamination(List<DataQuery> Queries) {
        int CorrectScore = 0;
        for (var Q : Queries) {
            var result = Brain.Evaluate(Q.Inputs);
            var actualAnswer = (result.get(0) > result.get(1)) ? 0 : 1;
            if (actualAnswer == Q.ExpectedOutput) {
                CorrectScore += 1;
            }
        }
        this.Fitness = ((float)CorrectScore / Queries.size());
        return this.Fitness;
    }

    /**
     * Comparible component. Useful for sorting. Hehe
     */
    @Override
    public int compareTo(Player o) {
        if (this.Fitness > o.Fitness) return 1;
        if (this.Fitness < o.Fitness) return -1;
        return 0;
    }

    /**
     * This function will accept another player object and create 2 new candidates by
     * mating with the other player object
     * @param partner Player to mate with
     * @return List of child players
     */
    public List<Player> MateWith(Player partner) throws Exception {
        var aset = partner.Brain.GetBrainConfiguration();
        var bset = partner.Brain.GetBrainConfiguration();

        if (aset.size() != bset.size()) {
            throw new Exception("Gene length mismatch!");
        }

        var intersection_point = Shared.GetRandomInt(1, Shared.GetRandomInt(1, aset.size() - 2));

        var caset = new ArrayList<Float>();
        var cbset = new ArrayList<Float>();
        caset.addAll(aset.subList(0, intersection_point + 1));
        cbset.addAll(bset.subList(0, intersection_point + 1));
        caset.addAll(bset.subList(intersection_point, bset.size() - 1));
        cbset.addAll(aset.subList(intersection_point, bset.size() - 1));

        if (caset.size() != aset.size()) throw new Exception("Child size mismatch");
        if (cbset.size() != aset.size()) throw new Exception("Child size mismatch");
        var childa = new Player(this.Brain.NUMBER_OF_HIDDEN_LAYERS, this.Brain.NUMBER_OF_NODES_IN_HIDDEN_LAYERS);
        var childb = new Player(this.Brain.NUMBER_OF_HIDDEN_LAYERS, this.Brain.NUMBER_OF_NODES_IN_HIDDEN_LAYERS);
        childa.Brain.SetBrainConfiguration(caset);
        childb.Brain.SetBrainConfiguration(cbset);
        return new ArrayList<Player>() {{ add(childa); add(childb); }};
    }

    /**
     * This function will mutate the player.
     * @param newWeightAssignmentRate This variable will define the probability a gene will be reassigned a new value instead of shifting
     * @param mutationMagnitude This variable will define the mutation magnitude
     */
    public void Mutate(float newWeightAssignmentRate, float mutationMagnitude) {
        var geneSequence = this.Brain.GetBrainConfiguration();
        for (int i = 0; i < 1; i++) {
            var MutationIndex = Shared.GetRandomInt(0, Shared.GetRandomInt(0, geneSequence.size() - 1));

            if (Shared.GlobalRandom.nextDouble() < newWeightAssignmentRate) {
                geneSequence.set(MutationIndex, Shared.GetRandomWeight());
            } else {
                var normalizedMutationMagnitude = mutationMagnitude * Shared.GlobalRandom.nextFloat();
                if (Shared.GetRandomInt(0, 2) == 1) {
                    geneSequence.set(MutationIndex, geneSequence.get(MutationIndex) + normalizedMutationMagnitude);
                } else {
                    geneSequence.set(MutationIndex, geneSequence.get(MutationIndex) - normalizedMutationMagnitude);
                }
            }
        }
        this.Brain.SetBrainConfiguration(geneSequence);
    }
}

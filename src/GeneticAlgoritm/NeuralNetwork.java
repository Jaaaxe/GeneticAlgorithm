package GeneticAlgoritm;

import java.util.ArrayList;
import java.util.List;

public class NeuralNetwork {
    public int NUMBER_OF_HIDDEN_LAYERS = 1;
    public int NUMBER_OF_NODES_IN_HIDDEN_LAYERS = 10;
    public int NUMBER_OF_OUTPUTS = 2;
    public int NUMBER_OF_INPUTS = 6;

    public List<List<Neuron>> Layers;

    /**
     * This constructor will create the neurons in the neural network based on the variables defined in the class
     */
    public NeuralNetwork() {
        Init();
    }

    public NeuralNetwork(int NumberOfHiddenLayers, int NumberOfNodesInHiddenLayer, int NumberOfOutputs, int NumberOfInputs) {
        this.NUMBER_OF_HIDDEN_LAYERS = NumberOfHiddenLayers;
        this.NUMBER_OF_NODES_IN_HIDDEN_LAYERS = NumberOfNodesInHiddenLayer;
        this.NUMBER_OF_OUTPUTS = NumberOfOutputs;
        this.NUMBER_OF_INPUTS = NumberOfInputs;
        Init();
    }

    private void Init() {
        Layers = new ArrayList<List<Neuron>>();
        Layers.add(GenerateNewLayer(NUMBER_OF_NODES_IN_HIDDEN_LAYERS, NUMBER_OF_INPUTS));
        for (int i = 1; i < NUMBER_OF_HIDDEN_LAYERS; i++) Layers.add(GenerateNewLayer(NUMBER_OF_NODES_IN_HIDDEN_LAYERS, NUMBER_OF_NODES_IN_HIDDEN_LAYERS));
        Layers.add(GenerateNewLayer(NUMBER_OF_OUTPUTS, NUMBER_OF_NODES_IN_HIDDEN_LAYERS));
    }

    /**
     * This function will accept some inputs and run them through the neural network and return
     * the result
     * @param Input
     * @return
     */
    public List<Float> Evaluate(List<Float> Input) {
        for (var neuron : Layers.get(0)) {
            neuron.CalculateOutput(Input);
        }
        for (int i = 1; i < this.Layers.size(); i++) {
            for (var neuron : Layers.get(i)) {
                neuron.CalculateOutput(RetrieveResultsFromLayer(Layers.get(i - 1)));
            }
        }
        return RetrieveResultsFromLayer(Layers.get(Layers.size() - 1));
    }

    /**
     * This function get the output from a layer. Used extensively by the evaluation method
     * @param l Layer to process
     * @return List of stored values.
     */
    public List<Float> RetrieveResultsFromLayer(List<Neuron> l) {
        var returnList = new ArrayList<Float>();
        for (var n : l) {
            returnList.add(n.Result);
        }
        return returnList;
    }

    /**
     * This function will generate a new layer with the provided number of nodes and input weights
     * @param NumberOfNodes Number of nodes in the layer itself
     * @param PreviousLayerNodeCount Number of nodes in the previous layer. Used to create a weight array
     * @return
     */
    public List<Neuron> GenerateNewLayer(int NumberOfNodes, int PreviousLayerNodeCount) {
        var newLayer = new ArrayList<Neuron>();
        for (int i = 0; i < NumberOfNodes; i++) {
            newLayer.add(new Neuron(PreviousLayerNodeCount));
        }
        return newLayer;
    }

    /**
     * This function will get the brain configuration by reading all the weight and biases and putting it
     * into a list and return it. This will be used for the mutation and mating processes.
     * @return Brain configuration
     */
    public List<Float> GetBrainConfiguration() {
        List<Float> BrainConfiguration = new ArrayList<>();
        for (var layer : this.Layers) {
            for (var neuron : layer) {
                BrainConfiguration.addAll(neuron.Weights);
                BrainConfiguration.add(neuron.Bias);
            }
        }
        return BrainConfiguration;
    }

    /**
     * This will accept a brain configuration and apply it to the neural network. Used for the mutation and
     * mating processes.
     * @param newWeights Configuration to apply to the neural network
     */
    public void SetBrainConfiguration(List<Float> newWeights) {
        var icounter = 0;
        for (var layer : this.Layers) {
            for (var neuron : layer) {
                var ogsize = neuron.Weights.size();
                neuron.Weights.clear();
                neuron.Weights.addAll(newWeights.subList(icounter, icounter + ogsize));
                icounter += ogsize;
                neuron.Bias = newWeights.get(icounter);
                icounter += 1;
            }
        }
    }

    /**
     * Neuron class. Represents a single neuron
     */
    public class Neuron {
        public List<Float> Weights = new ArrayList<>();
        public float Bias = 1f;
        public float Result = 0.0f;

        /**
         * Constructor
         * @param NumberOfNodesInPreviousLayer Number of nodes in the previous layer. Needed to create input weights
         */
        public Neuron(int NumberOfNodesInPreviousLayer) {
            for (int i = 0; i < NumberOfNodesInPreviousLayer + 1; i++) {
                var rw = Shared.GetRandomWeight();
                Weights.add(rw);
            }
        }

        /**
         * This function will handle the computation for the neuron. Will accept a list of float values and processes them
         * @param IncomingData Data to process
         * @return Computation result
         */
        public float CalculateOutput(List<Float> IncomingData) {
            Result = 0.0f;
            for (int i = 0; i < IncomingData.size(); i++) {
                Result += IncomingData.get(i) * this.Weights.get(i);
            }
            Result += Bias;
            Result = RectifiedLinearActivation(Result);
            return Result;
        }

        /**
         * This is the activation function
         * @param Input Input
         * @return Output
         */
        public float RectifiedLinearActivation(float Input) {
//          Method: Leaky ReLU
//          return (Input*0.1 > Input) ? (float) (Input * 0.1) : Input;

//          Method: Tahn
//          return (float) Math.tanh(Input);

//          Method: ReLU
            return (Input > 0) ? Input : 0;
        }
    }
}

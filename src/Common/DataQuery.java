package Common;

import GeneticAlgoritm.Shared;
import com.sun.source.tree.NewArrayTree;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * This class will store a single line from the dataset file. It will store the inputs and the expected outputs.
 */
public class DataQuery{
    public List<Float> Inputs;
    public float[] InputsArray;
    public int ExpectedOutput;

    /**
     * Constructor
     * @param inputs Inputs of the query
     * @param expectedOutput Expected output of the query
     */
    public DataQuery(List<Float> inputs, int expectedOutput) {
        this.Inputs = inputs;
        this.ExpectedOutput = expectedOutput;
    }

    /**
     * This function will get the inputs, which are usually in floats
     * as integers.
     * @return Integer array
     */
    public int[] GetInputsAsIntegers() {
        var result = new int[this.InputsArray.length];
        for (int i = 0; i < this.InputsArray.length; i++) {
            result[i] = (int)InputsArray[i];
        }
        return result;
    }

    /**
     * This function will get the inputs, which are usually in floats
     * as boolean.
     * @return boolean array
     */
    public boolean[] GetInputsAsBoolean() {
        var result = new boolean[this.InputsArray.length];
        for (int i = 0; i < this.InputsArray.length; i++) {
            result[i] = this.InputsArray[i] == 1.0f;
        }
        return result;
    }

    public boolean GetOutputAsBoolean() {
        return this.ExpectedOutput == 1;
    }

    /**
     * Blank constructor
     */
    public DataQuery() {
        this.Inputs = new ArrayList<Float>();
        this.ExpectedOutput = 0;
    }

    /**
     * Read a file with the same structure as dataset 3 file
     * @param FileName Filename
     * @return List of queries
     */
    public static List<DataQuery> ReadDataset3File(String FileName) {
        var Queries = Shared.ReadFileContents(FileName);
        var ParseQueries = new ArrayList<DataQuery>();

        for (var Query : Queries) {
            var qsplit = Query.split(Pattern.quote(" "));
            var newQueryObj = new DataQuery();
            for (int i = 0; i < 5; i++) {
                newQueryObj.Inputs.add(Float.parseFloat(qsplit[i]));
            }
            newQueryObj.InputsArray = new float[newQueryObj.Inputs.size()];
            for (int i = 0; i < newQueryObj.InputsArray.length; i++) {
                newQueryObj.InputsArray[i] = newQueryObj.Inputs.get(i);
            }
            newQueryObj.ExpectedOutput = Integer.parseInt(qsplit[6]);
            ParseQueries.add(newQueryObj);
        }


        return ParseQueries;
    }

    /**
     * Read a file with the same structure as dataset 2 file
     * @param FileName Filename
     * @return List of queries
     */
    public static List<DataQuery> ReadDataset2File(String FileName) {
        var Queries = Shared.ReadFileContents(FileName);
        var ParsedQueries = new ArrayList<DataQuery>();

        for (var Query : Queries) {
            var NewQuery = new DataQuery();
            var split = Query.split(Pattern.quote(" "));
            for (int i = 0; i < 6; i++) {
                NewQuery.Inputs.add(Float.parseFloat(
                    String.valueOf(split[0].toCharArray()[i])
                ));
            }
            NewQuery.InputsArray = new float[NewQuery.Inputs.size()];
            for (int i = 0; i < NewQuery.Inputs.size(); i++) {
                NewQuery.InputsArray[i] = NewQuery.Inputs.get(i);
            }
            NewQuery.ExpectedOutput = Integer.parseInt(split[1]);
            ParsedQueries.add(NewQuery);
        }
        return ParsedQueries;
    }

    /**
     * Read a file with the same structure as dataset 1 file
     * @param FileName Filename
     * @return List of queries
     */
    public static List<DataQuery> ReadDataset1File(String FileName) {
        var Queries = Shared.ReadFileContents(FileName);
        var ParsedQueries = new ArrayList<DataQuery>();

        for (var Query : Queries) {
            var NewQuery = new DataQuery();
            var split = Query.split(Pattern.quote(" "));
            for (int i = 0; i < 5; i++) {
                NewQuery.Inputs.add(Float.parseFloat(
                        String.valueOf(split[0].toCharArray()[i])
                ));
            }
            NewQuery.InputsArray = new float[NewQuery.Inputs.size()];
            for (int i = 0; i < NewQuery.Inputs.size(); i++) {
                NewQuery.InputsArray[i] = NewQuery.Inputs.get(i);
            }
            NewQuery.ExpectedOutput = Integer.parseInt(split[1]);
            ParsedQueries.add(NewQuery);
        }
        return ParsedQueries;
    }




}

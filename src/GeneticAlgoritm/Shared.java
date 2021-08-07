package GeneticAlgoritm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Shared {
    public static Random GlobalRandom = new Random();

    public static int GetRandomInt(int min, int max) {
        return min + (int)(GlobalRandom.nextFloat() * (max - min));
    }

    public static float GetRandomWeight() {
        return -1 + GlobalRandom.nextFloat()  * (1 - (-1));
    }

    public static List<String> ReadFileContents(String filename) {
        try {
            List<String> returnObj = new ArrayList<String>();
            File fileObj = new File(filename);
            Scanner reader = new Scanner(fileObj);
            while (reader.hasNextLine()) {
                returnObj.add(reader.nextLine());
            }
            return returnObj;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

}

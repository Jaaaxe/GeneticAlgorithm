import Common.DataQuery;
import GeneticAlgoritm.Evaluator;

import java.io.File;
import java.util.Scanner;

public class MainProgram {
    public static void main(String[] args) throws Exception {

        Scanner scan = new Scanner(System.in);

        System.out.println("CLASSIFICATION WITH THE USE OF GENETIC ALGORITHMS");
        System.out.println("PLEASE CHOOSE AN OPTION BY TYPING THE NUMBER");
        System.out.println("1. RUN DATASET ONE");
        System.out.println("2. RUN DATASET TWO");
        System.out.println("3. RUN DATASET THREE");
        System.out.println("4. RUN HIDDEN LAYER & NODE CHECK FOR SET 3");
        System.out.println("5. EXIT");

        Boolean sim = true;

        while(sim){
            int ans = scan.nextInt();
            switch(ans){
                case 1: RunDataset1();
                    break;
                case 2: RunDataset2();
                    break;
                case 3: RunDataset3();
                    break;
                case 4: HiddenLayerAndNodeCheck();
                    break;
                case 5: sim = false;
                    break;
            }

        }
        System.out.println("APPLICATION TERMINATED");
        scan.close();

    }

    public static void RunDataset1() throws Exception {
        var ds1_sim = new GeneticAlgorithmLogic.Simulation(16, 5, new File("dataset_1.csv"), 5);
        ds1_sim.Run(3000, DataQuery.ReadDataset1File("data1.txt"));
        System.out.println("Best Candidate For Dataset 1 : " + ds1_sim.BestCandidate.GetGeneString());
        }

    public static void RunDataset2() throws Exception {

        var ds2_sim = new GeneticAlgorithmLogic.Simulation(32, 6, new File("dataset_2.csv"), 100);
        ds2_sim.Run(30000, DataQuery.ReadDataset2File("data2.txt"));
        System.out.println("Best Candidate For Dataset 2 : " + ds2_sim.BestCandidate.GetGeneString());
    }

    public static void RunDataset3() throws Exception {
        var eval = new Evaluator();
        eval.RunSimulation(5000);
        System.out.println("Simulation complete");
        var best = eval.CurrentGeneration.get(0);
        var best_Score = best.RunExamination(eval.TestingData);
        System.out.println("Best candidate testing score : " + best_Score);
    }

    public static void HiddenLayerAndNodeCheck() throws Exception {
        var averageCount = 3;

        for (int hlc = 1; hlc <= 10; hlc++) {
            for (int npl = 1; npl <= 10; npl++) {
                System.out.print(hlc + " Hidden Layers + " + npl + " Nodes Per Layer = ");
                var best_score = 0.0;

                for (int a = 0; a < averageCount; a++) {
                    var eval = new Evaluator();
                    eval.quietMode = true;
                    eval.NUMBER_OF_NODES_IN_HIDDEN_LAYERS = npl;
                    eval.NUMBER_OF_HIDDEN_LAYERS = hlc;
                    eval.RunSimulation(1000);

                    best_score += eval.CurrentGeneration.get(0).RunExamination(eval.TestingData);

                }
                best_score = best_score / averageCount;
                System.out.println(best_score);

            }
        }
    }

}

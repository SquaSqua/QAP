import java.io.*;
import java.util.Arrays;

public class Runner {
    public static void main(String[] args) {
        String instanceFile = "src//instances//KC10-2fl-1rl.dat";
        NSGAII nsga2 = new NSGAII(instanceFile, 1, 10,1, 0.5);
        nsga2.initialize();
        Individual individual1 = nsga2.population.get(0);
        Individual individual2 = nsga2.population.get(1);
        System.out.println(Arrays.toString(individual1.getPermutation()));
        System.out.println(Arrays.toString(individual2.getPermutation()));
        Individual[] res = nsga2.crossing_OX(individual1, individual2);
        System.out.println(Arrays.toString(res[0].getPermutation()) + " + " + Arrays.toString(res[1].getPermutation()));

//        String results = "data.csv";
//        try
//        {
//            PrintWriter out = new PrintWriter(results);
//            out.println(nsga2.frontGenerator());
//            out.close();
//        }
//        catch (FileNotFoundException ex)
//        {
//            System.out.println("Nie da się utworzyć pliku!");
//        }
    }
}

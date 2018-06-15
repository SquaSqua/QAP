import java.io.*;

public class Runner {
    public static void main(String[] args) {
//        String instanceFile = "instances/Gar30-2fl-2uni.dat";
//        String instanceFile = "instances/Gar60-2fl-4uni.dat";
//        String instanceFile = "instances/Gar60-2fl-5uni.dat";
//        String instanceFile = "instances/Gar100-2fl-1rl.dat";
        String instanceFile = "instances/Gar100-2fl-3uni.dat";
        NSGAII nsga2 = new NSGAII(instanceFile, 100, 100,0.5, 0.5, 5);
        String results = "data.csv";
        try
        {
            PrintWriter out = new PrintWriter(results);
            out.println("ED, PFS, HV");
            out.println(nsga2.NSGAII());
            out.close();
        }
        catch (FileNotFoundException ex)
        {
            System.out.println("Nie da się utworzyć pliku!");
        }
    }
}

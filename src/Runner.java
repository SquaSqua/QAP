import java.io.*;

public class Runner {
    public static void main(String[] args) {
        String instanceFile = "src//instances//KC10-2fl-1rl.dat";
        NSGAII nsga2 = new NSGAII(instanceFile, 1, 10,0.5, 0.5);
        nsga2.initialize();

        String results = "data.csv";
        try
        {
            PrintWriter out = new PrintWriter(results);
            out.println(nsga2.frontGenerator());
            out.close();
        }
        catch (FileNotFoundException ex)
        {
            System.out.println("Nie da się utworzyć pliku!");
        }
    }
}

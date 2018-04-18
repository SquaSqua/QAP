import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class QAPIO
{
    private int size;
    private int DunnoYet;
    private Individual individual;

    public void readDefinition(String instanceFile) throws FileNotFoundException
    {
        BufferedReader in = new BufferedReader(new FileReader(instanceFile));
        try {
            String[] header = in.readLine().split(" ");
            size = Integer.parseInt(header[0]);
            DunnoYet = Integer.parseInt(header[1]);
            double[][] distanceMatrix = readArray(in, size);
            double[][] flowMatrix = readArray(in, size);
            double[][] whatIsIt = readArray(in, size);
            individual = new Individual(distanceMatrix, flowMatrix, whatIsIt);
        } catch (IOException e) {
            System.out.println("Nie znaleziono pliku!");
        }
    }

    public double[][] readArray(BufferedReader in, int size) throws IOException
    {
        String nextLine = null;
        double[][] temp = new double[size][size];

        in.readLine();
        for(int i = 0; i < size; i++)
        {
            nextLine = in.readLine();
            String[] line = nextLine.split(" ");
            for(int j = 0; j < size; j++)
            {
                temp[i][j] = Double.parseDouble(line[j]);
            }
        }
        return temp;
    }

    public Individual getIndividual() {
        return individual;
    }
    public void setIndividual(Individual individual) {
        this.individual = individual;
    }
    public int getDunnoYet() {
        return DunnoYet;
    }
    public void setDunnoYet(int dunnoYet) {
        DunnoYet = dunnoYet;
    }
    public int getSize() {
        return size;
    }
    public void setSize(int size) {
        this.size = size;
    }
}

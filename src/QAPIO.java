import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class QAPIO
{
    private int size;
    private int flowsNumber;
    private Population population;
    private int[][][] matricesArray;

    public void readDefinition(String instanceFile) throws FileNotFoundException
    {
        BufferedReader in = new BufferedReader(new FileReader(instanceFile));
        try {
            String[] header = in.readLine().split(" +");
            size = Integer.parseInt(header[0]);
            flowsNumber = Integer.parseInt(header[1]);
            matricesArray = new int[flowsNumber][size][size];

            int[][] distanceMatrix = readArray(in, size);
            for(int i = 0; i < flowsNumber; i++)
            {
                matricesArray[i] = readArray(in, size);
            }

            population = new Population(distanceMatrix, matricesArray);
        } catch (IOException e) {
            System.out.println("Nie znaleziono pliku!");
        }
    }

    public int[][] readArray(BufferedReader in, int size) throws IOException
    {
        String nextLine;
        int[][] temp = new int[size][size];

        in.readLine();
        for(int i = 0; i < size; i++)
        {
            nextLine = in.readLine();
            String[] line = nextLine.split(" +");
            for(int j = 0; j < size; j++)
            {
                temp[i][j] = Integer.parseInt(line[j]);
            }
        }
        return temp;
    }

    public Population getPopulation() {
        return population;
    }
    public void setPopulation(Population population) {
        this.population = population;
    }
    public int getFlowsNumber() {
        return flowsNumber;
    }
    public void setFlowsNumber(int flowsNumber) {
        flowsNumber = flowsNumber;
    }
    public int getSize() {
        return size;
    }
    public void setSize(int size) {
        this.size = size;
    }
}

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class QAPIO
{
    private int individual_size;
    private int flowsNumber;
    private int max_distance;
    private int max_flows;
    private int[][][] matricesArray;
    private int[][] distanceMatrix;

    public void readDefinition(String instanceFile) throws FileNotFoundException
    {
        BufferedReader in = new BufferedReader(new FileReader(instanceFile));
        try {
            String[] header = in.readLine().split(" +");
            individual_size = Integer.parseInt(header[2]);
            flowsNumber = Integer.parseInt(header[5]);
            max_distance = Integer.parseInt(header[8]);
            max_flows =  Integer.parseInt(header[12]);
            matricesArray = new int[flowsNumber][individual_size][individual_size];

            distanceMatrix = readArray(in, individual_size);
            for(int i = 0; i < flowsNumber; i++)
            {
                matricesArray[i] = readArray(in, individual_size);
            }

        } catch (IOException e) {
            System.out.println("Nie znaleziono pliku!");
        }
    }

    private int[][] readArray(BufferedReader in, int size) throws IOException
    {
        String nextLine;
        int[][] temp = new int[size][size];

        in.readLine();
        for(int i = 0; i < size; i++)
        {
            nextLine = in.readLine();
            String[] line = nextLine.replaceFirst("^" + " ", "").split(" +");

            for(int j = 0; j < size; j++)
            {
                temp[i][j] = Integer.parseInt(line[j]);
            }
        }
        return temp;
    }

    public int getFlowsNumber() {
        return flowsNumber;
    }
    public int getIndividual_size() {
        return individual_size;
    }
    public int[][] getDistanceMatrix() { return distanceMatrix; }
    public int[][][] getMatricesArray() { return matricesArray; }
    public int getMax_distance() {
        return max_distance;
    }
    public int getMax_flows() {
        return max_flows;
    }
}

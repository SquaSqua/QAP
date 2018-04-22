public class Population
{
    int[][] distanceMatrix;
    int[][][] matricesArray;
    Individual[] population;

    public Population(int[][] distanceMatrix, int[][][] matricesArray)
    {
        this.distanceMatrix = distanceMatrix;
        this.matricesArray = matricesArray;
    }
    public int fitness(int[] permutation, int[][] flowArray)
    {
        int fitness = 0;
        for(int i = 0; i < distanceMatrix.length - 1; i++)
        {
            for(int j = 0; j < distanceMatrix.length - 1; j++)
            {
                fitness += distanceMatrix[i][j] * (flowArray[permutation[i]][permutation[j]] + flowArray[permutation[j]][permutation[i]]);
            }
        }
        return fitness;
    }
}

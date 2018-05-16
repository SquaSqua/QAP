import java.util.Arrays;

public class Individual implements Comparable<Individual> {
    private int[] permutation;
    int[] fitnessArray;
    private double crowdingDistance;

    public Individual(int[] permutation, int flowsNumber) {
        this.permutation = permutation;
        this.fitnessArray = new int[flowsNumber];
    }

    public int fitness(int[][] distanceMatrix, int[][] flowArray) {
        int fitness = 0;
        for (int i = 0; i < distanceMatrix.length - 1; i++) {
            for (int j = 0; j < distanceMatrix.length - 1; j++) {
                fitness += distanceMatrix[i][j] * (flowArray[permutation[i]][permutation[j]] + flowArray[permutation[j]][permutation[i]]);
            }
        }
        return fitness;
    }

    public String toString() {
        System.out.println("Kolejne fitnessy:");
        for (int i : fitnessArray) {
            System.out.println(i);
        }
        return "Permutation: " + Arrays.toString(permutation);
    }

    public int[] getPermutation() {
        return permutation;
    }

    public void setPermutation(int[] permutation) {
        this.permutation = permutation;
    }

    public void setCrowdingDistance(double crowdingDistance) {
        this.crowdingDistance = crowdingDistance;
    }

    public double getCrowdingDistance() {
        return crowdingDistance;
    }

    @Override
    public int compareTo(Individual o) {
        int flag = 0, i, result;

        for (i = 0; flag == 0 && i < fitnessArray.length; i++) {
            flag = (int) Math.signum(fitnessArray[i] - o.fitnessArray[i]);
        }
        result = flag;
        for (int j = i; result != 0 && j < fitnessArray.length; j++) {
            int sgn = (int) Math.signum(fitnessArray[j] - o.fitnessArray[j]);
            if (sgn != flag && sgn != 0)
                result = 0;
        }
        return result;
    }
}

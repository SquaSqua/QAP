public class Individual
{
    private int[] permutation;

    public Individual(int[] permutation)
    {
        this.permutation = new int[permutation.length];

        for(int i = 0; i < permutation.length; i++){
            this.permutation[i] = permutation[i];
        }
    }

    public int[] getPermutation() {
        return permutation;
    }

    public void setPermutation(int[] permutation) {
        this.permutation = permutation;
    }
}

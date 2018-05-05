public class Runner {
    public static void main(String[] args) {
        String instanceFile = "src//instances//KC10-2fl-1rl.dat";
        NSGAII nsga2 = new NSGAII(instanceFile, 1, 15);

        nsga2.initialize();
    }


}

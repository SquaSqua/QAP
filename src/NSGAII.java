import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class NSGAII {
    private int[][] distanceMatrix;
    private int[][][] matricesArray;
    private int generations;
    private int pop_size;
    private double cross_prob;
    private double mut_prob;
    private int flowsNumber;
    String instanceFile;
    ArrayList<Individual> population = new ArrayList<>();
    ArrayList<ArrayList<Individual>> paretoFronts = new ArrayList<>();

    public NSGAII(String instanceFile, int generations, int pop_size, double cross_prob, double mut_prob) {
        this.generations = generations;
        this.pop_size = pop_size;
        this.instanceFile = instanceFile;
        this.cross_prob = cross_prob;
        this.mut_prob = mut_prob;
    }

    public void initialize() {
        QAPIO reader = new QAPIO();
        try {
            reader.readDefinition(instanceFile);
            distanceMatrix = reader.getDistanceMatrix();
            matricesArray = reader.getMatricesArray();
            flowsNumber = reader.getFlowsNumber();
            int individual_size = reader.getIndividual_size();
            ArrayList<Integer> list = new ArrayList();
            for (int i = 0; i < individual_size; i++) {
                list.add(i);
            }
            Individual individual;

            for (int i = 0; i < pop_size; i++) {
                Collections.shuffle(list);
                int[] permutation = new int[individual_size];
                for (int j = 0; j < individual_size; j++) {
                    permutation[j] = list.get(j);
                }
                individual = new Individual(permutation, flowsNumber);
                population.add(individual);
            }
            setFitnesses();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void setFitnesses() {
        for (int i = 0; i < population.size(); i++) {
            for (int j = 0; j < population.get(i).fitnessArray.length; j++) {
                population.get(i).fitnessArray[j] = population.get(i).fitness(distanceMatrix, matricesArray[j]);
            }
        }
    }

    public String frontGenerator() {

        paretoFronts.add(new ArrayList<>());
        for(int i = 0; i < population.size(); i++) {
            for (int j = 0; j < paretoFronts.size(); j++) {
                ArrayList<Individual> currentFront = paretoFronts.get(j);
                if (currentFront.size() == 0) {
                    currentFront.add(population.get(i));
                    break;
                } else {
                    for (int k = 0; k < currentFront.size(); k++) {
                        int compared = population.get(i).compareTo(currentFront.get(k));
                        if ((compared == 0) && (k == currentFront.size() - 1)) {
                            currentFront.add(population.get(i));
                            if(i < population.size() - 1) {
                                i++;
                                j = -1;
                            }else {
                                j = paretoFronts.size();
                            }
                            break;
                        } else if (compared == -1) {
                            //zamiana miejsc
                            ArrayList<Individual> betterFront = new ArrayList<>();
                            betterFront.add(population.get(i));
                            for(int z = 0; z < k; ) {
                                betterFront.add(currentFront.get(z));
                                currentFront.remove(z);
                                k--;
                            }
                            for(int z = 1; z < currentFront.size(); z++) {
                                if(population.get(i).compareTo(currentFront.get(z)) == 0) {
                                    betterFront.add(currentFront.get(z));
                                    currentFront.remove(z);
                                    z--;
                                }
                            }
                            paretoFronts.add(j, betterFront);
                            if(i < population.size() - 1) {
                                i++;
                                j = -1;
                            }else {
                                j = paretoFronts.size();
                            }
                            break;
                        } else if (compared == 1) {
                            //nowy front
                            if (paretoFronts.size() < j + 2) {
                                paretoFronts.add(new ArrayList<>());
                                paretoFronts.get(j + 1).add(population.get(i));
                                if(i < population.size() - 1) {
                                    i++;
                                    j = -1;
                                }else {
                                    j = paretoFronts.size();
                                }
                                break;
                            }
                            else {
                                break;
                            }
                        }
                    }
                }
            }

        }
//        for(ArrayList<Individual> a : paretoFronts) {
//            System.out.println("*************************");
//            for(Individual i : a) {
//                i.toString();
//            }
//        }
        return dataGenerator();
    }

    public String dataGenerator() {
        String result = "";
        StringBuilder sB = new StringBuilder(result);
        for(ArrayList<Individual> a : paretoFronts) {
            for(Individual i : a) {
                for(int j = 0; j < i.fitnessArray.length; j++) {
                    sB.append(i.fitnessArray[j] + ", ");
                }
                sB.append("\n");
            }
            sB.append("\n");
        }
        result = sB.toString();
        return result;
    }
//    public void putInPareto() {
//        for(int i = 0; i < population.size(); i++) {
//            Individual obj1 = population.get(i);
//            ArrayList<Individual> setOfDominated = new ArrayList<>();
//            int dominatingCount = 0;
//            for(int j = 0; j < population.size(); j++) {
//                Individual obj2 = population.get(j);
//                int compared = obj1.compareTo(obj2);
//                if(compared == -1) {
//                    setOfDominated.add(obj2);
//                }
//                else {
//                    if(compared == 1) {
//                        dominatingCount += 1;
//                    }
//                }
//            }
//            obj1.setDominatingCount(dominatingCount);
//            obj1.setSetOfDominated(setOfDominated);
//        }
//
//        for(Individual i : population) {
//            System.out.println("********************");
//            i.toString();
//            System.out.println("gorszych ode mnie " + i.getDominatingCount() + ", lepszych ode mnie " + i.getSetOfDominated().size());
//        }
//
//
//
//        ArrayList<Individual> temp = new ArrayList<>(population);
//        int currentRank = 1;
//        ArrayList<Individual> currentFront = new ArrayList<>();
//        boolean nextFront = false;
//        ArrayList<Individual> restList = new ArrayList<>();
//        do {
//            for(Iterator itr = temp.iterator(); itr.hasNext();) {
//                Individual obj = (Individual)itr.next();
//                if(obj.getDominatingCount() == 0) {
//                    obj.setRank(currentRank);
//                    currentFront.add(obj);
//                    for(Iterator internItr = obj.getSetOfDominated().iterator(); internItr.hasNext(); ) {
//                        Individual obj2 = (Individual)internItr.next();
//                        obj2.setDominatingCount(obj2.getDominatingCount() - 1);
//                        if(obj2.getDominatingCount() == 0) {
//                            nextFront = true;
//                        }
//                    }
//                    if(nextFront) {
//                        nextFront = false;
//                        paretoFronts.add(currentFront);
//                        currentFront = new ArrayList<>();
//                    }
//                }
//                else {
//                    restList.add(obj);
//                }
//            }
//            temp = restList;
//            restList = new ArrayList<>();
//        }
//        while(!temp.isEmpty());
//
//
////        for(ArrayList<Individual> a : paretoFronts) {
////            System.out.println("********************");
////            for(Individual i : a) {
////                i.toString();
////            }
////        }
//    }

    /*************************************************************************************************************************/

//    public ArrayList<Individual> matingPool() {
//
//    }

    public void mutation(Individual ind) {
        int[] permutation = ind.getPermutation();
        int length = permutation.length;
        Random rand = new Random();

        for(int i = 0; i < length; i++) {
            if(Math.random() < mut_prob) {
                int index = rand.nextInt(length);
                int temp = permutation[index];
                permutation[index] = permutation[i];
                permutation[i] = temp;
            }
        }
        ind.setPermutation(permutation);
    }

    public Individual[] crossing_OrderedX(Individual ind1, Individual ind2)
    {
        Individual[] result = new Individual[2];
        if(Math.random() < cross_prob) {
            int[] perm1 = ind1.getPermutation();
            int[] perm2 = ind2.getPermutation();
            int[] res1 = new int[perm1.length];
            int[] res2 = new int[perm1.length];
            Random rand = new Random();
            int first = rand.nextInt(perm1.length);
            int second = rand.nextInt(perm1.length);
            if(first > second) {
                int temp = first;
                first = second;
                second = temp;
            }
            for(int i = first; i < second; i++) {
                res1[i] = perm2[i];
                res2[i] = perm1[i];
            }
            for(int i = 0; i < perm1.length; i++) {
                if(i < first || i > second) {

                }
            }
        }
        else {
            result[0] = ind1;
            result[1] = ind2;
        }
        return result;
    }
    public Individual[] crossing_CX(Individual ind1, Individual ind2) {
        if()
    }
    public Individual[] crossing_PMX(Individual ind1, Individual ind2) {
        if()
    }
}


import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;

public class NSGAII {
    private int[][] distanceMatrix;
    private int[][][] matricesArray;
    private int generations;
    private int pop_size;
    private int flowsNumber;
    String instanceFile;
    ArrayList<Individual> population = new ArrayList<>();
    ArrayList<ArrayList<Individual>> paretoFronts = new ArrayList<>();

    public NSGAII(String instanceFile, int generations, int pop_size) {
        this.generations = generations;
        this.pop_size = pop_size;
        this.instanceFile = instanceFile;
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

    public void frontGenerator() {
        ArrayList<Individual> currentFront = new ArrayList<>();
        currentFront.add(population.get(0));
        paretoFronts.add(currentFront);
        for(int i = 0; i < population.size(); i++) {
            for(int j = 0; j < paretoFronts.size(); j++) {
                ArrayList<Individual> temp = paretoFronts.get(j);
                if(temp.size() == 0) {
                    temp.add(population.get(i));
                }
                else {
                    for(int k = 0; k < temp.size(); k++) {
                        if((population.get(i).compareTo(temp.get(k)) == 0) && (k == temp.size() - 1)) {
                            temp.add(population.get(i));
                        }
                        else if(population.get(i).compareTo(temp.get(k)) == -1) {
                            //zamiana miejsc
                        }
                        else if(population.get(i).compareTo(temp.get(k)) == 1) {
                            //nowy front
                            break;
                        }
                    }
                }


            }
        }
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
}


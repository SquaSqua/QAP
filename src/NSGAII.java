import java.awt.*;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class NSGAII {
    private int[][] distanceMatrix;
    private int[][][] matricesArray;
    Point ideal = new Point();
    Point nadir = new Point();
    private int generations;
    private int pop_size;
    private double cross_prob;
    private double mut_prob;
    private int tournamentSize;
    private int flowsNumber;
    private String instanceFile;
    private int max_distance;
    private int max_flows;
    ArrayList<Individual> population = new ArrayList<>();
    private ArrayList<ArrayList<Individual>> paretoFronts = new ArrayList<>();
    ArrayList<Individual> archive = new ArrayList<>();

    public NSGAII(String instanceFile, int generations, int pop_size, double cross_prob, double mut_prob, int tournamentSize) {
        this.generations = generations;
        this.pop_size = pop_size;
        this.instanceFile = instanceFile;
        this.cross_prob = cross_prob;
        this.mut_prob = mut_prob;
        this.tournamentSize = tournamentSize;
    }

    public String NSGAII() {

        String measures = "", archivePrint = "";
        StringBuilder sBMeasures = new StringBuilder(measures);
        StringBuilder sBArchivePrint = new StringBuilder(archivePrint);
        initialize();
        paretoFronts = frontGenerator(population);
        rankAssignment(paretoFronts);
        archive = paretoFronts.get(0);
        crowdingDistanceSetter();

        for(int z = 0; z < generations; z++) {
            ArrayList<Individual> offspring = matingPool();
            checkArchive(offspring);
            population.addAll(offspring);
            paretoFronts = frontGenerator(population);
            rankAssignment(paretoFronts);
            crowdingDistanceSetter();
            ArrayList<Individual> nextGeneration = new ArrayList<>();
            int frontNumber = 0;
            while (nextGeneration.size() < pop_size) {
                if (paretoFronts.get(frontNumber).size() < pop_size - nextGeneration.size()) {
                    nextGeneration.addAll(paretoFronts.get(frontNumber));
                    frontNumber++;
                } else {
                    crowdingDistanceSorting();
                    for (int i = 0; nextGeneration.size() < pop_size; i++) {
                        nextGeneration.add(paretoFronts.get(frontNumber).get(i));
                    }
                }
            }
            population = nextGeneration;
            paretoFronts = frontGenerator(population);
            rankAssignment(paretoFronts);
        }
        sBMeasures.append(ED_measure(paretoFronts.get(0)) + ", " + PFS_measure() + ", " + HV_measure());
        sBMeasures.append("\n");
        sBArchivePrint.append(printPF(archive, sBArchivePrint));
//        System.out.println(sBArchivePrint.toString());
        sBMeasures.append(sBArchivePrint.toString());
        measures = sBMeasures.toString();

        return measures;
    }

    public void initialize() {
        QAPIO reader = new QAPIO();
        try {
            reader.readDefinition(instanceFile);
            distanceMatrix = reader.getDistanceMatrix();
            matricesArray = reader.getMatricesArray();
            flowsNumber = reader.getFlowsNumber();
            int individual_size = reader.getIndividual_size();
            max_distance = reader.getMax_distance();
            max_flows = reader.getMax_flows();

            findIdealAndNadir(individual_size);

            ArrayList<Integer> list = new ArrayList<>();
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

    public void checkArchive(ArrayList<Individual> group) {
        for(int i = 0; i < group.size(); i++) {
            boolean nonDominated = true;
            for(int j = 0; nonDominated && j < archive.size(); j++) {
                if(group.get(i).compareTo(archive.get(j)) == -1) {
                    if(!archive.contains(group.get(i)))
                        archive.set(j, group.get(i));
                    else {
                        archive.remove(j);
                        j--;
                    }
                }
                else if(group.get(i).compareTo(archive.get(j)) == 1)
                    nonDominated = false;
            }
            if(nonDominated && !archive.contains(group.get(i)))
                archive.add(group.get(i));
        }
    }

    public void setFitnesses() {
        for (int i = 0; i < population.size(); i++) {
            setFitness(population.get(i));
        }
    }

    public void setFitness(Individual ind) {
        for (int j = 0; j < ind.fitnessArray.length; j++) {
            ind.fitnessArray[j] = ind.fitness(distanceMatrix, matricesArray[j]);
        }
    }

    public ArrayList<ArrayList<Individual>> frontGenerator(ArrayList<Individual> group) {

        ArrayList<ArrayList<Individual>> fronts = new ArrayList<>();
//        paretoFronts.clear();
        fronts.add(new ArrayList<>());
        for(int i = 0; i < group.size(); i++) {
            for (int j = 0; j < fronts.size(); j++) {
                ArrayList<Individual> currentFront = fronts.get(j);
                if (currentFront.size() == 0) {
                    currentFront.add(group.get(i));
                    break;
                } else {
                    for (int k = 0; k < currentFront.size(); k++) {
                        int compared = group.get(i).compareTo(currentFront.get(k));
                        if ((compared == 0) && (k == currentFront.size() - 1)) {
                            currentFront.add(group.get(i));
                            if(i < group.size() - 1) {
                                i++;
                                j = -1;
                            }else {
                                j = fronts.size();
                            }
                            break;
                        } else if (compared == -1) {
                            //zamiana miejsc
                            ArrayList<Individual> betterFront = new ArrayList<>();
                            betterFront.add(group.get(i));
                            for(int z = 0; z < k; ) {
                                betterFront.add(currentFront.get(z));
                                currentFront.remove(z);
                                k--;
                            }
                            for(int z = 1; z < currentFront.size(); z++) {
                                if(group.get(i).compareTo(currentFront.get(z)) == 0) {
                                    betterFront.add(currentFront.get(z));
                                    currentFront.remove(z);
                                    z--;
                                }
                            }
                            fronts.add(j, betterFront);
                            if(i < group.size() - 1) {
                                i++;
                                j = -1;
                            }else {
                                j = fronts.size();
                            }
                            break;
                        } else if (compared == 1) {
                            //nowy front
                            if (fronts.size() < j + 2) {
                                fronts.add(new ArrayList<>());
                                fronts.get(j + 1).add(group.get(i));
                                if(i < group.size() - 1) {
                                    i++;
                                    j = -1;
                                }else {
                                    j = fronts.size();
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
//        crowdingDistanceSetter();
//
//        for(ArrayList<Individual> a : paretoFronts) {
//            System.out.println("*************************");
//            for(Individual i : a) {
//                i.toString();
//                System.out.println("CrowdingDistance: " + i.getCrowdingDistance());
//            }
//        }
//        return dataGenerator();
        return fronts;
    }

    public void rankAssignment(ArrayList<ArrayList<Individual>> fronts) {
        for(int i = 0; i < fronts.size(); i++) {
            for(int j = 0; j < fronts.get(i).size(); j++) {
                fronts.get(i).get(j).setRank(i + 1);
            }
        }
    }

    public void objectiveSorting() {
        for(int i = 0; i < paretoFronts.size(); i++) {
            Collections.sort(paretoFronts.get(i), new ObjectiveFrontComparator());
        }
    }

    public void crowdingDistanceSorting() {
        for(int i = 0; i < paretoFronts.size(); i++) {
            Collections.sort(paretoFronts.get(i), new ObjectiveFrontComparator());
        }
    }

    public void crowdingDistanceSetter() {
        objectiveSorting();
        for(int i = 0; i < paretoFronts.size(); i++) {
            paretoFronts.get(i).get(0).setCrowdingDistance(Double.POSITIVE_INFINITY);
            paretoFronts.get(i).get(paretoFronts.get(i).size() - 1).setCrowdingDistance(Double.POSITIVE_INFINITY);

            for(int j = 1; j < paretoFronts.get(i).size() - 1; j++) {
                Individual currentInd = paretoFronts.get(i).get(j);
                double crowdingDistance = 0;
                for(int z = 0; z < currentInd.fitnessArray.length; z++) {
                    double numerator = Math.abs(paretoFronts.get(i).get(j + 1).fitnessArray[z]
                            - paretoFronts.get(i).get(j - 1).fitnessArray[z]);
                    double denominator = Math.abs(paretoFronts.get(i).get(0).fitnessArray[z]
                            - paretoFronts.get(i).get(paretoFronts.get(i).size() - 1).fitnessArray[z]);
                    crowdingDistance += numerator / denominator;
                }
                currentInd.setCrowdingDistance(crowdingDistance);
            }
        }
    }

    public String dataGenerator() {
        String result = "";
        StringBuilder sB = new StringBuilder(result);
        for(ArrayList<Individual> a : paretoFronts) {
            sB.append(printPF(a, sB));
            sB.append("\n");
        }
        result = sB.toString();
        return result;
    }

    public String printPF(ArrayList<Individual> a, StringBuilder sB) {
        for(Individual i : a) {
            for(int j = 0; j < i.fitnessArray.length; j++) {
                sB.append(i.fitnessArray[j] + ", ");
            }
            sB.append("\n");
        }
        return sB.toString();
    }

    public Individual tournamentSelection() {
        Individual[] tournament = new Individual[tournamentSize];
        Random random = new Random();
        for(int i = 0; i < tournamentSize; i++) {
            tournament[i] = population.get(random.nextInt(pop_size));
        }
        Individual max = tournament[0];
        for(int i = 0; i < tournamentSize; i++) {
            if(tournament[i].getRank() < max.getRank()) {
                max = tournament[i];
            }
            else if(tournament[i].getRank() == max.getRank() && tournament[i].getCrowdingDistance() > max.getCrowdingDistance()) {
                max = tournament[i];
            }
        }
        Individual res = new Individual(max.getPermutation(), max.fitnessArray.length);
        setFitness(res);
        return res;
    }

    public ArrayList<Individual> matingPool() {

        ArrayList<Individual> offspring = new ArrayList<>();
        while(offspring.size() < pop_size) {
            Individual ind1 = tournamentSelection();
            Individual ind2 = tournamentSelection();
            Individual[] crossed = crossing_OX(ind1, ind2);
            mutation(crossed[0]);
            mutation(crossed[1]);
            offspring.add(crossed[0]);
            offspring.add(crossed[1]);
        }
        return offspring;
    }

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

    public Individual[] crossing_OX(Individual ind1, Individual ind2)
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
            for(int i = first; i <= second; i++) {
                res1[i] = perm1[i];
                res2[i] = perm2[i];
            }
            res1 = fillTheRest(perm1, perm2, first, second, res1);
            res2 = fillTheRest(perm2, perm1, first, second, res2);
            result[0] = new Individual(res1, ind1.fitnessArray.length);
            result[1] = new Individual(res2, ind2.fitnessArray.length);

        }
        else {
            result[0] = new Individual(ind1.getPermutation(), ind1.fitnessArray.length);
            result[1] = new Individual(ind2.getPermutation(), ind2.fitnessArray.length);
        }
        setFitness(result[0]);
        setFitness(result[1]);
        return result;
    }

    public int[] fillTheRest(int[] perm1, int[] perm2, int first, int second, int[] res) {
        int j = 0;
        int[] result = res;
        for(int i = 0; i < perm2.length; i++) {
            boolean contains = false;
            for(int z = first; z <= second && !contains; z++) {
                if(perm1[z] == perm2[i]) {
                    contains = true;
                }
            }
            if(!contains) {
                if(j == first) {
                    j = second + 1;
                }
                result[j] = perm2[i];
                j++;

            }
        }
        return result;
    }

//    public Individual[] crossing_CX(Individual ind1, Individual ind2) {
//        if()
//    }
//    public Individual[] crossing_PMX(Individual ind1, Individual ind2) {
//        if()
//    }

    public String ED_measure(ArrayList<Individual> group) {
        double sumED = 0;
        for(int i = 0; i < group.size(); i++) {
            Individual ind = group.get(i);
            sumED += Math.round(Math.sqrt(Long.valueOf((ind.fitnessArray[0] - ideal.x) * (ind.fitnessArray[0] - ideal.x) + (ind.fitnessArray[1] - ideal.y) * (ind.fitnessArray[1] - ideal.y))));
        }
        sumED = (sumED / group.size());

        return Math.round(sumED) + "";
    }

    public String PFS_measure() {
        return archive.size() + "";
    }

    public double HV_measure() {
        Collections.sort(archive, new ObjectiveFrontComparator());
        Long hyperVolume = 0L;
        int lastY = nadir.y;
        for(int i = 0; i < archive.size(); i++) {
            hyperVolume += ((nadir.x - archive.get(i).fitnessArray[0]) * (lastY - archive.get(i).fitnessArray[1]));
            lastY = archive.get(i).fitnessArray[1];
        }
        return hyperVolume;
    }

    public void findIdealAndNadir(int ind_size) {

        int minDistance = Integer.MAX_VALUE;
//        int maxDistance = 0;
        for(int i = 0; i < distanceMatrix.length; i++) {
            for(int j = 0; j < distanceMatrix[i].length; j++) {
                if(minDistance > distanceMatrix[i][j] && distanceMatrix[i][j] != 0) {
                    minDistance = distanceMatrix[i][j];
//                else if(maxDistance < distanceMatrix[i][j])
//                    maxDistance = distanceMatrix[i][j];
                }
            }
        }
        for(int i = 0; i < matricesArray.length; i++) {
            int min = Integer.MAX_VALUE/*, max = 0*/;
            for(int j = 0; j < matricesArray[i].length; j++) {
                for(int k = 0; k < matricesArray[i][j].length ; k++) {
                    int temp = matricesArray[i][j][k];
                    if(temp < min && temp != 0)
                        min = temp;
//                    else if(temp > max)
//                        max = temp;
                }

            }
            if(i == 0) {
                ideal.x = min * minDistance * ind_size;
                nadir.x = max_flows * max_distance * ind_size;
//                nadir.x = max * maxDistance * ind_size;
            }
            else {
                ideal.y = min * minDistance * ind_size;
                nadir.y = max_flows * max_distance * ind_size;
//                nadir.y = max * maxDistance * ind_size;
            }

        }
    }

}


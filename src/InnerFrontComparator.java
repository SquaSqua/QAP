import java.util.Comparator;

public class InnerFrontComparator implements Comparator<Individual> {

    @Override
    public int compare(Individual o1, Individual o2) {
        return Integer.compare(o1.fitnessArray[0], o2.fitnessArray[0]);
    }
}

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * Driver class.  Decided to implement this in Java for better thread performance (vs. Python)
 * @author Alex Hartford
 * Course: CS3851-021
 * Assignment: Lab 4 - Parallel Sorting
 * Spring 2017-2018
 * 19 April 2018
 */
public class AlgoRunner {

    public static void main(String[] args) {

        ArrayList<int[]> lists = generateLists(100);
        int[] bestCase = lists.get(0);
        int[] worstCase = lists.get(1);
        int[] likelyCase = lists.get(2);

        Algorithm quickSort = new QuickSort();
        Algorithm mergeSort = new MergeSort();

        System.out.println("QuickSort sorted\n" +
                Arrays.toString(likelyCase) +
                "\nin " + quickSort.sort(unique(likelyCase)) + "ns");

        System.out.println("MergeSort sorted\n" +
                Arrays.toString(likelyCase) +
                "\nin " + mergeSort.sort(unique(likelyCase)) + "ns");
    }

    /**
     * Returns best case, worst case, and most likely case lists to be analyzed.
     * @param inputLength - How long the lists will be
     * @return an ArrayList that can be indexed to get each list. Index 0 = best, 1 = worst, 2 = likely
     */
    private static ArrayList<int[]> generateLists(int inputLength) {
        Random random = new Random();

        int[] bestCase = new int[inputLength];
        int[] worstCase = new int[inputLength];
        int[] likelyCase = new int[inputLength];

        for (int i = 0; i < inputLength; i++) {
            bestCase[i] = i;
            worstCase[i] = inputLength - i;
            likelyCase[i] = random.nextInt(inputLength * 10);
        }

        ArrayList<int[]> listOfLists = new ArrayList<>();
        listOfLists.add(bestCase);
        listOfLists.add(worstCase);
        listOfLists.add(likelyCase);

        return listOfLists;
    }

    /**
     * Takes a generic list and makes a deep copy to guarantee it's not sorted by another algorithm
     * @param A - Array to make a deep copy of
     * @return a deep copy of the input array A
     */
    private static int[] unique(int[] A) {
        int[] Au = new int[A.length];
        System.arraycopy(A, 0, Au, 0, A.length);
        return Au;
    }
}

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
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

        ArrayList<int[]> lists = generateLists(10000);
        int[] bestCase = lists.get(0);
        int[] worstCase = lists.get(1);
        int[] likelyCase = lists.get(2);

        Algorithm quickSort = new QuickSort();
        Algorithm mergeSort = new MergeSort();

        Algorithm mQuickSort = new mQuickSort();
        Algorithm mMergeSort = new mMergeSort();

        Algorithm IQuickSort = new IQuickSort();

//        runTest("QuickSort", quickSort, "best case", bestCase);
//        runTest("MergeSort", mergeSort, "best case", bestCase);
//        runTest("Multi-Threaded QuickSort", mQuickSort, "best case", bestCase);
//        runTest("Multi-Threaded MergeSort", mMergeSort, "best case", bestCase);
//
//        runTest("QuickSort", quickSort, "worst case", worstCase);
//        runTest("MergeSort", mergeSort, "worst case", worstCase);
//        runTest("Multi-Threaded QuickSort", mQuickSort, "worst case", worstCase);
//        runTest("Multi-Threaded MergeSort", mMergeSort, "worst case", worstCase);

//        runTest("QuickSort", quickSort, "likely", likelyCase);
//        runTest("MergeSort", mergeSort, "likely", likelyCase);
//        runTest("Multi-Threaded QuickSort", mQuickSort, "likely", likelyCase);
//        runTest("Multi-Threaded MergeSort", mMergeSort, "likely", likelyCase);

        runTest("IQuickSort", IQuickSort, "best case", bestCase);
        runTest("IQuickSort", IQuickSort, "worst case", worstCase);
        runTest("IQuickSort", IQuickSort, "likely case", likelyCase);

//        comprehensiveAnalysisToCSV(mergeSort, mMergeSort);
//        comprehensiveAnalysisToCSV(quickSort, mergeSort, mQuickSort, mMergeSort);
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

    /**
     * Runs a specified algorithm on a specified list and prints relevant data
     * @param name - name of the algorithm, e.g. QuickSort
     * @param algo - the algorithm to run, e.g. Multi-Threaded QuickSort
     * @param inputName - the type of input, e.g. "likely"
     * @param input - the input to sort, e.g. [0, 1, 2, 5, 6, 4, 3]
     */
    private static void runTest(String name, Algorithm algo, String inputName, int[] input) {
        System.out.println("[" + inputName + "] " + name + " sorted" +
//                Arrays.toString(input) +
                " in " + algo.sort(unique(input)) + "ns");

    }

    /**
     * Huge workhorse that generates a csv file from running every algorithm on every set of input
     * Would have cleaned it up more but I plan to move back to Python after this threading lab.
     * @param algos - any algorithms I want to run the script on (always all 4)
     *           In this order: Quicksort -> mergesort -> mQuicksort -> mMergesort
     */
    private static void comprehensiveAnalysisToCSV(Algorithm... algos) {

        int numIterations = 20;

        ArrayList<int[]> bestLists = new ArrayList<>();
        ArrayList<int[]> worstLists = new ArrayList<>();
        ArrayList<int[]> likelyLists = new ArrayList<>();

        // populates lists with length 1000, 10000, 100000, 1000000, 10000000
        for (int i = 1000; i <= 10000; i *= 10) {
            ArrayList<int[]> lists = generateLists(i);
            bestLists.add(lists.get(0));
            worstLists.add(lists.get(1));
            likelyLists.add(lists.get(2));
        }

        ArrayList<ArrayList> inputLists = new ArrayList<>();
        inputLists.add(bestLists);
        inputLists.add(worstLists);
        inputLists.add(likelyLists);

        ArrayList<String> algoNames = new ArrayList<>();
        algoNames.add("QuickSort");
        algoNames.add("MergeSort");
        algoNames.add("mQuickSort");
        algoNames.add("mMergeSort");

        ArrayList<String> inputNames = new ArrayList<>();
        inputNames.add("BestCase");
        inputNames.add("WorstCase");
        inputNames.add("LikelyCase");

        try {
            Writer writer = new BufferedWriter(
                            new OutputStreamWriter(
                            new FileOutputStream("CS3851Lab4.csv")));

            for (String name : algoNames) {
                for (String inputName : inputNames) {
                    for (int[] input : bestLists) {
                        writer.write(name + " " + inputName + " " + input.length + ",");
                    }
                }
            }
            writer.write("\n");


            for (int i = 0; i < inputNames.size(); i++) {
                ArrayList<int[]> lists = inputLists.get(i);
                for (Algorithm algo : algos) {
                    for (int[] input : lists) {
                        long time = 0;
                        for (int j = 0; j < numIterations; j++) {
                            time += algo.sort(unique(input));
                        }
//                        time = algo.sort(unique(input));
                        writer.write(time / numIterations + ",");
                        writer.write(time + ",");
                    }
                }
            }

            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

/**
 * Iterative Multi-Threaded QuickSort
 * Recursive QuickSort was giving me stackoverflow errors so here we are.
 * @author Alex Hartford
 * Course: CS3851-021
 * Assignment: Lab 4 - Parallel Sorting
 * Spring 2017-2018
 * 19 April 2018
 */
public class IMQuickSort implements Algorithm {

//    private static int threadCount = 0;
    private final static int CPUCores = Runtime.getRuntime().availableProcessors();

    public String getName() {
        return "IMQuickSort";
    }

    @Override
    public long sort(int[] A) {
        long startTime = System.nanoTime();
        sort(A, 0, A.length - 1);
//        System.out.println("[Iterative Multi-Threaded QuickSort] Sorted Array: " + Arrays.toString(A));
        return System.nanoTime() - startTime;
    }

    @Override
    public int[] sort(int[] array, int lower, int upper) {
        try{
            ArrayList<Thread> threads = new ArrayList<>();
            int k = upper / CPUCores;
            int count = -1;
            while(count < upper) {
                Runnable task = () -> {
                    Algorithm quicksort = new IQuickSort();
                    if(count + k < array.length) {
                        quicksort.sort(array, count + 1, count + k);
                    } else {
                        quicksort.sort(array, count + 1, upper);
                    }
                };
                Thread thread = new Thread(task);
                threads.add(thread);
            }
            for (Thread thread : threads) {
                thread.start();
            }
            for (Thread thread : threads) {
                thread.join();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return array;
    }
}

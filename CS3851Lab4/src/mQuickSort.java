import java.util.Arrays;

/**
 * Multi-Threaded QuickSort
 * @author Alex Hartford
 * Course: CS3851-021
 * Assignment: Lab 4 - Parallel Sorting
 * Spring 2017-2018
 * 19 April 2018
 */
public class mQuickSort implements Algorithm {

    @Override
    public String sort(int A[]) {
        long startTime = System.nanoTime();
        quicksort(A, 0, A.length - 1);
        System.out.println("[QuickSort] Sorted Array: " + Arrays.toString(A));
        return String.valueOf(System.nanoTime() - startTime);
    }

    private int partition(int A[], int low, int high) {
        int pivot = A[high];
        int i = (low - 1);
        for (int j = low; j < high; j++) {
            if (A[j] <= pivot) {
                i++;
                int temp = A[i];
                A[i] = A[j];
                A[j] = temp;
            }
        }

        int temp = A[i + 1];
        A[i + 1] = A[high];
        A[high] = temp;

        return i + 1;
    }

    private void quicksort(int A[], int low, int high) {
        if (low < high) {
            int i = partition(A, low, high);

            Thread t1 = new Thread(() -> quicksort(A, low, i - 1));
            Thread t2 = new Thread(() -> quicksort(A, i + 1, high));

            t1.start();
            t2.start();

            try {
                t1.join();
                t2.join();
            } catch (InterruptedException e) {
                System.out.println("[QuickSort] Threads failed to join");
                e.printStackTrace();
            }
        }
    }
}

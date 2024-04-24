import java.util.Arrays;
import java.util.Random;

class MergeSort {
    
    private static final int MAX_THREADS = 4;

    private static class SortThread extends Thread {
        SortThread(Integer[] array, int begin, int end) {
            super(() -> mergeSort(array, begin, end));
            start();
        }
    }

    public static void threadedSort(Integer[] array) {
        final int length = array.length;

        int chunkSize = length % MAX_THREADS == 0 ? length / MAX_THREADS : length / (MAX_THREADS - 1);
        chunkSize = Math.max(chunkSize, MAX_THREADS);

        final SortThread[] threads = new SortThread[MAX_THREADS];

        for (int i = 0; i < length; i += chunkSize) {
            int begin = i;
            int remaining = length - i;
            int end = remaining < chunkSize ? i + (remaining - 1) : i + (chunkSize - 1);
            threads[i / chunkSize] = new SortThread(array, begin, end);
        }

        for (SortThread thread : threads) {
            try {
                if (thread != null)
                    thread.join();
            } catch (InterruptedException ignored) {}
        }

        for (int i = 0; i < length; i += chunkSize) {
            int mid = i == 0 ? 0 : i - 1;
            int remaining = length - i;
            int end = remaining < chunkSize ? i + (remaining - 1) : i + (chunkSize - 1);
            merge(array, 0, mid, end);
        }
    }

    public static void mergeSort(Integer[] array, int begin, int end) {
        if (begin < end) {
            int mid = (begin + end) / 2;
            mergeSort(array, begin, mid);
            mergeSort(array, mid + 1, end);
            merge(array, begin, mid, end);
        }
    }

    public static void merge(Integer[] array, int begin, int mid, int end) {
        Integer[] temp = new Integer[end - begin + 1];

        int i = begin, j = mid + 1;
        int k = 0;
        while (i <= mid && j <= end) {
            if (array[i] <= array[j]) {
                temp[k] = array[i];
                i++;
            } else {
                temp[k] = array[j];
                j++;
            }
            k++;
        }
        while (i <= mid) {
            temp[k] = array[i];
            i++;
            k++;
        }
        while (j <= end) {
            temp[k] = array[j];
            j++;
            k++;
        }

        for (i = begin, k = 0; i <= end; i++, k++) {
            array[i] = temp[k];
        }
    }
}

public class MultiThreadedMergeSort {

    private Random random = new Random();
    private int size;
    private Integer[] list;

    public MultiThreadedMergeSort(int size) {
        this.size = size;
        this.list = new Integer[1000000];
        generateRandomArray();
    }

    public static void main(String[] args) {
        int size = new Random().nextInt(100);
        MultiThreadedMergeSort sorter = new MultiThreadedMergeSort(size);

        System.out.println("\nInput.length = " + sorter.list.length + '\n');

        Integer[] arr = Arrays.copyOf(sorter.list, sorter.list.length);

        long startTime = System.currentTimeMillis();
        MergeSort.threadedSort(arr);
        long elapsedTime = System.currentTimeMillis() - startTime;

        System.out.println("Time spent for custom multi-threaded recursive merge_sort(): " + elapsedTime + "ms");

        startTime = System.currentTimeMillis();
        MergeSort.mergeSort(arr, 0, arr.length - 1);
        elapsedTime = System.currentTimeMillis() - startTime;

        System.out.println("Time spent for custom single-threaded recursive merge_sort(): " + elapsedTime + "ms");
    }

    public void generateRandomArray() {
        for (int i = 0; i < list.length; i++) {
            list[i] = random.nextInt(size + (size - 1)) - (size - 1);
        }
    }
}




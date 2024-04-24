import java.util.Random;

public class MultiThreadedBubbleSort {

    public static void main(String[] args) {
        int[] array1 = generateRandomArray(200000);
        int[] array2 = array1.clone();

        long startTime, endTime;
        double sequentialTime, parallelTime;

        // Sequential Bubble Sort
        startTime = System.currentTimeMillis();
        bubbleSort(array1);
        endTime = System.currentTimeMillis();
        sequentialTime = (endTime - startTime) / 1000.0;
        System.out.println("Sequential Bubble Sort Time: " + sequentialTime + " seconds");

        // Parallel Bubble Sort
        startTime = System.currentTimeMillis();
        parallelBubbleSort(array2);
        endTime = System.currentTimeMillis();
        parallelTime = (endTime - startTime) / 1000.0;
        System.out.println("Parallel Bubble Sort Time: " + parallelTime + " seconds");

        System.out.println("Speedup: " + (sequentialTime / parallelTime));
    }

    public static void bubbleSort(int[] arr) {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
    }

    public static void parallelBubbleSort(int[] arr) {
        int cores = Runtime.getRuntime().availableProcessors();
        int chunkSize = arr.length / cores;

        Thread[] threads = new Thread[cores];
        for (int i = 0; i < cores; i++) {
            final int startIndex = i * chunkSize;
            final int endIndex = (i == cores - 1) ? arr.length : (i + 1) * chunkSize;
            threads[i] = new Thread(() -> bubbleSortChunk(arr, startIndex, endIndex));
            threads[i].start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Final bubble pass to ensure correctness
        bubbleSort(arr);
    }

    public static void bubbleSortChunk(int[] arr, int start, int end) {
        for (int i = start; i < end - 1; i++) {
            for (int j = start; j < end - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
    }

    public static int[] generateRandomArray(int size) {
        int[] array = new int[size];
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            array[i] = random.nextInt(1000);
        }
        return array;
    }
}

import java.util.Arrays;
import java.util.OptionalDouble;
import java.util.Random;

public class ParallelReduction {

    public static void parallelReduction(int[] array) {
        int min = Arrays.stream(array)
                        .parallel()  // Make the stream parallel
                        .reduce(Integer.MAX_VALUE, Integer::min);
        
        int max = Arrays.stream(array)
                        .parallel()  
                        .reduce(Integer.MIN_VALUE, Integer::max);
        
        long sum = Arrays.stream(array)
                         .parallel()  
                         .sum();
        
        OptionalDouble average = Arrays.stream(array)
                         .parallel()  
                         .average();
        
        System.out.println("Parallel Min: " + min);
        System.out.println("Parallel Max: " + max);
        System.out.println("Parallel Sum: " + sum);
        System.out.println("Parallel Average: " + (average.isPresent() ? average.getAsDouble() : "NaN"));
    }

    public static void sequentialReduction(int[] array) {
        int min = Arrays.stream(array).min().orElse(Integer.MAX_VALUE);
        int max = Arrays.stream(array).max().orElse(Integer.MIN_VALUE);
        long sum = Arrays.stream(array).sum();
        OptionalDouble average = Arrays.stream(array).average();
        
        System.out.println("Sequential Min: " + min);
        System.out.println("Sequential Max: " + max);
        System.out.println("Sequential Sum: " + sum);
        System.out.println("Sequential Average: " + (average.isPresent() ? average.getAsDouble() : "NaN"));
    }
    public static void main(String[] args) {
        // Generate a random array
        Random random = new Random();
        int[] array = new int[35000000];
        for (int i = 0; i < array.length; i++) {
            array[i] = random.nextInt(10000);
        }

        // Perform parallel reduction and measure time
        long startTime = System.currentTimeMillis();
        parallelReduction(array);
        long parallelTime = System.currentTimeMillis() - startTime;
        System.out.println("Parallel Time: " + parallelTime + "ms");

        // Perform sequential reduction and measure time
        startTime = System.currentTimeMillis();
        sequentialReduction(array);
        long sequentialTime = System.currentTimeMillis() - startTime;
        System.out.println("Sequential Time: " + sequentialTime + "ms");
    }
}

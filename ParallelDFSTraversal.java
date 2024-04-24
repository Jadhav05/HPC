import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ParallelDFSTraversal {
    private int V; // Number of vertices
    private int E; // Number of edges
    private List<List<Integer>> adj; // Adjacency list

    public ParallelDFSTraversal(int V, int E) {
        this.V = V;
        this.E = E;
        adj = new ArrayList<>(V);
        for (int i = 0; i < V; ++i)
            adj.add(new ArrayList<>());
    }

    // Function to add an edge into the graph
    public void addEdge(int v, int w) {
        adj.get(v).add(w); // Add w to v's list.
    }

    // A function used by DFS
    private void DFSUtil(int v, boolean visited[]) {
        Stack<Integer> stack = new Stack<>();
        stack.push(v);

        while (!stack.isEmpty()) {
            int current = stack.pop();
            if (!visited[current]) {
                visited[current] = true;
                for (int neighbor : adj.get(current)) {
                    if (!visited[neighbor]) {
                        stack.push(neighbor);
                    }
                }
            }
        }
    }

    // The function to do DFS traversal sequentially. It uses recursive DFSUtil()
    public long sequentialDFS() {
        boolean[] visited = new boolean[V];
        long startTime = System.nanoTime();
        for (int i = 0; i < V; ++i) {
            if (!visited[i]) {
                DFSUtil(i, visited);
            }
        }
        long endTime = System.nanoTime();
        return TimeUnit.NANOSECONDS.toMillis(endTime - startTime);
    }

    // The function to do DFS traversal in parallel. It uses ExecutorService for parallelism.
    public long parallelDFS() {
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        boolean[] visited = new boolean[V];

        long startTime = System.nanoTime();

        for (int i = 0; i < V; ++i) {
            if (!visited[i]) {
                final int current = i;
                executor.submit(() -> DFSUtil(current, visited));
            }
        }

        executor.shutdown();

        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long endTime = System.nanoTime();
        return TimeUnit.NANOSECONDS.toMillis(endTime - startTime);
    }

    public static void main(String args[]) {
        int V = 1000000; 
        int E = 1500000; 
        ParallelDFSTraversal g = new ParallelDFSTraversal(V, E);

        // Generating random edges for the graph
        for (int i = 0; i < E; i++) {
            int v = (int) (Math.random() * V);
            int w = (int) (Math.random() * V);
            g.addEdge(v, w);
        }

        // Perform sequential DFS
        long sequentialTime = g.sequentialDFS();
        System.out.println("Sequential DFS Execution Time: " + sequentialTime + " milliseconds");

        // Perform parallel DFS
        long parallelTime = g.parallelDFS();
        System.out.println("Parallel DFS Execution Time: " + parallelTime + " milliseconds");
    }
}

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class Graph {
    private int V;
    private ArrayList<ArrayList<Integer>> adj;

    public Graph(int v) {
        V = v;
        adj = new ArrayList<>(V);
        for (int i = 0; i < V; ++i)
            adj.add(new ArrayList<>());
    }

    void addEdge(int v, int w) {
        adj.get(v).add(w);
        adj.get(w).add(v);
    }

    void generateRandomGraph(int numNodes, int numEdges) {
        Random random = new Random();
        for (int i = 0; i < numEdges; i++) {
            int node1 = random.nextInt(numNodes);
            int node2 = random.nextInt(numNodes);
            addEdge(node1, node2);
        }
    }

    long parallelBFS(int source) {
        boolean[] visited = new boolean[V];
        Queue<Integer> queue = new LinkedList<>();
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        visited[source] = true;
        queue.add(source);

        long startTime = System.nanoTime();
        while (!queue.isEmpty()) {
            int u = queue.poll();

            for (int v : adj.get(u)) {
                if (!visited[v]) {
                    visited[v] = true;
                    final int finalV = v;
                    executor.execute(() -> {
                        queue.add(finalV);
                    });
                }
            }
        }
        executor.shutdown();
        return System.nanoTime() - startTime;
    }

    long sequentialBFS(int source) {
        boolean[] visited = new boolean[V];
        Queue<Integer> queue = new LinkedList<>();

        visited[source] = true;
        queue.add(source);

        long startTime = System.nanoTime();
        while (!queue.isEmpty()) {
            int u = queue.poll();

            for (int v : adj.get(u)) {
                if (!visited[v]) {
                    visited[v] = true;
                    queue.add(v);
                }
            }
        }
        return System.nanoTime() - startTime;
    }
}

public class ParallelBFSTraversal {
    public static void main(String[] args) {
        int numNodes = 10000; // Number of nodes
        int numEdges = 20000; // Number of edges (adjust according to the graph density)
        Graph graph = new Graph(numNodes);
        graph.generateRandomGraph(numNodes, numEdges);

        long parallelBFSTime = graph.parallelBFS(0);
        System.out.println("Parallel BFS Time: " + parallelBFSTime + " nanoseconds");

        long sequentialBFSTime = graph.sequentialBFS(0);
        System.out.println("Sequential BFS Time: " + sequentialBFSTime + " nanoseconds");
    }
}

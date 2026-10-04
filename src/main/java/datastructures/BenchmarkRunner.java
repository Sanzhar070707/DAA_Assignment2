package datastructures;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Random;

public class BenchmarkRunner {

    public static void main(String[] args) {
        String csvFile = "results.csv";

        // Запись заголовков CSV
        try (PrintWriter writer = new PrintWriter(new FileWriter(csvFile))) {
            writer.println("workload,variant,structure,n,time_ms,steps,moves,comparisons");
        } catch (IOException e) {
            e.printStackTrace();
        }

        int[] sizes = {100, 1000, 10000, 100000};

        // --- W1 - Random Access ---
        for (String structure : new String[]{"DynamicArray", "MyLinkedList"}) {
            for (int n : sizes) {
                runW1(n, structure, csvFile);
            }
        }

        // --- W2 - Search ---
        for (String structure : new String[]{"DynamicArray", "MyLinkedList"}) {
            for (int n : sizes) {
                runW2(n, structure, csvFile);
            }
        }

        // --- W3 - Insert & Remove (Head & Middle) ---
        for (String variant : new String[]{"head", "middle"}) {
            for (String structure : new String[]{"DynamicArray", "MyLinkedList"}) {
                for (int n : sizes) {
                    runW3(n, variant, structure, csvFile);
                }
            }
        }

        // --- W4 - Priority Processing ---
        for (int n : sizes) {
            runW4(n, "MinHeap", csvFile);
        }

        System.out.println("Benchmarks completed! results.csv generated.");
    }

    private static void runW1(int n, String structure, String csvFile) {
        int runs = 5;
        double[] times = new double[runs];
        OperationMetrics lastMetrics = null;

        for (int r = 0; r < runs; r++) {
            OperationMetrics metrics = new OperationMetrics();
            Random rand = new Random(42);
            long start = System.nanoTime();

            if (structure.equals("DynamicArray")) {
                DynamicArray da = new DynamicArray(metrics);
                for (int i = 0; i < n; i++) da.add(rand.nextInt());

                Random indexGen = new Random(42);
                for (int i = 0; i < 10000; i++) {
                    da.get(indexGen.nextInt(n));
                }
            } else {
                MyLinkedList list = new MyLinkedList(metrics);
                for (int i = 0; i < n; i++) list.add(rand.nextInt());

                Random indexGen = new Random(42);
                for (int i = 0; i < 10000; i++) {
                    list.get(indexGen.nextInt(n));
                }
            }
            times[r] = (System.nanoTime() - start) / 1_000_000.0;
            lastMetrics = metrics;
        }

        double medianTime = getMedian(times);
        appendCsv(csvFile, "W1_random_access", "-", structure, n, medianTime,
                lastMetrics.getSteps(), lastMetrics.getMoves(), lastMetrics.getComparisons());
    }

    private static void runW2(int n, String structure, String csvFile) {
        int runs = 5;
        double[] times = new double[runs];
        OperationMetrics lastMetrics = null;

        for (int r = 0; r < runs; r++) {
            OperationMetrics metrics = new OperationMetrics();
            Random rand = new Random(42);
            int[] elements = new int[n];
            for (int i = 0; i < n; i++) elements[i] = rand.nextInt();

            int[] queries = new int[1000];
            Random qRand = new Random(42);
            for (int i = 0; i < 500; i++) queries[i] = elements[qRand.nextInt(n)];
            for (int i = 500; i < 1000; i++) queries[i] = qRand.nextInt();

            long start = System.nanoTime();
            if (structure.equals("DynamicArray")) {
                DynamicArray da = new DynamicArray(metrics);
                for (int val : elements) da.add(val);
                metrics.reset();
                for (int q : queries) da.contains(q);
            } else {
                MyLinkedList list = new MyLinkedList(metrics);
                for (int val : elements) list.add(val);
                metrics.reset();
                for (int q : queries) list.contains(q);
            }
            times[r] = (System.nanoTime() - start) / 1_000_000.0;
            lastMetrics = metrics;
        }

        double medianTime = getMedian(times);
        appendCsv(csvFile, "W2_search", "-", structure, n, medianTime,
                lastMetrics.getSteps(), lastMetrics.getMoves(), lastMetrics.getComparisons());
    }

    private static void runW3(int n, String variant, String structure, String csvFile) {
        int runs = 5;
        double[] times = new double[runs];
        OperationMetrics lastMetrics = null;
        int targetIndex = variant.equals("head") ? 0 : n / 2;
        if (targetIndex > n) targetIndex = n;

        for (int r = 0; r < runs; r++) {
            OperationMetrics metrics = new OperationMetrics();
            Random rand = new Random(42);

            if (structure.equals("DynamicArray")) {
                DynamicArray da = new DynamicArray(metrics);
                for (int i = 0; i < n; i++) da.add(rand.nextInt());
                metrics.reset();

                long start = System.nanoTime();
                for (int i = 0; i < 1000; i++) {
                    da.add(targetIndex, 999);
                    da.remove(targetIndex);
                }
                times[r] = (System.nanoTime() - start) / 1_000_000.0;
            } else {
                MyLinkedList list = new MyLinkedList(metrics);
                for (int i = 0; i < n; i++) list.add(rand.nextInt());
                metrics.reset();

                long start = System.nanoTime();
                for (int i = 0; i < 1000; i++) {
                    list.add(targetIndex, 999);
                    list.remove(targetIndex);
                }
                times[r] = (System.nanoTime() - start) / 1_000_000.0;
            }
            lastMetrics = metrics;
        }

        double medianTime = getMedian(times);
        appendCsv(csvFile, "W3_insert_remove", variant, structure, n, medianTime,
                lastMetrics.getSteps(), lastMetrics.getMoves(), lastMetrics.getComparisons());
    }

    private static void runW4(int n, String structure, String csvFile) {
        int runs = 5;
        double[] times = new double[runs];
        OperationMetrics lastMetrics = null;

        for (int r = 0; r < runs; r++) {
            OperationMetrics metrics = new OperationMetrics();
            Random rand = new Random(42);

            MinHeap heap = new MinHeap(metrics);
            for (int i = 0; i < n; i++) heap.insert(rand.nextInt());
            metrics.reset();

            long start = System.nanoTime();
            for (int i = 0; i < n; i++) heap.extractMin();
            times[r] = (System.nanoTime() - start) / 1_000_000.0;
            lastMetrics = metrics;
        }

        double medianTime = getMedian(times);
        appendCsv(csvFile, "W4_priority_processing", "-", structure, n, medianTime,
                lastMetrics.getSteps(), lastMetrics.getMoves(), lastMetrics.getComparisons());
    }

    private static double getMedian(double[] values) {
        Arrays.sort(values);
        return values[values.length / 2];
    }

    private static void appendCsv(String file, String workload, String variant, String structure, int n, double time, long steps, long moves, long comparisons) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(file, true))) {
            writer.printf("%s,%s,%s,%d,%.3f,%d,%d,%d%n", workload, variant, structure, n, time, steps, moves, comparisons);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
package datastructures;

import java.util.Arrays;

public class MinHeap {
    private int[] data;
    private int size;
    private final OperationMetrics metrics;

    public MinHeap(OperationMetrics metrics) {
        this.data = new int[10];
        this.size = 0;
        this.metrics = metrics;
    }

    public void insert(int x) {
        if (size == data.length) {
            resize();
        }
        data[size] = x;
        metrics.addMove();
        bubbleUp(size);
        size++;
    }

    public int peekMin() {
        if (size == 0) {
            throw new IllegalStateException("Heap is empty");
        }
        metrics.addStep();
        return data[0];
    }

    public int extractMin() {
        if (size == 0) {
            throw new IllegalStateException("Heap is empty");
        }
        int min = data[0];
        data[0] = data[size - 1];
        metrics.addMove();
        size--;
        if (size > 0) {
            bubbleDown(0);
        }
        return min;
    }

    public int size() {
        return size;
    }

    private void bubbleUp(int index) {
        int parent = (index - 1) / 2;
        while (index > 0) {
            metrics.addComparison();
            if (data[index] < data[parent]) {
                swap(index, parent);
                index = parent;
                parent = (index - 1) / 2;
            } else {
                break;
            }
        }
    }

    private void bubbleDown(int index) {
        int smallest = index;
        int left = 2 * index + 1;
        int right = 2 * index + 2;

        if (left < size) {
            metrics.addComparison();
            if (data[left] < data[smallest]) {
                smallest = left;
            }
        }
        if (right < size) {
            metrics.addComparison();
            if (data[right] < data[smallest]) {
                smallest = right;
            }
        }

        if (smallest != index) {
            swap(index, smallest);
            bubbleDown(smallest);
        }
    }

    private void swap(int i, int j) {
        int temp = data[i];
        data[i] = data[j];
        data[j] = temp;
        metrics.addMoves(2); // два перемещения при свапе
    }

    private void resize() {
        int newCapacity = data.length == 0 ? 10 : data.length * 2;
        metrics.addMoves(data.length);
        data = Arrays.copyOf(data, newCapacity);
    }
}
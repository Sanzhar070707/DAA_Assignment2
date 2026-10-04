package datastructures;

import java.util.Arrays;

public class DynamicArray {
    private int[] data;
    private int size;
    private final OperationMetrics metrics;

    public DynamicArray(OperationMetrics metrics) {
        this.data = new int[10];
        this.size = 0;
        this.metrics = metrics;
    }

    public void add(int x) {
        if (size == data.length) {
            resize();
        }
        data[size++] = x;
        metrics.addMove(); // запись элемента
    }

    public void add(int index, int x) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        if (size == data.length) {
            resize();
        }
        long shiftCount = size - index;
        if (shiftCount > 0) {
            System.arraycopy(data, index, data, index + 1, (int) shiftCount);
            metrics.addMoves(shiftCount); // сдвиги элементов в массиве
        }
        data[index] = x;
        metrics.addMove();
        size++;
    }

    public int remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        int removed = data[index];
        long shiftCount = size - index - 1;
        if (shiftCount > 0) {
            System.arraycopy(data, index + 1, data, index, (int) shiftCount);
            metrics.addMoves(shiftCount); // сдвиги при удалении
        }
        size--;
        return removed;
    }

    public int get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        metrics.addStep(); // чтение ячейки массива
        return data[index];
    }

    public boolean contains(int x) {
        for (int i = 0; i < size; i++) {
            metrics.addStep(); // чтение ячейки
            metrics.addComparison(); // сравнение элементов
            if (data[i] == x) {
                return true;
            }
        }
        return false;
    }

    public int size() {
        return size;
    }

    private void resize() {
        int newCapacity = data.length == 0 ? 10 : data.length * 2;
        metrics.addMoves(data.length); // копирование при ресайзе
        data = Arrays.copyOf(data, newCapacity);
    }
}
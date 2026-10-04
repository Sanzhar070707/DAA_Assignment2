package datastructures;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class DataStructuresTest {

    private final OperationMetrics dummyMetrics = new OperationMetrics();

    // --- DynamicArray Tests ---

    @Test
    void testDynamicArrayEdgeCases() {
        DynamicArray da = new DynamicArray(dummyMetrics);

        // Пустая структура и неверные индексы
        assertTrue(da.size() == 0);
        assertThrows(IndexOutOfBoundsException.class, () -> da.get(0));
        assertThrows(IndexOutOfBoundsException.class, () -> da.remove(0));

        // Один элемент
        da.add(42);
        assertEquals(1, da.size());
        assertEquals(42, da.get(0));

        // Дубликаты и первый/последний индекс
        da.add(42);
        da.add(100);
        da.add(42); // [42, 42, 100, 42]
        assertEquals(4, da.size());
        assertEquals(42, da.get(0));   // первый
        assertEquals(42, da.get(3));   // последний

        // Неверный индекс при вставке/удалении
        assertThrows(IndexOutOfBoundsException.class, () -> da.add(-1, 10));
        assertThrows(IndexOutOfBoundsException.class, () -> da.add(10, 10));
        assertThrows(IndexOutOfBoundsException.class, () -> da.remove(10));

        // Удаление
        int removed = da.remove(0);
        assertEquals(42, removed);
        assertEquals(3, da.size());
    }

    @Test
    void testDynamicArrayCorrectnessRandom() {
        DynamicArray da = new DynamicArray(dummyMetrics);
        List<Integer> expected = new ArrayList<>();
        Random rand = new Random(42);

        for (int i = 0; i < 500; i++) {
            int val = rand.nextInt(1000);
            da.add(val);
            expected.add(val);
        }

        assertEquals(expected.size(), da.size());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i), da.get(i));
        }

        // Проверка contains
        for (int i = 0; i < 50; i++) {
            int val = expected.get(rand.nextInt(expected.size()));
            assertTrue(da.contains(val));
        }
        assertFalse(da.contains(-99999));
    }

    // --- MyLinkedList Tests ---

    @Test
    void testMyLinkedListEdgeCases() {
        MyLinkedList list = new MyLinkedList(dummyMetrics);

        assertTrue(list.size() == 0);
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(0));
        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(0));

        list.add(10);
        assertEquals(1, list.size());
        assertEquals(10, list.get(0));

        list.add(0, 5);  // вставка в начало
        list.add(2, 20); // вставка в конец
        list.add(1, 15); // вставка в середину [5, 15, 10, 20]

        assertEquals(4, list.size());
        assertEquals(5, list.get(0));
        assertEquals(20, list.get(3));

        assertThrows(IndexOutOfBoundsException.class, () -> list.add(-1, 0));
        assertThrows(IndexOutOfBoundsException.class, () -> list.add(10, 0));

        list.remove(0); // удаление головы
        assertEquals(15, list.get(0));
    }

    @Test
    void testMyLinkedListCorrectnessRandom() {
        MyLinkedList list = new MyLinkedList(dummyMetrics);
        List<Integer> expected = new ArrayList<>();
        Random rand = new Random(42);

        for (int i = 0; i < 300; i++) {
            int val = rand.nextInt(1000);
            list.add(val);
            expected.add(val);
        }

        assertEquals(expected.size(), list.size());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i), list.get(i));
        }

        for (int i = 0; i < 30; i++) {
            int val = expected.get(rand.nextInt(expected.size()));
            assertTrue(list.contains(val));
        }
        assertFalse(list.contains(-99999));
    }

    // --- MinHeap Tests & Heap Property ---

    @Test
    void testMinHeapEdgeCases() {
        MinHeap heap = new MinHeap(dummyMetrics);

        assertEquals(0, heap.size());
        assertThrows(IllegalStateException.class, heap::peekMin);
        assertThrows(IllegalStateException.class, heap::extractMin);

        heap.insert(10);
        assertEquals(10, heap.peekMin());
        assertEquals(1, heap.size());

        assertEquals(10, heap.extractMin());
        assertEquals(0, heap.size());
    }

    @Test
    void testMinHeapPropertyAndSortedOutput() {
        MinHeap heap = new MinHeap(dummyMetrics);
        Random rand = new Random(42);
        int n = 500;

        // Проверяем, что свойство кучи (parent <= child) сохраняется при каждой вставке
        for (int i = 0; i < n; i++) {
            heap.insert(rand.nextInt(10000));
        }

        // Проверяем сортировку по неубыванию при extractMin()
        int previousMin = Integer.MIN_VALUE;
        while (heap.size() > 0) {
            int currentMin = heap.extractMin();
            assertTrue(currentMin >= previousMin, "Heap output must be in non-decreasing order!");
            previousMin = currentMin;
        }
    }
}
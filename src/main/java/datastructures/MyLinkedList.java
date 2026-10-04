package datastructures;

public class MyLinkedList {
    private static class Node {
        int data;
        Node next;
        Node prev;

        Node(int data) {
            this.data = data;
        }
    }

    private Node head;
    private Node tail;
    private int size;
    private final OperationMetrics metrics;

    public MyLinkedList(OperationMetrics metrics) {
        this.head = null;
        this.tail = null;
        this.size = 0;
        this.metrics = metrics;
    }

    public void add(int x) {
        Node newNode = new Node(x);
        metrics.addMove();
        if (tail == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
            metrics.addMoves(2); // обновление связей (pointer updates)
        }
        size++;
    }

    public void add(int index, int x) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        if (index == size) {
            add(x);
            return;
        }
        if (index == 0) {
            Node newNode = new Node(x);
            metrics.addMove();
            if (head == null) {
                head = newNode;
                tail = newNode;
            } else {
                newNode.next = head;
                head.prev = newNode;
                head = newNode;
                metrics.addMoves(2);
            }
            size++;
            return;
        }

        Node current = getNode(index);
        Node newNode = new Node(x);
        metrics.addMove();
        Node prevNode = current.prev;

        prevNode.next = newNode;
        newNode.prev = prevNode;
        newNode.next = current;
        current.prev = newNode;
        metrics.addMoves(4); // переброска указателей
        size++;
    }

    public int remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        Node target = getNode(index);

        if (target.prev != null) {
            target.prev.next = target.next;
            metrics.addMove();
        } else {
            head = target.next;
            metrics.addMove();
        }

        if (target.next != null) {
            target.next.prev = target.prev;
            metrics.addMove();
        } else {
            tail = target.prev;
            metrics.addMove();
        }

        size--;
        return target.data;
    }

    public int get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        return getNode(index).data;
    }

    public boolean contains(int x) {
        Node current = head;
        while (current != null) {
            metrics.addStep(); // переход к следующему узлу
            metrics.addComparison(); // сравнение данных
            if (current.data == x) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    public int size() {
        return size;
    }

    private Node getNode(int index) {
        Node current;
        if (index < size / 2) {
            current = head;
            for (int i = 0; i < index; i++) {
                current = current.next;
                metrics.addStep(); // шаг по списку вперед
            }
        } else {
            current = tail;
            for (int i = size - 1; i > index; i--) {
                current = current.prev;
                metrics.addStep(); // шаг по списку назад
            }
        }
        return current;
    }
}
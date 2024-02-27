package queue;

import java.util.Objects;
import java.util.function.Predicate;

public class ArrayQueue {
    private final int BASE_ARRAY_CAPACITY = 16;
    public Object[] elements = new Object[BASE_ARRAY_CAPACITY];

    private int head;

    private int tail;

    private int size;

    public int indexIf(Predicate<Object> predicate) {
        int index = head;
        for (int i = 0; i < size; i++) {
            if (predicate.test(elements[index])) {
                return (index - head + elements.length) % elements.length;
            }
            index = (index + 1) % elements.length;
        }
        return -1;
    }

    public int lastIndexIf(Predicate<Object> predicate) {
        int index = (tail - 1 + elements.length) % elements.length;
        for (int i = size - 1; i >= 0; i--) {
            if (predicate.test(elements[index])) {
                return (index - head + elements.length) % elements.length;
            }
            index = (index - 1 + elements.length) % elements.length;
        }
        return -1;
    }

    public void enqueue(Object element) {
        Objects.requireNonNull(element);
        Objects.requireNonNull(elements);
        ensureCapacity();
        elements[tail] = element;
        tail = (tail + 1) % elements.length;
        size++;
    }

    public int size() {
        Objects.requireNonNull(elements);
        return size;
    }

    public Object element() {
        Objects.requireNonNull(elements);
        assert size != 0;
        return elements[head];
    }

    public Object dequeue() {
        Objects.requireNonNull(elements);
        assert size != 0;
        Object deletedElement = elements[head];
        elements[head] = null;
        head = (head + 1) % elements.length;
        size--;
        return deletedElement;
    }

    public boolean isEmpty() {
        Objects.requireNonNull(elements);
        return size == 0;
    }

    public void clear() {
        Objects.requireNonNull(elements);
        elements = new Object[BASE_ARRAY_CAPACITY];
        head = 0;
        tail = 0;
        size = 0;
    }

    private void ensureCapacity() {
        Objects.requireNonNull(elements);
        if (size == elements.length) {
            increaseQueueCapacity();
        }
    }

    private void increaseQueueCapacity() {
        Objects.requireNonNull(elements);
        Object[] resizedElements = new Object[elements.length * 3 / 2 + 1];
        if (tail > head) {
            System.arraycopy(elements, head, resizedElements, 0, size);
        } else {
            System.arraycopy(elements, head, resizedElements, 0, elements.length - head);
            System.arraycopy(elements, 0, resizedElements, elements.length - head, tail);
        }
        elements = resizedElements;
        head = 0;
        tail = size;
    }
}

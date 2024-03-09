package queue;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Predicate;

public class ArrayQueueModule {

    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    public static Object[] elements = new Object[DEFAULT_INITIAL_CAPACITY];

    private static int head;

    private static int size;

    public static int indexIf(Predicate<Object> predicate) {
        int index = head;
        for (int i = 0; i < size; i++) {
            if (predicate.test(elements[index])) {
                return (index - head + elements.length) % elements.length;
            }
            index = (index + 1) % elements.length;
        }
        return -1;
    }

    public static int lastIndexIf(Predicate<Object> predicate) {
        int index = (head + size - 1) % elements.length;
        for (int i = size - 1; i >= 0; i--) {
            if (predicate.test(elements[index])) {
                return i;
            }
            index = (index - 1 + elements.length) % elements.length;
        }
        return -1;
    }


    public static void enqueue(Object element) {
        Objects.requireNonNull(element);
        ensureCapacity();
        elements[(head + size) % elements.length] = element;
        size++;
    }

    public static Object element() {
        Objects.requireNonNull(elements);
        assert size != 0;
        return elements[head];
    }

    public static Object dequeue() {
        Objects.requireNonNull(elements);
        assert size != 0;
        Object deletedElement = elements[head];
        elements[head] = null;
        head = (head + 1) % elements.length;
        size--;
        return deletedElement;
    }

    public static int size() {
        Objects.requireNonNull(elements);
        return size;
    }

    public static boolean isEmpty() {
        Objects.requireNonNull(elements);
        return size == 0;
    }

    public static void clear() {
        Objects.requireNonNull(elements);
        Arrays.fill(elements, null);
        head = 0;
        size = 0;
    }

    private static void ensureCapacity() {
        Objects.requireNonNull(elements);
        if (size == elements.length) {
            increaseQueueCapacity();
        }
    }

    private static void increaseQueueCapacity() {
        Object[] resizedElements = new Object[elements.length * 3 / 2 + 1];
        int firstPartLength = Math.min(size, elements.length - head);
        System.arraycopy(elements, head, resizedElements, 0, firstPartLength);
        System.arraycopy(elements, 0, resizedElements, firstPartLength, size - firstPartLength);
        elements = resizedElements;
        head = 0;
    }
}
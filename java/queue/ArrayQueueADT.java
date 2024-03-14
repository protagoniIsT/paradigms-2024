package queue;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Predicate;

public class ArrayQueueADT {

    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    public Object[] elements = new Object[DEFAULT_INITIAL_CAPACITY];

    private int head;

    private int size;

    public static int indexIf(final ArrayQueueADT queue, Predicate<Object> predicate) {
        int index = queue.head;
        for (int i = 0; i < queue.size; i++) {
            if (predicate.test(queue.elements[index])) {
                return (index - queue.head + queue.elements.length) % queue.elements.length;
            }
            index = (index + 1) % queue.elements.length;
        }
        return -1;
    }

    public static int lastIndexIf(final ArrayQueueADT queue, Predicate<Object> predicate) {
        int index = (queue.head + queue.size - 1) % queue.elements.length;
        for (int i = queue.size - 1; i >= 0; i--) {
            if (predicate.test(queue.elements[index])) {
                return i;
            }
            index = (index - 1 + queue.elements.length) % queue.elements.length;
        }
        return -1;
    }

    public static void enqueue(final ArrayQueueADT queue, Object element) {
        Objects.requireNonNull(element);
        Objects.requireNonNull(queue);
        ensureCapacity(queue);
        queue.elements[(queue.head + queue.size) % queue.elements.length] = element;
        queue.size++;
    }

    public static Object element(final ArrayQueueADT queue) {
        Objects.requireNonNull(queue);
        assert queue.size != 0;
        return queue.elements[queue.head];
    }

    public static Object dequeue(final ArrayQueueADT queue) {
        Objects.requireNonNull(queue);
        assert queue.size != 0;
        Object deletedElement = queue.elements[queue.head];
        queue.elements[queue.head] = null;
        queue.head = (queue.head + 1) % queue.elements.length;
        queue.size--;
        return deletedElement;
    }

    public static int size(final ArrayQueueADT queue) {
        Objects.requireNonNull(queue);
        return queue.size;
    }

    public static boolean isEmpty(final ArrayQueueADT queue) {
        Objects.requireNonNull(queue);
        return queue.size == 0;
    }

    public static void clear(final ArrayQueueADT queue) {
        Objects.requireNonNull(queue.elements);
        Arrays.fill(queue.elements, 0, queue.size,  null);
        queue.head = 0;
        queue.size = 0;
    }

    private static void ensureCapacity(final ArrayQueueADT queue) {
        Objects.requireNonNull(queue);
        if (queue.size == queue.elements.length) {
            increaseQueueCapacity(queue);
        }
    }

    private static void increaseQueueCapacity(final ArrayQueueADT queue) {
        Object[] resizedElements = new Object[queue.elements.length * 3 / 2 + 1];
        int firstPartLength = Math.min(queue.size, queue.elements.length - queue.head);
        System.arraycopy(queue.elements, queue.head, resizedElements, 0, firstPartLength);
        System.arraycopy(queue.elements, 0, resizedElements, firstPartLength, queue.size - firstPartLength);
        queue.elements = resizedElements;
        queue.head = 0;
    }
}

package queue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.function.Predicate;
/*
    Model: a[0]...a[n - 1]
    Inv: for i = 0..(actualQueueSize - 1): a[i] != null
    Let immutable(n): for i = 0..(n - 1): a'[i] == a[i]
*/
public class ArrayQueue extends AbstractQueue {
    private final int DEFAULT_INITIAL_CAPACITY = 16;
    public Object[] elements = new Object[DEFAULT_INITIAL_CAPACITY];

    private int head;

    private int tail;

    private int size;

    /*
     Pred: size' > 0
     Let forall i = 0..(size - 1) : A = {i | predicate(elements[i]) == true}
     Post: R = index && index == min(A)
    */
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

    /*
     Pred: size' > 0
     Let forall i = (size - 1)..0 : A = {i | predicate(elements[i]) == true}
     Post: R = index && index == min(A)
    */
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

    /*
         Pre: element != null
         Post: size' = size + 1 &&
               tail' = (tail + 1) % elements.length &&
               elements'[n'] = element &&
               immutable(size)
    */
    public void enqueue(Object element) {
        Objects.requireNonNull(element);
        Objects.requireNonNull(elements);
        ensureCapacity();
        elements[tail] = element;
        tail = (tail + 1) % elements.length;
        size++;
    }


    /*
     Pre: n > 0
     Post: R = elements[head] && head' = head
    */
    public Object element() {
        Objects.requireNonNull(elements);
        assert size != 0;
        return elements[head];
    }

    /*
     Pre: size' > 0
     Post: R = elements[head] && head' = (head' + 1) % elements.length && immutable(size)
    */
    public Object dequeue() {
        Objects.requireNonNull(elements);
        assert size != 0;
        Object deletedElement = elements[head];
        elements[head] = null;
        head = (head + 1) % elements.length;
        size--;
        return deletedElement;
    }

    /*
     Pre: true
     Post: R = size && size' = size && immutable(size)
    */
    public int size() {
        Objects.requireNonNull(elements);
        return size;
    }


    /*
     Pre: true
     Post: R = (size = 0) && size' = size && immutable(size)
    */
    public boolean isEmpty() {
        Objects.requireNonNull(elements);
        return size == 0;
    }

    @Override
    public void distinct() {
        HashSet<Object> set = new HashSet<>();
        Object[] newElements = new Object[elements.length];
        int newTail = 0;
        for (int i = 0; i < size; i++) {
            int index = (head + i) % elements.length;
            if (set.add(elements[index])) {
                newElements[newTail++] = elements[index];
            }
        }
        elements = newElements;
        head = 0;
        tail = newTail;
        size = set.size();
    }


    /*
     Pred: elements != null
     Post: size' == 0 && head' == 0 && tail' == 0
    */
    public void clear() {
        Objects.requireNonNull(elements);
        elements = new Object[DEFAULT_INITIAL_CAPACITY];
        Arrays.fill(elements, null);
        head = 0;
        tail = 0;
        size = 0;
    }

    @Override
    protected void enqueueLinked(Object element) {
        enqueue(element);
    }

    @Override
    protected Object dequeueLinked() {
        return dequeue();
    }

    @Override
    protected Object elementLinked() {
        return element();
    }

    @Override
    protected void clearLinked() {
        clear();
    }

    @Override
    protected void distinctLinked() {
        distinct();
    }

    /*
     Pre: true
     Post: size' = size && immutable(size)
    */
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

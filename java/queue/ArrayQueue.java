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
        int index = (head + size - 1) % elements.length;
        for (int i = size - 1; i >= 0; i--) {
            if (predicate.test(elements[index])) {
                return i;
            }
            index = (index - 1 + elements.length) % elements.length;
        }
        return -1;
    }

    /*
         Pre: element != null
         Post: size' = size + 1 &&
               tail' = (tail + 1) % elements.length &&
               elements'[n'] = element
    */
    public void enqueue(Object element) {
        Objects.requireNonNull(element);
        ensureCapacity();
        elements[(head + size) % elements.length] = element;
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
     Post: R = elements[head] && head' = (head' + 1) % elements.length
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
        int newTail = head;
        for (int i = 0; i < size; i++) {
            int index = (head + i) % elements.length;
            if (set.add(elements[index])) {
                elements[newTail] = elements[index];
                newTail = (newTail + 1) % elements.length;
            }
        }
        for (int i = set.size(); i < size; i++) {
            elements[(head + i) % elements.length] = null;
        }
        size = set.size();
    }

    /*
     Pred: elements != null
     Post: size' == tail' == 0 && head' == 0
    */
    public void clear() {
        Objects.requireNonNull(elements);
        Arrays.fill(elements, 0, size,  null);
        head = 0;
        size = 0;
    }

    @Override
    protected void enqueueImpl(Object element) {
        enqueue(element);
    }

    @Override
    protected Object dequeueImpl() {
        return dequeue();
    }

    @Override
    protected Object elementImpl() {
        return element();
    }

    @Override
    protected void clearImpl() {
        clear();
    }

    @Override
    protected void distinctImpl() {
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
        Object[] resizedElements = new Object[elements.length * 3 / 2 + 1];
        int firstPartLength = Math.min(size, elements.length - head);
        System.arraycopy(elements, head, resizedElements, 0, firstPartLength);
        System.arraycopy(elements, 0, resizedElements, firstPartLength, size - firstPartLength);
        elements = resizedElements;
        head = 0;
    }
}

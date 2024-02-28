package queue;

import java.util.Objects;

public abstract class AbstractQueue implements Queue {
    protected int size;

    protected abstract void enqueueLinkedQueue(Object element);

    protected abstract Object dequeueLinkedQueue();

    protected abstract Object elementLinkedQueue();

    protected abstract void clearLinkedQueue();

    public void enqueue(Object element) {
        Objects.requireNonNull(element);
        enqueueLinkedQueue(element);
        size++;
    }

    public Object dequeue() {
        assert size != 0;
        size--;
        return dequeueLinkedQueue();
    }

    public Object element() {
        assert size != 0;
        return elementLinkedQueue();
    }

    public void clear() {
        clearLinkedQueue();
        size = 0;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }
}

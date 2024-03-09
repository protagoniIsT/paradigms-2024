package queue;

import java.util.Objects;

public abstract class AbstractQueue implements Queue {

    protected int size;

    protected abstract void enqueueImpl(Object element);

    protected abstract Object dequeueImpl();

    protected abstract Object elementImpl();

    protected abstract void clearImpl();

    protected abstract void distinctImpl();

    public void enqueue(Object element) {
        Objects.requireNonNull(element);
        enqueueImpl(element);
        size++;
    }

    public Object dequeue() {
        assert size != 0;
        size--;
        return dequeueImpl();
    }

    public Object element() {
        assert size != 0;
        return elementImpl();
    }

    public void distinct() {
        distinctImpl();
    }

    public void clear() {
        clearImpl();
        size = 0;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }
}

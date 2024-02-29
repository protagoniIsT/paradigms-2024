package queue;

import java.util.Objects;

public abstract class AbstractQueue implements Queue {
    protected int size;

    protected abstract void enqueueLinked(Object element);

    protected abstract Object dequeueLinked();

    protected abstract Object elementLinked();

    protected abstract void clearLinked();

    protected abstract void distinctLinked();

    public void enqueue(Object element) {
        Objects.requireNonNull(element);
        enqueueLinked(element);
        size++;
    }

    public Object dequeue() {
        assert size != 0;
        size--;
        return dequeueLinked();
    }

    public Object element() {
        assert size != 0;
        return elementLinked();
    }

    public void distinct() {
        distinctLinked();
    }

    public void clear() {
        clearLinked();
        size = 0;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }
}

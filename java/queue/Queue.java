package queue;

public interface Queue {

    /*
         Pre: element != null
         Post: size' = size + 1 &&
               tail' = (tail + 1) % elements.length &&
               elements'[n'] = element &&
               immutable(size)
    */
    void enqueue(Object element);

    /*
       Pre: size' > 0
       Post: R = elements[head] && head' = (head' + 1) % elements.length && immutable(size)
    */
    Object dequeue();

    /*
       Pre: n > 0
       Post: R = elements[head] && head' = head
    */
    Object element();

    /*
       Pred: elements != null
       Post: size' == 0 && head' == 0 && tail' == 0
    */
    void clear();

    /*
       Pre: true
       Post: R = size && size' = size && immutable(size)
    */
    int size();

    /*
       Pre: true
       Post: R = (size = 0) && size' = size && immutable(size)
    */
    boolean isEmpty();
}

package queue;

public class LinkedQueue extends AbstractQueue {
    private Node tail;

    private Node head;

    public LinkedQueue() {
        clearLinkedQueue();
    }

    public void enqueueLinkedQueue(Object element) {
        tail.value = element;
        Node lastTail = tail;
        tail = new Node(null, null);
        lastTail.nextNode = tail;
    }

    public Object elementLinkedQueue() {
        return head.nextNode.value;
    }

    public Object dequeueLinkedQueue() {
        Object element = head.nextNode.value;
        head = head.nextNode;
        head.value = null;
        return element;
    }

    public void clearLinkedQueue() {
        tail = new Node(null, null);
        head = new Node(null, tail);
    }

    private static class Node {
        private Object value;
        private Node nextNode;

        public Node(Object value, Node nextNode) {
            this.value = value;
            this.nextNode = nextNode;
        }
    }
}

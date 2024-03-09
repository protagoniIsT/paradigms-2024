package queue;

import java.util.HashSet;

public class LinkedQueue extends AbstractQueue {

    private Node head;

    private Node tail;

    public LinkedQueue() {
        clearImpl();
    }

    public void enqueueImpl(Object element) {
        tail.value = element;
        Node lastTail = tail;
        tail = new Node(null, null);
        lastTail.nextNode = tail;
    }

    public Object elementImpl() {
        return head.nextNode.value;
    }

    public Object dequeueImpl() {
        Object element = head.nextNode.value;
        assert element != null;
        head = head.nextNode;
        head.value = null;
        return element;
    }

    public void clearImpl() {
        tail = new Node(null, null);
        head = new Node(null, tail);
    }

    public void distinctImpl() {
        HashSet<Object> set = new HashSet<>();
        Node currNode = head.nextNode;
        Node prevNode = head;
        while (currNode != null && currNode.value != null) {
            if (!set.add(currNode.value)) {
                prevNode.nextNode = currNode.nextNode;
                if (currNode.nextNode == null) {
                    tail = prevNode;
                }
            } else {
                prevNode = currNode;
            }
            currNode = currNode.nextNode;
        }
        size = set.size();
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

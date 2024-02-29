package queue;

import java.util.HashSet;
import java.util.Objects;

public class LinkedQueue extends AbstractQueue {

    private Node head;

    private Node tail;

    public LinkedQueue() {
        clearLinked();
    }

    public void enqueueLinked(Object element) {
        tail.value = element;
        Node lastTail = tail;
        tail = new Node(null, null);
        lastTail.nextNode = tail;
    }

    public Object elementLinked() {
        return head.nextNode.value;
    }

    public Object dequeueLinked() {
        Object element = head.nextNode.value;
        assert element != null;
        head = head.nextNode;
        head.value = null;
        return element;
    }

    public void clearLinked() {
        tail = new Node(null, null);
        head = new Node(null, tail);
    }

    public void distinctLinked() {
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

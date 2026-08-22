package doubly_linked_list;

import java.util.List;
import java.util.Deque;
import java.util.ArrayDeque;

class DesignBrowserHistory {

    private Deque<String> mainStack;
    private Deque<String> secondaryStack;

    public DesignBrowserHistory(String homepage) {
        mainStack = new ArrayDeque<>(List.of(homepage));
        secondaryStack = new ArrayDeque<>();
    }

    public void visit(String url) {
        mainStack.push(url);
        secondaryStack.clear();
    }

    public String back(int steps) {
        int mainStackSize = mainStack.size();
        int actualSteps = (steps < mainStackSize) ? steps : (mainStackSize - 1);

        while (actualSteps > 0) {
            secondaryStack.push(mainStack.pop());
            actualSteps--;
        }

        return mainStack.peek();
    }

    public String forward(int steps) {
        int secondaryStackSize = secondaryStack.size();
        int actualSteps = (steps <= secondaryStackSize) ? steps : secondaryStackSize;

        while (actualSteps > 0) {
            mainStack.push(secondaryStack.pop());
            actualSteps--;
        }

        return mainStack.peek();
    }
}

class BrowserHistory {

    private static class Node {
        private String value;
        private Node prev;
        private Node next;

        private Node(String value) {
            this.value = value;
        }
    }

    private final Node head;
    private final Node tail;
    private Node currentNode;

    public BrowserHistory(String homepage) {
        head = new Node(null);
        tail = new Node(null);

        currentNode = head;

        head.next = tail;
        tail.prev = head;
        insertNode(new Node(homepage));
    }

    public void visit(String url) {
        insertNode(new Node(url));
        currentNode.next = tail;
        tail.prev = currentNode;
    }

    public String back(int steps) {
        int counter = steps;
        while (counter > 0 && currentNode.prev != head) {
            currentNode = currentNode.prev;
            counter--;
        }
        return currentNode.value;
    }

    public String forward(int steps) {
        int counter = steps;
        while (counter > 0 && currentNode.next != tail) {
            currentNode = currentNode.next;
            counter--;
        }
        return currentNode.value;
    }

    private void insertNode(Node node) {
        currentNode.next.prev = node;
        node.next = currentNode.next;
        node.prev = currentNode;
        currentNode.next = node;
        currentNode = node;
    }
}

/**
 * Your BrowserHistory object will be instantiated and called as such:
 * BrowserHistory obj = new BrowserHistory(homepage);
 * obj.visit(url);
 * String param_2 = obj.back(steps);
 * String param_3 = obj.forward(steps);
 */
package doubly_linked_list;

public class FlattenMultilevelDoublyLinkedList {

    private static class Node {
        public int val;
        public Node prev;
        public Node next;
        public Node child;

        Node(int val) {
            this.val = val;
        }
    };

    private Node flatten(Node head) {

        if (head != null)
            visit(head);

        return head;
    }

    private Node visit(Node node) {

        Node child = node.child;
        Node next = node.next;
        Node tail = next;

        if (child != null) {
            tail = visit(child);

            node.next = child;
            child.prev = node;
            node.child = null;

            if (next != null) {
                tail.next = next;
                next.prev = tail;
            }
        }

        if (next != null)
            return visit(next);

        if (child == null)
            return node;

        return tail;
    }

    public static void main(String[] args) {

        Node n1 = new Node(1);
        Node n2 = new Node(2);
        Node n3 = new Node(3);
        Node n4 = new Node(4);
        Node n5 = new Node(5);
        Node n6 = new Node(6);
        Node n7 = new Node(7);
        Node n8 = new Node(8);
        Node n9 = new Node(9);
        Node n10 = new Node(10);
        Node n11 = new Node(11);
        Node n12 = new Node(12);

        n1.next = n2;

        n2.prev = n1;
        n2.next = n3;

        n3.prev = n2;
        n3.next = n4;
        n3.child = n7;

        n4.prev = n3;
        n4.next = n5;

        n5.prev = n4;
        n5.next = n6;

        n6.prev = n5;

        n7.next = n8;

        n8.prev = n7;
        n8.next = n9;
        n8.child = n11;

        n9.prev = n8;
        n9.next = n10;

        n10.prev = n9;

        n11.next = n12;

        n12.prev = n11;

        FlattenMultilevelDoublyLinkedList solution = new FlattenMultilevelDoublyLinkedList();
        Node n = solution.flatten(n1);

        while (n != null) {
            System.out.print(n.val + " ");
            n = n.next;
        }
    }
}

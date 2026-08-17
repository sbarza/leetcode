package doubly_linked_list;

import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

class MinimumPairRemovalToSortArray1 {

    public int minimumPairRemoval(int[] nums) {
        int cntOperations = 0;
        int[] auxArray = Arrays.copyOf(nums, nums.length);

        while (true) {

            int index = findIndex(auxArray);

            if (index == -1) {
                break;
            }

            auxArray = createResultArray(auxArray, index);
            cntOperations++;
        }

        return cntOperations;

    }

    private int findIndex(int[] numbers) {
        int index = -1;
        boolean isSorted = true;

        int minumumPairSum = Integer.MAX_VALUE;

        for (int i = 0; i < numbers.length - 1; i++) {
            int pairSum = numbers[i] + numbers[i + 1];

            if (pairSum < minumumPairSum) {
                minumumPairSum = pairSum;
                index = i;
            }

            if (isSorted && numbers[i] > numbers[i + 1]) {
                isSorted = false;
            }
        }

        if (isSorted) {
            return -1;
        }

        return index;
    }

    private int[] createResultArray(int[] numbers, int index) {
        int[] result = new int[numbers.length - 1];

        System.arraycopy(numbers, 0, result, 0, index);
        result[index] = numbers[index] + numbers[index + 1];
        System.arraycopy(numbers, index + 2, result, index + 1, numbers.length - index - 2);

        return result;
    }

    public static void main(String[] args) {
        int[] nums = new int[] {5, 2, 3, 1};
        MinimumPairRemovalToSortArray1 mp = new MinimumPairRemovalToSortArray1();
        int ops = mp.minimumPairRemoval(nums);
        System.out.println(ops);
    }
}

class Solution {

    private static class Node {
        private int value;
        private int index;
        private Node prev;
        private Node next;

        private Node(int value) {
            this.value = value;
        }
    }

    private static class Pair {
        private int sum;
        private Node left;
        private Node right;

        private Pair(int sum) {
            this(sum, null, null);
        }

        private Pair(int sum, Node left, Node right) {
            this.sum = sum;
            this.left = left;
            this.right = right;
        }

        private int getSum() {
            return sum;
        }

        private int getLeftIndex() {
            return left.index;
        }
    }

    private final Node head;
    private final Node tail;
    private final Queue<Pair> priorityQueue;

    public Solution() {
        Comparator<Pair> c = Comparator.comparing(Pair::getSum)
                .thenComparing(Pair::getLeftIndex);

        priorityQueue = new PriorityQueue<>(c);
        head = new Node(0);
        tail = new Node(0);
        head.next = tail;
        tail.prev = head;
    }

    public int minimumPairRemoval(int[] nums) {
        int cntOperations = 0;
        int cntInversions = populateDataStructures(nums);

        while (cntInversions > 0) {
            Pair priorityPair = priorityQueue.poll();
            Node left = priorityPair.left;
            Node prev = left.prev;
            Node right = priorityPair.right;
            Node next = right.next;
            int sum = left.value + right.value;

            if (left.next != right || right.prev != left || sum != priorityPair.sum) {
                continue;
            }

            if (prev != head && prev.value > left.value) {
                cntInversions--;
            }

            if (next != tail && right.value > next.value) {
                cntInversions--;
            }

            if (left.value > right.value) {
                cntInversions--;
            }

            fusionNodes(priorityPair);

            if (prev != head && prev.value > sum) {
                cntInversions++;
            }

            if (next != tail && sum > next.value) {
                cntInversions++;
            }

            if (prev != head) {
                priorityQueue.offer(new Pair(sum + prev.value, prev, left));
            }

            if (next != tail) {
                priorityQueue.offer(new Pair(sum + next.value, left, next));
            }

            cntOperations++;
        }

        return cntOperations;
    }

    private int populateDataStructures(int[] nums) {
        int cntInversions = 0;
        Node firstNode = new Node(nums[0]);
        firstNode.index = 0;
        insertNode(head, firstNode);
        Node curNode = firstNode;

        for (int i = 1; i < nums.length; i++) {
            Node newNode = new Node(nums[i]);
            newNode.index = i;
            insertNode(curNode, newNode);

            Pair pair = new Pair(nums[i - 1] + nums[i]);
            pair.left = curNode;
            pair.right = newNode;
            priorityQueue.offer(pair);

            if (nums[i - 1] > nums[i]) {
                cntInversions++;
            }

            curNode = newNode;
        }

        return cntInversions;
    }

    private void insertNode(Node refNode, Node newNode) {
        refNode.next.prev = newNode;
        newNode.prev = refNode;
        newNode.next = refNode.next;
        refNode.next = newNode;
    }

    private void fusionNodes(Pair pair) {
        Node left = pair.left;
        Node right = pair.right;
        left.next = right.next;
        right.next.prev = left;
        left.value = left.value + right.value;
    }

    public static void main(String[] args) {
        Solution s = new Solution();
        int inversionsCnt = s.minimumPairRemoval(new int[]{5, 2, 3, 1});
        System.out.println(inversionsCnt);
    }

}

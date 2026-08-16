package doublylinkedlist;

/*

    Design a data structure that follows the constraints of a Least Recently Used (LRU) cache.

    Implement the LRUCache class:

    LRUCache(int capacity) Initialize the LRU cache with positive size capacity.
    int get(int key) Return the value of the key if the key exists, otherwise return -1.
    void put(int key, int value) Update the value of the key if the key exists. Otherwise, add the key-value pair to the cache. If the number of keys exceeds the capacity from this operation, evict the least recently used key.
    The functions get and put must each run in O(1) average time complexity.



    Example 1:

    Input
    ["LRUCache", "put", "put", "get", "put", "get", "put", "get", "get", "get"]
    [[2], [1, 1], [2, 2], [1], [3, 3], [2], [4, 4], [1], [3], [4]]
    Output
    [null, null, null, 1, null, -1, null, -1, 3, 4]

    Explanation
    LRUCache lRUCache = new LRUCache(2);
    lRUCache.put(1, 1); // cache is {1=1}
    lRUCache.put(2, 2); // cache is {1=1, 2=2}
    lRUCache.get(1);    // return 1
    lRUCache.put(3, 3); // LRU key was 2, evicts key 2, cache is {1=1, 3=3}
    lRUCache.get(2);    // returns -1 (not found)
    lRUCache.put(4, 4); // LRU key was 1, evicts key 1, cache is {4=4, 3=3}
    lRUCache.get(1);    // return -1 (not found)
    lRUCache.get(3);    // return 3
    lRUCache.get(4);    // return 4


    Constraints:

    1 <= capacity <= 3000
    0 <= key <= 10^4
    0 <= value <= 10^5
    At most 2 * 105 calls will be made to get and put.

class LRUCache {

    public LRUCache(int capacity) {

    }

    public int get(int key) {

    }

    public void put(int key, int value) {

    }
}

 * Your LRUCache object will be instantiated and called as such:
 * LRUCache obj = new LRUCache(capacity);
 * int param_1 = obj.get(key);
 * obj.put(key,value);
 *

*/

// Solution 1: By using LinkedHashMap as LRU Cache extending the LinkedHashMap

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;

class LRUCache1 extends LinkedHashMap<Integer, Integer> {

    private final int capacity;

    public LRUCache1(int capacity) {
        super(capacity, 0.75f, true);
        this.capacity = capacity;
    }

    public int get(int key) {
        return getOrDefault(key, -1);
    }

    public void put(int key, int value) {
        super.put(key, value);
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<Integer, Integer> entry) {
        return size() > capacity;
    }
}

// Solution 2: By using LinkedHashMap as LRU Cache without extending the LinkedHashMap

class LRUCache2 {

    private final LinkedHashMap<Integer, Integer> cache;

    public LRUCache2(int capacity) {
        cache = new LinkedHashMap<Integer, Integer>(capacity, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<Integer, Integer> entry) {
                return size() > capacity;
            }
        };
    }

    public int get(int key) {
        return cache.getOrDefault(key, -1);
    }

    public void put(int key, int value) {
        cache.put(key, value);
    }
}

// Solution 3: By creating a Doubly-Linked List from scratch
class LRUCache {

    private static class Node {
        private int key;
        private int value;
        private Node next;
        private Node prev;

        private Node(int key, int value) {
            this.key = key;
            this.value = value;
            next = null;
            prev = null;
        }
    }

    private final Node head;
    private final Node tail;
    private final int capacity;
    private Node[] nodes;
    private int size;

    public LRUCache(int capacity) {
        nodes = new Node[10000];
        head = new Node(0, 0);
        tail = new Node(0, 0);
        head.next = tail;
        tail.prev = head;
        this.capacity = capacity;
        size = 0;
    }

    public int get(int key) {
        Node node = nodes[key];

        if (node == null) {
            return -1;
        }

        moveNodeToEnd(node);
        return node.value;
    }

    public void put(int key, int value) {
        Node node = nodes[key];

        if (node == null) {
            node = new Node(key, value);
            nodes[key] = node;
            size++;
        } else {
            node.value = value;
        }

        moveNodeToEnd(node);

        if (size > capacity) {
            removeFirstNode();
        }
    }

    private void removeFirstNode() {
        Node nodeToBeRemoved = head.next;
        head.next = nodeToBeRemoved.next;
        nodeToBeRemoved.next.prev = head;
        nodeToBeRemoved.next = null;
        nodeToBeRemoved.prev = null;
        nodes[nodeToBeRemoved.key] = null;
        size--;
    }

    private void moveNodeToEnd(Node node) {
        if (node.prev != null && node.next != null) {
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }
        tail.prev.next = node;
        node.next = tail;
        node.prev = tail.prev;
        tail.prev = node;
    }

    public static void main(String[] args) {
        LRUCache cache = new LRUCache(2);
        cache.put(2, 1);
        cache.put(1, 1);
        cache.put(2, 3);
        cache.put(4, 1);
        int value = cache.get(1);
        System.out.println(value);
        value = cache.get(2);
        System.out.println(value);
    }
}

package doubly_linked_list;

/*
import java.util.*;

public class LFUCache {

    private final Map<Integer, Integer> cache;
    private final Map<Integer, Integer> frequencyMap;
    private final NavigableMap<Integer, LinkedHashSet<Integer>> orderedFrequencies;
    private final int capacity;
    private int size;

    public LFUCache(int capacity) {
        cache = new HashMap<>(capacity);
        frequencyMap = new HashMap<>(capacity);
        orderedFrequencies = new TreeMap<>();
        this.capacity = capacity;
        size = 0;
    }

    public int get(int key) {
        Integer value = cache.get(key);
        if (value == null) {
            return -1;
        }

        updateFrequency(key);
        return value;
    }

    public void put(int key, int value) {
        final boolean hasKey = cache.containsKey(key);
        boolean hasLimitReached = false;
        if (!hasKey) {
            if (size < capacity) {
                size++;
            } else {
                hasLimitReached = true;
            }
        }

        if (hasLimitReached) {
            // From the orderedFrequencies, retrieve the set of keys associated to the least frequency.
            // As orderedFrequencies is a NavigableMap, the keys are stored in natural order.
            // So, the least key is the first key. The set associated to this least frequency
            // is also ordered, but ordered by insertion, as it is a LinkedHashSet.
            // So, we need to update this set by removing the first key, as it was the first
            // entry among other keys in this set. After removing it, if this set is empty, we remove
            // this association in the orderedMap.
            Integer leastFrequency = orderedFrequencies.firstKey();
            LinkedHashSet<Integer> keySet = orderedFrequencies.get(leastFrequency);
            Integer leastFrequencyKey = keySet.removeFirst();

            if (keySet.isEmpty()) {
                orderedFrequencies.remove(leastFrequency);
            }

            // Remove the mapping from frequencyMap whose key is the least frequency
            // key retrieved from the orderedFrequencies
            frequencyMap.remove(leastFrequencyKey);

            // Remove the mapping from cache whose key is the least frequency
            // key retrieved from the orderedFrequencies
            cache.remove(leastFrequencyKey);
        }

        updateFrequency(key);
        cache.put(key, value);
    }

    private void updateFrequency(Integer key) {
        int frequency = frequencyMap.getOrDefault(key, 0);
        int updatedFrequency = frequency + 1;

        // Update frequencyMap adding 1 to the current frequency associated to the given key
        frequencyMap.merge(key, 1, Integer::sum);

        // Remove the key associated to the list of keys (ordered by insertion)
        // mapped to the previous frequency
        orderedFrequencies.computeIfPresent(frequency, (fr, keySet) -> {
            keySet.remove(key);
            if (keySet.isEmpty()) {
                return null;
            }
            return keySet;
        });

        // Update the orderedFrequencies by creating a new entry, if there is no mapping
        // associated to the updatedFrequency or by adding the key to the list of keys
        // (ordered by insertion) associated to the updatedFrequency
        orderedFrequencies.compute(updatedFrequency, (fr, keySet) -> {
            if (keySet == null) {
                keySet = new LinkedHashSet<>(List.of(key));
            } else {
                keySet.addLast(key);
            }
            return keySet;
        });
    }


    public static void main(String[] args) {

//        LFUCache cache = new LFUCache(2);
//        cache.put(1, 1);
//        cache.put(2, 2);
//        int value = cache.get(1);
//        System.out.println(value);
//        cache.put(3, 3);
//        value = cache.get(2);
//        System.out.println(value);
//        value = cache.get(3);
//        System.out.println(value);
//        cache.put(4, 4);
//        value = cache.get(1);
//        System.out.println(value);
//        value = cache.get(3);
//        System.out.println(value);
//        value = cache.get(4);
//        System.out.println(value);

        LFUCache cache = new LFUCache(2);
        cache.put(2, 1);
        cache.put(2, 2);
        int value = cache.get(2);
        System.out.println(value);
        cache.put(1, 1);
        cache.put(4, 1);
        value = cache.get(2);
        System.out.println(value);

    }

}
*/

import java.util.*;

public class LFUCache {

    private final Map<Integer, Integer> cache;
    private final Map<Integer, Integer> frequencyMap;
    private final Map<Integer, LinkedHashSet<Integer>> orderedFrequencies;
    private final int capacity;
    private int leastFrequency;

    public LFUCache(int capacity) {
        cache = new HashMap<>();
        frequencyMap = new HashMap<>();
        orderedFrequencies = new HashMap<>();
        this.capacity = capacity;
        leastFrequency = 0;
    }

    public int get(int key) {
        Integer value = cache.get(key);
        if (value == null) {
            return -1;
        }

        updateFrequency(key);
        return value;
    }

    public void put(int key, int value) {
        final boolean hasKey = cache.containsKey(key);
        cache.put(key, value);
        boolean hasLimitReached = false;
        if (!hasKey) {
            if (cache.size() <= capacity) {
                leastFrequency = 1;
            } else {
                hasLimitReached = true;
            }
        }

        if (hasLimitReached) {
            // Get the keys in the least-frequency bucket, ordered by insertion.
            LinkedHashSet<Integer> keySet = orderedFrequencies.get(leastFrequency);
            Integer leastFrequencyKey = keySet.removeFirst();

            if (keySet.isEmpty()) {
                orderedFrequencies.remove(leastFrequency);
            }

            frequencyMap.remove(leastFrequencyKey);
            cache.remove(leastFrequencyKey);
        }

        updateFrequency(key);
    }

    private void updateFrequency(Integer key) {
        int frequency = frequencyMap.getOrDefault(key, 0);
        int updatedFrequency = frequency + 1;

        frequencyMap.put(key, updatedFrequency);

        // Remove the key from its previous frequency bucket.
        orderedFrequencies.computeIfPresent(frequency, (fr, keySet) -> {
            keySet.remove(key);
            return keySet.isEmpty() ? null : keySet;
        });

        // If the minimum-frequency bucket became empty,
        // the minimum frequency increases by one.
        if (frequency == leastFrequency && !orderedFrequencies.containsKey(frequency)) {
            leastFrequency++;
        }

        // Add the key to its new frequency bucket.
        orderedFrequencies.computeIfAbsent(updatedFrequency, fr -> new LinkedHashSet<>())
                .addLast(key);

        // A newly inserted key always starts with frequency 1.
        if (frequency == 0) {
            leastFrequency = 1;
        }
    }


    public static void main(String[] args) {

//        LFUCache cache = new LFUCache(2);
//        cache.put(1, 1);
//        cache.put(2, 2);
//        int value = cache.get(1);
//        System.out.println(value);
//        cache.put(3, 3);
//        value = cache.get(2);
//        System.out.println(value);
//        value = cache.get(3);
//        System.out.println(value);
//        cache.put(4, 4);
//        value = cache.get(1);
//        System.out.println(value);
//        value = cache.get(3);
//        System.out.println(value);
//        value = cache.get(4);
//        System.out.println(value);

//        LFUCache cache = new LFUCache(2);
//        cache.put(2, 1);
//        cache.put(2, 2);
//        int value = cache.get(2);
//        System.out.println(value);
//        cache.put(1, 1);
//        cache.put(4, 1);
//        value = cache.get(2);
//        System.out.println(value);

        LFUCache cache = new LFUCache(3);
        cache.put(2, 2);
        cache.put(1, 1);
        int value = cache.get(2);
        System.out.println(value);
        value = cache.get(1);
        System.out.println(value);
        value = cache.get(2);
        System.out.println(value);
        cache.put(3, 3);
        cache.put(4, 4);
        value = cache.get(3);
        System.out.println(value);
        value = cache.get(2);
        System.out.println(value);
        value = cache.get(1);
        System.out.println(value);
        value = cache.get(4);
        System.out.println(value);

    }

}

/**
 * Your LFUCache object will be instantiated and called as such:
 * LFUCache obj = new LFUCache(capacity);
 * int param_1 = obj.get(key);
 * obj.put(key,value);
 */


/**
 * Your LFUCache object will be instantiated and called as such:
 * LFUCache obj = new LFUCache(capacity);
 * int param_1 = obj.get(key);
 * obj.put(key,value);
 */

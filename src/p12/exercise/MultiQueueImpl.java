package p12.exercise;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class MultiQueueImpl<T, Q> implements MultiQueue<T, Q>{

    final static String NOT_AVAILABLE_QUEUE = "Queue not Available";
    final static String ALREDY_EXIST_QUEUE = "Queue alredy exist";
    final static String NO_ALTERNATIVES = "There's no alternative queue for moving elements to";

    private final Map<Q, Queue<T>> map;

    public MultiQueueImpl() {
        this.map = new HashMap<>();
    }

    @Override
    public Set<Q> availableQueues() {
        Set<Q> set = new HashSet<>(map.keySet());
        set.remove(null);
        return set;
    }

    @Override
    public void openNewQueue(Q queue) {
        if (this.map.containsKey(queue)) {
            throw new IllegalArgumentException(ALREDY_EXIST_QUEUE);
        }
        
        this.map.put(queue, new LinkedList<T>());
    }

    @Override
    public boolean isQueueEmpty(Q queue) {
        if (!this.map.containsKey(queue)) {
            throw new IllegalArgumentException(NOT_AVAILABLE_QUEUE);
        }

        Queue<T> q = this.map.get(queue);

        return q.isEmpty();
    }

    @Override
    public void enqueue(T elem, Q queue) {
        if (!this.map.containsKey(queue)) {
            throw new IllegalArgumentException(NOT_AVAILABLE_QUEUE);
        }

        Queue<T> q = this.map.get(queue);
        q.add(elem);
    }

    @Override
    public T dequeue(Q queue) {
        if (!this.map.containsKey(queue)) {
            throw new IllegalArgumentException(NOT_AVAILABLE_QUEUE);
        }

        Queue<T> q = this.map.get(queue);

        return q.poll();
    }

    @Override
    public Map<Q, T> dequeueOneFromAllQueues() {
        Map<Q, T> dequedMap = new HashMap<>();

        for (Map.Entry<Q, Queue<T>> entry : map.entrySet()) {
            T element = entry.getValue().poll();
            dequedMap.put(entry.getKey(), element);
        }
        return dequedMap;
    }

    @Override
    public Set<T> allEnqueuedElements() {
        Set<T> allEnqueued = new HashSet<T>();

        for (Map.Entry<Q, Queue<T>> entry : map.entrySet()) {
            Queue<T> queue = entry.getValue();
            allEnqueued.addAll(queue);
        }

        return allEnqueued;
    }

    @Override
    public List<T> dequeueAllFromQueue(Q queue) {
        if (!this.map.containsKey(queue)) {
            throw new IllegalArgumentException(NOT_AVAILABLE_QUEUE);
        }

        Queue<T> q = map.get(queue);
        List<T> allDequed = new LinkedList<>(q);
        q.clear();

        return allDequed;
    }

    @Override
    public void closeQueueAndReallocate(Q queue) {
        if (!this.map.containsKey(queue)) {
            throw new IllegalArgumentException(NOT_AVAILABLE_QUEUE);
        }
        if (map.size() <= 1) {
            throw new IllegalStateException(NO_ALTERNATIVES);
        }

        Queue<T> q = new LinkedList<T>(map.get(queue));   
        boolean isTransferred = false;
        Iterator<Map.Entry<Q, Queue<T>>> iterator = map.entrySet().iterator();
        while (!isTransferred && iterator.hasNext()) {
            Map.Entry<Q, Queue<T>> elem = iterator.next();
            if(!elem.getValue().equals(q)){
                elem.getValue().addAll(q);
                isTransferred = true;
            }
        }
        map.remove(queue);
    }

}

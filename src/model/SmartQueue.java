package model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Queue;

public class SmartQueue<T> implements Serializable {
    private final Queue<QueueItem<T>> queue = new LinkedList<>(); //generic

    public synchronized void enqueue(T item) {
        queue.add(new QueueItem<>(item));
        notify(); // Notify waiting thread
    }
    
    public void clear() {
    synchronized (queue) {
        queue.clear();
    }
}

    public void setItems(Queue<QueueItem<T>> items) {
    synchronized (queue) {
        queue.clear();
        queue.addAll(items);
    }
}


    public QueueItem<T> dequeue() throws InterruptedException {
    synchronized (queue) {
        while (queue.isEmpty()) {
            queue.wait();
        }
        return queue.poll(); // aman karena tunggu sampai isi
    }
}


    public synchronized int size() {
        return queue.size();
    }

    public Queue<QueueItem<T>> getAllItems() {
    synchronized (queue) {
        return new LinkedList<>(queue);
    }
}

    
    public QueueItem<T> dequeueNow() {
    synchronized (queue) {
        return queue.poll(); // Tidak blocking, langsung return null jika kosong
    }
}

}
//Pemrograman Generic
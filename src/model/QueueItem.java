package model;

import java.io.Serializable;

public class QueueItem<T> implements Serializable {
    private T data;

    public QueueItem(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }
}
//Pemrograman Generic
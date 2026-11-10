package org.firstinspires.ftc.teamcode.helpers;

import java.util.Arrays;

public class EvictingArray<T> {
    private final T[] buffer;
    private final int capacity;
    private int writePointer = 0;
    private int size = 0;

    @SuppressWarnings("unchecked")
    public EvictingArray(int capacity) {
        this.capacity = capacity;
        // Java doesn't allow direct generic array creation, so we cast an Object array
        this.buffer = (T[]) new Object[capacity];
    }

    public void add(T element) {
        buffer[writePointer] = element;
        // Wrap the pointer around to 0 if it exceeds capacity
        writePointer = (writePointer + 1) % capacity;

        if (size < capacity) {
            size++;
        }
    }

    public int size() {
        return size;
    }

    // Optional: Return elements in chronological order (oldest to newest)
    @SuppressWarnings("unchecked")
    public T[] getAll() {
        T[] result = (T[]) new Object[size];
        for (int i = 0; i < size; i++) {
            // Find chronological start point if buffer has wrapped around
            int index = (writePointer - size + i + capacity) % capacity;
            result[i] = buffer[index];
        }
        return result;
    }
}

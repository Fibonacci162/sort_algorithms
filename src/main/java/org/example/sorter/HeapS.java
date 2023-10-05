package org.example.sorter;

import org.example.main.WrappedArray;

public class HeapS extends Sorter {
    @Override
    public String toString() {
        return "Heap";
    }

    @Override
    public int adjust(int d, int n) {
        return d;
    }

    private void heapify(WrappedArray a, int i, int n) {
        if (2*i > n) {
            return;
        }
        if (2*i == n) {
            if (a.get(i-1) < a.get(2*i-1)) {
                a.swap(i-1, 2*i-1);
            }
        }
        else {
            int c = a.get(i-1);
            int l = a.get(2*i-1);
            int r = a.get(2*i);
            if (l > r) {
                if (c < l) {
                    a.swap(i-1, 2*i-1);
                    heapify(a, 2*i, n);
                }
            }
            else {
                if (c < r) {
                    a.swap(i-1, 2*i);
                    heapify(a, 2*i+1, n);
                }
            }
        }
    }

    private void buildHeap(WrappedArray a) {
        int n = a.size();
        for (int i = n; i > 0; i--) {
            heapify(a, i, n);
        }
    }

    private void sortHeap(WrappedArray a) {
        int n = a.size();
        for (int i = n; i > 0; i--) {
            a.swap(0, i-1);
            heapify(a, 1, i-1);
        }
    }

    @Override
    public void sort(WrappedArray arr) {
        try {
            buildHeap(arr);
            sortHeap(arr);
        }
        catch (WrappedArray.WrappedInterrupt e) {
            Thread.currentThread().interrupt();
        }
    }
}

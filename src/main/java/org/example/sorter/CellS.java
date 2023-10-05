package org.example.sorter;

import org.example.main.WrappedArray;

import java.util.Deque;
import java.util.LinkedList;

public class CellS extends Sorter{
    public String toString() {
        return "Cell";
    }

    public int adjust(int d, int n) {
        return (int)((double)d / (double)(n));
    }

    private boolean isSorted(WrappedArray arr, int n) {
        int a = arr.get(0);
        for (int i = 1; i < n; i++) {
            int b = arr.rAccess(i);
            if (b < a) {
                return false;
            }
            a = b;
        }
        return true;
    }

    public void sort(WrappedArray arr) {
        try {
            Deque<Integer> d = new LinkedList<>();
            int n = arr.size();
            while (!isSorted(arr, n)) {
                int a = arr.get(0);
                d.add(a);
                for (int i=1; i < n; i++) {
                    int b = arr.get(i);
                    if (b < a) {
                        d.addFirst(b);
                    }
                    else {
                        d.addLast(b);
                    }
                    a = b;
                }
                int it=0;
                while (!d.isEmpty()) {
                    arr.set(it, d.removeFirst());
                    it++;
                }
            }
        }
        catch (WrappedArray.WrappedInterrupt e) {
            Thread.currentThread().interrupt();
        }
    }
}

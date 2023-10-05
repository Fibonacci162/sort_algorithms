package org.example.sorter;

import org.example.main.WrappedArray;

import java.util.ArrayList;
import java.util.LinkedList;

public class RadixS extends Sorter {
    private final int base;

    public RadixS(int base) {
        this.base = base;
    }

    public RadixS() {
        this(2);
    }

    @Override
    public String toString() {
        return "Radix LSD Base " + base;
    }

    @Override
    public int adjust(int d, int n) {
        return (int)((double)d * Math.log(base) / Math.log(2));
    }

    @Override
    public void sort(WrappedArray arr) {
        try {
            int n = arr.size();
            int m = 0;
            for (int i=0; i<n; i++) {
                m = Math.max(arr.rAccess(i), m);
            }
            m++;
            ArrayList<LinkedList<Integer>> buckets = new ArrayList<>();
            for (int i=0; i<base; i++) {
                buckets.add(new LinkedList<>());
            }
            int pow = 1;
            while (pow < m) {
                for (int val : arr) {
                    int x = (val / pow) % base;
                    buckets.get(x).addLast(val);
                }
                int it=0;
                for (int i=0; i<base; i++) {
                    while (!buckets.get(i).isEmpty()) {
                        int x = buckets.get(i).removeFirst();
                        arr.set(it++, x);
                    }
                }
                pow *= base;
            }
        }
        catch (WrappedArray.WrappedInterrupt e) {
            Thread.currentThread().interrupt();
        }
    }
}

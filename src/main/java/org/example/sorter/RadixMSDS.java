package org.example.sorter;

import org.example.main.WrappedArray;

import java.util.ArrayList;
import java.util.LinkedList;

public class RadixMSDS extends Sorter {
    private final int base;

    public RadixMSDS(int base) {
        this.base = base;
    }

    public RadixMSDS() {
        this(2);
    }

    @Override
    public String toString() {
        return "Radix MSD Base " + base;
    }

    @Override
    public int adjust(int d, int n) {
        return (int)((double)d * Math.log(base) / Math.log(2));
    }

    private void msdsort(WrappedArray arr, int first, int last, int p) {
        if (p == 0 || last <= first) {
            return;
        }
        ArrayList<LinkedList<Integer>> buckets = new ArrayList<>();
        for (int i=0; i<base; i++) {
            buckets.add(new LinkedList<>());
        }
        for (int i = first; i<last; i++) {
            int val = arr.get(i);
            int x = (val / p) % base;
            buckets.get(x).addLast(val);
        }
        int it=first;
        for (int i=0; i<base; i++) {
            for(int x : buckets.get(i)) {
                arr.set(it++, x);
            }
        }
        int a = first;
        int b;
        for (int i=0; i<base; i++) {
            b = a + buckets.get(i).size();
            msdsort(arr, a, b, p/base);
            a = b;
        }
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
            int pow = 1;
            while (pow < m) {
                pow *= base;
            }
            pow /= base;
            msdsort(arr, 0, n, pow);
        }
        catch (WrappedArray.WrappedInterrupt e) {
            Thread.currentThread().interrupt();
        }
    }
}

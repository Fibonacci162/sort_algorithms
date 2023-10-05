package org.example.sorter;

import org.example.main.WrappedArray;

import java.util.ArrayList;
import java.util.LinkedList;

public class BucketS extends Sorter{
    @Override
    public String toString() {
        return "Bucket";
    }

    @Override
    public int adjust(int d, int n) {
        return (int)((double)d * Math.log(n) / Math.log(2));
    }

    @Override
    public void sort(WrappedArray arr) {
        try {
            int n = arr.size();
            int m = 0;
            for (int d : arr) {
                m = Math.max(d, m);
            }
            m++;
            ArrayList<LinkedList<Integer>> buckets = new ArrayList<>();
            for (int i=0; i<m; i++) {
                buckets.add(new LinkedList<>());
            }
            for (int val : arr) {
                buckets.get(val).addLast(val);
            }
            int it=0;
            for (int i=0; i<m; i++) {
                while (!buckets.get(i).isEmpty()) {
                    int x = buckets.get(i).removeFirst();
                    arr.set(it++, x);
                }
            }
        }
        catch (WrappedArray.WrappedInterrupt e) {
            Thread.currentThread().interrupt();
        }
    }
}

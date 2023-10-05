package org.example.sorter;

import org.example.main.WrappedArray;

public class SlowS extends Sorter{
    @Override
    public String toString() {
        return "Slow";
    }

    public int adjust(int d, int n) {
        return n <= 256 ? 1 : 0;
    }

    private void slow(WrappedArray a, int p, int k) {
        if (p >= k) {
            return;
        }
        int mid = (p+k)/2;
        slow(a, p, mid);
        slow(a, mid+1, k);
        if (a.get(mid) > a.get(k)) {
            a.swap(mid, k);
        }
        if (a.get(mid+1) < a.get(p)) {
            a.swap(mid+1, p);
        }
        slow(a, p+1, k-1);
    }

    public void sort(WrappedArray arr) {
        try {
            int n = arr.size();
            slow(arr, 0, n-1);
        }
        catch (WrappedArray.WrappedInterrupt e) {
            Thread.currentThread().interrupt();
        }
    }
}

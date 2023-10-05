package org.example.shuffler;

import org.example.main.WrappedArray;

import java.util.concurrent.ThreadLocalRandom;

public class RandomMinHeap extends Shuffler{
    @Override
    public void shuffle(WrappedArray arr, int m) {
        int n = arr.size();
        for (int i=0; i<n; i++) {
            arr.rSet(i,i);
        }
        for (int i=0; i<n; i++) {
            int j = i+ ThreadLocalRandom.current().nextInt(n-i);
            arr.swap(i,j);
        }
        for (int i=n; i>0; i--) {
            if (2*i < n) {
                if (arr.rAccess(2*i-1) > arr.rAccess(2*i)) {
                    if (arr.rAccess(i-1) > arr.rAccess(2*i)) {
                        arr.swap(i-1, 2*i);
                    }
                }
                else {
                    if (arr.rAccess(i-1) > arr.rAccess(2*i-1)) {
                        arr.swap(i-1, 2*i-1);
                    }
                }
            }
            if (2*i <= n) {
                if (arr.rAccess(i-1) > arr.rAccess(2*i-1)) {
                    arr.swap(i-1, 2*i-1);
                }
            }
        }
    }

    @Override
    public boolean respectLimit() {
        return false;
    }

    @Override
    public String toString() {
        return "Random Min Heap";
    }
}

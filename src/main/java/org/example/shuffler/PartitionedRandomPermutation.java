package org.example.shuffler;

import org.example.main.WrappedArray;

import java.util.concurrent.ThreadLocalRandom;

public class PartitionedRandomPermutation extends Shuffler {
    @Override
    public void shuffle(WrappedArray arr, int m) {
        int n = arr.size();
        for (int i=0; i<n; i++) {
            arr.rSet(i,i);
        }
        for (int i=0; i<n/3; i++) {
            int j = i+ ThreadLocalRandom.current().nextInt(n/3-i);
            arr.swap(i,j);
        }
        for (int i=n/3; i<2*n/3; i++) {
            int j = i+ ThreadLocalRandom.current().nextInt(2*n/3-i);
            arr.swap(i,j);
        }
        for (int i=2*n/3; i<n; i++) {
            int j = i+ ThreadLocalRandom.current().nextInt(n-i);
            arr.swap(i,j);
        }
    }

    @Override
    public boolean respectLimit() {
        return false;
    }

    @Override
    public String toString() {
        return "Partitioned Random Permutation";
    }
}

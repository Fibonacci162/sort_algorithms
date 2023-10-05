package org.example.shuffler;

import org.example.main.WrappedArray;

import java.util.concurrent.ThreadLocalRandom;

public class AlmostSorted extends Shuffler{
    @Override
    public void shuffle(WrappedArray arr, int m) {
        int n = arr.size();

        for (int i=0; i<n; i++) {
            double d  = (double)m * (double)(i) / (double)(n);
            int x = Math.max(0, Math.min(m-1, (int)d + ThreadLocalRandom.current().nextInt(12)-6));
            arr.set(i, x);
        }
    }

    @Override
    public boolean respectLimit() {
        return true;
    }

    @Override
    public String toString() {
        return "Almost Sorted";
    }
}

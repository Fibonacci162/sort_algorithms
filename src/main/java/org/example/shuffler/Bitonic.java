package org.example.shuffler;

import org.example.main.WrappedArray;

public class Bitonic extends Shuffler{
    @Override
    public void shuffle(WrappedArray arr, int m) {
        int n = arr.size();
        for (int i=0; i<n/2; i++) {
            arr.set(i, 2*i);
        }
        int x = (n-1) % 2 == 0 ? n-2 : n-1;
        for (int i=n/2; i<n; i++) {
            arr.set(i, x - 2*(i - n/2));
        }
    }

    @Override
    public boolean respectLimit() {
        return false;
    }

    @Override
    public String toString() {
        return "Bitonic Sequence";
    }
}

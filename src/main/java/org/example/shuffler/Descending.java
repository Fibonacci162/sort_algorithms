package org.example.shuffler;

import org.example.main.WrappedArray;

public class Descending extends Shuffler {
    @Override
    public void shuffle(WrappedArray arr, int m) {
        int n = arr.size();
        for (int i=0; i<n; i++) {
            arr.rSet(i,n-1-i);
        }
    }

    @Override
    public boolean respectLimit() {
        return false;
    }

    @Override
    public String toString() {
        return "Descending";
    }
}

package org.example.shuffler;

import org.example.main.WrappedArray;

import java.util.concurrent.ThreadLocalRandom;

public class RandomNumbers extends Shuffler{
    @Override
    public void shuffle(WrappedArray arr, int m) {
        int n = arr.size();
        for (int i=0; i<n; i++) {
            arr.set(i, ThreadLocalRandom.current().nextInt(m));
        }
    }

    @Override
    public boolean respectLimit() {
        return true;
    }

    @Override
    public String toString() {
        return "Uniform Random Numbers";
    }
}

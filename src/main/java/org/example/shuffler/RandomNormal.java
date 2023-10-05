package org.example.shuffler;

import org.example.main.WrappedArray;
import org.apache.commons.rng.UniformRandomProvider;
import org.apache.commons.statistics.distribution.*;

import java.util.concurrent.ThreadLocalRandom;

public class RandomNormal extends Shuffler{

    private int bound(double d, int m) {
        int x = (int)d;
        if (x < 0) {
            return 0;
        }
        else if (x >= m) {
            return m-1;
        }
        return x;
    }

    @Override
    public void shuffle(WrappedArray arr, int m) {
        int n = arr.size();
        ContinuousDistribution cd = NormalDistribution.of(m * 0.5, m * 0.2);
        var sam = cd.createSampler(new UniformRandomProvider() {
            @Override
            public long nextLong() {
                return ThreadLocalRandom.current().nextLong();
            }
        });
        for (int i=0; i<n; i++) {
            arr.set(i, bound(sam.sample(),m));
        }
    }

    @Override
    public boolean respectLimit() {
        return true;
    }

    @Override
    public String toString() {
        return "Normal Random Numbers";
    }
}

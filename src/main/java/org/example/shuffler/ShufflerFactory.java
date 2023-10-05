package org.example.shuffler;

import java.util.ArrayList;
import java.util.List;

public class ShufflerFactory{
    public static List<Shuffler> shufflers() {
        List<Shuffler> res = new ArrayList<>();
        res.add(new RandomNumbers());
        res.add(new RandomPermutation());
        res.add(new Descending());
        res.add(new Bitonic());
        res.add(new RandomMaxHeap());
        res.add(new RandomMinHeap());
        res.add(new RandomNormal());
        res.add(new AlmostSorted());
        res.add(new PartitionedRandomPermutation());
        return res;
    }
}

package org.example.sorter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class SorterFactory {
    public static Map<String, List<Sorter>> alg_opts() {
        Map<String, List<Sorter>> res = new LinkedHashMap<>();
        java.util.List<Sorter> all =
                new ArrayList<>(List.of(new BubbleS(),
                        new CocktailS(),
                        new SelectionS(),
                        new DSelectionS(),
                        new InsertionS(),
                        new GnomeS(),
                        new ExchangeS(),
                        new OddEvenS(),
                        new PancakeS(),
                        new CombS(),
                        new ShellS(),
                        new ShellSTwo(),
                        new QuickS(),
                        new OOPMS(),
                        new TimS(),
                        new WeaveS(),
                        new WeavedS(),
                        new HeapS(),
                        new CountingS(),
                        new BucketS(),
                        new RadixS(),
                        new RadixS(16),
                        new RadixMSDS(),
                        new BitonicS(),
                        new BlockS(),
                        new STDS()
                        //new ALSorter()
                ));
        res.put("All", all);
        java.util.List<Sorter> shuffled =
                new ArrayList<>(all);
        int n = shuffled.size();
        for (int i = 0; i < n; i++) {
            int j = i + ThreadLocalRandom.current().nextInt(n-i);
            Sorter si = shuffled.get(i);
            Sorter sj = shuffled.get(j);
            shuffled.set(i, sj);
            shuffled.set(j, si);
        }
        res.put("All, shuffled", shuffled);
        List<Sorter> classics = new ArrayList<>(List.of(
                new BubbleS(),
                new InsertionS(),
                new SelectionS(),
                new QuickS(),
                new OOPMS(),
                new HeapS(),
                new BucketS()
        ));
        res.put("Classics", classics);
        List<Sorter> log = new ArrayList<>(List.of(
                new QuickS(),
                new OOPMS(),
                new TimS(),
                new HeapS(),
                new RadixS(2),
                new RadixMSDS(2),
                new BlockS(),
                new STDS()
        ));
        res.put("Logarithmic", log);
        List<Sorter> quad = new ArrayList<>(List.of(
                new BubbleS(),
                new CocktailS(),
                new SelectionS(),
                new DSelectionS(),
                new InsertionS(),
                new GnomeS(),
                new OddEvenS(),
                new ExchangeS(),
                new PancakeS()
        ));
        res.put("Quadratic", quad);
        for (Sorter s : all) {
            res.put(s.toString(), new ArrayList<>(List.of(s)));
        }
        List<Sorter> jokes = new ArrayList<>(List.of(
                new CellS(),
                new StoogeS(),
                new SlowS(),
                new InsertionMergeS(),
                // Because it uses n*m memory and n*m time,
                // and it's faster in the real world
                new GravityS()
        ));
        res.put("Joke algorithms", jokes);
        for (Sorter s : jokes) {
            res.put(s.toString(), new ArrayList<>(List.of(s)));
        }
        return res;
    }
}

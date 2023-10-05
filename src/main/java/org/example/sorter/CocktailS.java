package org.example.sorter;

import org.example.main.WrappedArray;

public class CocktailS extends Sorter {
    public String toString() {
        return "Cocktail Shaker";
    }

    @Override
    public int adjust(int d, int n) {
        return (int)((Math.log(n) / Math.log(2)) * (double)d / (double)n);
    }

    public void sort(WrappedArray arr) {
        try {
            int beg = 0;
            int en = arr.size();
            boolean swapped = true;
            while (swapped && beg < en && !Thread.currentThread().isInterrupted()) {
                swapped = false;
                for(int j=beg+1; j<en; j++) {
                    if (arr.get(j) < arr.get(j-1)) {
                        arr.swap(j, j-1);
                        swapped = true;
                    }
                }
                for(int j=en-1; j>beg; j--) {
                    if (arr.get(j) < arr.get(j-1)) {
                        arr.swap(j, j-1);
                        swapped = true;
                    }
                }
                beg++;
                en--;
            }
        }
        catch (WrappedArray.WrappedInterrupt e) {
            Thread.currentThread().interrupt();
        }
    }
}

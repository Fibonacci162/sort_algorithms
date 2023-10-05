package org.example.sorter;

import org.example.main.WrappedArray;

public class QuickS extends Sorter {
    @Override
    public String toString() {
        return "Quick";
    }

    @Override
    public int adjust(int d, int n) {
        return d;
    }

    private void insertion(WrappedArray arr, int p, int k) {
        for(int i=p+1; i<k && !Thread.currentThread().isInterrupted(); i++) {
            for(int j=i; j>p; j--) {
                if (arr.get(j) >= arr.get(j-1)) {
                    break;
                }
                arr.swap(j, j-1);
            }
        }
    }

    private void quick(WrappedArray arr, int p, int k) {
        if (k-p < 16) {
            insertion(arr, p, k);
        }
        else {
            int m = (p+k)/2;
            if (arr.get(p) < arr.get(k-1)) {
                if (arr.get(k-1) < arr.get(m)) {
                    arr.swap(k-1, p);
                }
                else if (arr.get(m) > arr.get(p)) {
                    arr.swap(m, p);
                }
            }
            else {
                if (arr.get(m) < arr.get(k-1)) {
                    arr.swap(k-1, p);
                }
                else if (arr.get(m) < arr.get(p)) {
                    arr.swap(m, p);
                }
            }
            int it1 = p+1;
            int it2 = k-1;
            int pivot = arr.get(p);
            while (it1 < it2) {
                while (arr.get(it1) <= pivot && it1 < it2) {
                    it1++;
                }
                while (arr.get(it2) > pivot && it1 < it2) {
                    it2--;
                }
                if (it1 < it2) {
                    arr.swap(it1, it2);
                }
            }
            if (arr.get(it1) > arr.get(p)) {
                it1--;
            }
            arr.swap(it1, p);
            quick(arr, p, it1);
            quick(arr, it1+1, k);
        }
    }


    @Override
    public void sort(WrappedArray arr) {
        try {
            quick(arr, 0, arr.size());
        }
        catch (WrappedArray.WrappedInterrupt e) {
            Thread.currentThread().interrupt();
        }
    }
}

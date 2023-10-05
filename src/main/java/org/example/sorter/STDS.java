package org.example.sorter;

import org.example.main.WrappedArray;

/*
* Implementation of std::sort in gcc taken from https://gcc.gnu.org/onlinedocs/libstdc++/libstdc++-html-USERS-4.4/a01347.html
* */

public class STDS extends Sorter {
    @Override
    public String toString() {
        return "std::sort (gcc)";
    }

    @Override
    public int adjust(int d, int n) {
        return d;
    }

    private int lg(int n) {
        int k;
        for (k=0; n != 0; n >>= 1) {
            ++k;
        }
        return k-1;
    }

    private int median(int __a, int __b, int __c) {
        if (__a < __b)
            if (__b < __c)
                return __b;
            else if (__a < __c)
                return __c;
            else
                return __a;
        else if (__a < __c)
            return __a;
        else if (__b < __c)
            return __c;
        else
            return __b;
    }

    private int unguarded_partition(WrappedArray a, int first, int last, int pivot) {
        while (true)
        {
            while (a.get(first) < pivot)
                ++first;
            --last;
            while (pivot < a.get(last))
                --last;
            if (!(first < last))
                return first;
            a.swap(first, last);
            ++first;
        }
    }

    private void push_heap(WrappedArray a, int first, int holeIndex, int topIndex, int value) {
        int parent = (holeIndex - 1) / 2;
        while (holeIndex > topIndex && a.get(first + parent) < value)
        {
            a.set(first + holeIndex, a.get(first + parent));
            holeIndex = parent;
            parent = (holeIndex - 1) / 2;
        }
        a.set(first + holeIndex, value);
    }

    private void adjust_heap(WrappedArray a, int first, int holeIndex, int len, int value) {
        int topIndex = holeIndex;
        int secondChild = holeIndex;
        while (secondChild < (len - 1) / 2)
        {
            secondChild = 2 * (secondChild + 1);
            if (a.get(first + secondChild) < a.get(first + (secondChild - 1)))
                secondChild--;
            a.set(first + holeIndex, a.get(first + secondChild));
            holeIndex = secondChild;
        }
        if ((len & 1) == 0 && secondChild == (len - 2) / 2)
        {
            secondChild = 2 * (secondChild + 1);
            a.set(first + holeIndex, a.get(first + (secondChild - 1)));
            holeIndex = secondChild - 1;
        }
        push_heap(a, first, holeIndex, topIndex, value);
    }

    private void make_heap(WrappedArray a, int first, int last) {
        if (last - first < 2)
            return;
        int len = last - first;
        int parent = (len - 2) / 2;
        while (true)
        {
            int value = a.get(first + parent);
            adjust_heap(a, first, parent, len, value);
            if (parent == 0)
                return;
            parent--;
        }
    }

    private void pop_heap(WrappedArray a, int first, int last, int result) {
        int value = a.get(result);
        a.set(result, a.get(first));
        adjust_heap(a, first, 0, last-first, value);
    }

    private void heap_select(WrappedArray a, int first, int middle, int last) {
        make_heap(a, first, middle);
        for (int i=middle; i<last; i++) {
            if (a.get(i) < a.get(first)) {
                pop_heap(a, first, middle, i);
            }
        }
    }

    private void sort_heap(WrappedArray a, int first, int last) {
        while (last - first > 1) {
            --last;
            pop_heap(a, first, last, last);
        }
    }

    private void partial_sort(WrappedArray a, int first, int middle, int last) {
        heap_select(a, first, middle, last);
        sort_heap(a, first, middle);
    }

    private static final int S_threshhold = 16;

    private void introsort_loop(WrappedArray a, int first, int last, int depthLimit) {
        while (last - first > S_threshhold) {
            if (depthLimit == 0) {
                partial_sort(a, first, last, last);
                return;
            }
            --depthLimit;
            int cut = unguarded_partition(a, first, last, median(a.get(first), a.get(first+(last-first)/2), a.get(last-1)));
            introsort_loop(a, cut, last, depthLimit);
            last = cut;
        }
    }

    private void unguarded_linear_insert(WrappedArray a, int last, int val) {
        int next = last;
        --next;
        while(val < a.get(next)) {
            a.set(last, a.get(next));
            last = next;
            --next;
        }
        a.set(last, val);
    }

    private void insertion_sort(WrappedArray a, int first, int last) {
        if (first == last) {
            return;
        }
        for (int i = first + 1; i < last; i++) {
            int val = a.get(i);
            if (val < a.get(first)) {
                a.shiftR(first, i+1);
            }
            else {
                unguarded_linear_insert(a, i, val);
            }
        }
    }

    private void unguarded_insertion_sort(WrappedArray a, int first, int last) {
        for (int i=first; i<last; i++) {
            unguarded_linear_insert(a, i, a.get(i));
        }
    }

    private void final_insertion_sort(WrappedArray a, int first, int last) {
        if (last-first > S_threshhold) {
            insertion_sort(a, first, first+S_threshhold);
            unguarded_insertion_sort(a, first+S_threshhold, last);
        }
        else {
            insertion_sort(a, first, last);
        }
    }

    @Override
    public void sort(WrappedArray a) {
        try {
            introsort_loop(a, 0, a.size(), lg(a.size()) * 2);
            final_insertion_sort(a, 0, a.size());
        }
        catch (WrappedArray.WrappedInterrupt e) {
            Thread.currentThread().interrupt();
        }
    }
}

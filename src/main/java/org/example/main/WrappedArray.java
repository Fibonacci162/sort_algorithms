package org.example.main;

import java.util.*;

public class WrappedArray extends ArrayList<Integer> {

    private int del;

    private int sc = 0;

    private int la = 0;

    private int brdr = -1;

    public WrappedArray(int del, Integer[] arr) {
        super(List.of(arr));
        this.del = del;
    }

    public void cd(int nd) {
        del = nd;
    }

    public int lAccess() {
        return la;
    }

    public void sla(int x) {
        la = x;
    }

    public void check() {
        try {
            for (int i = 0; i < size(); i++) {
                sleep();
                brdr++;
            }
            Thread.sleep(500);
            brdr = -1;
        }
        catch (WrappedInterrupt |InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public int state(int i) {
        if (i > brdr) {
            return 0;
        }
        if (i==0) {
            return 1;
        }
        if (rAccess(i) >= rAccess(i-1)) {
            return 1;
        }
        return -1;
    }

    public static class WrappedInterrupt extends RuntimeException {

    }

    public void sleep() {
        try {
            sc += del;
            while (sc >= 10000) {
                Thread.sleep(10);
                sc -= 10000;
            }
        }
        catch (InterruptedException e) {
            throw new WrappedInterrupt();
        }
    }

    private static class WrappedIterator implements Iterator<Integer> {

        private final Iterator<Integer> it;
        private int i = 0;
        private final WrappedArray wr;

        public WrappedIterator(WrappedArray wr) {
            this.wr = wr;
            this.it = wr.supiterator();
        }

        @Override
        public boolean hasNext() {
            return it.hasNext();
        }

        @Override
        public Integer next() {
            wr.sla(i++);
            wr.sleep();
            return it.next();
        }
    }

    @Override
    public Iterator<Integer> iterator() {
        return new WrappedIterator(this);
    }

    protected Iterator<Integer> supiterator() {
        return super.iterator();
    }

    @Override
    public Integer get(int index) {
        la = index;
        sleep();
        return super.get(index);
    }

    @Override
    public Integer set(int index, Integer element) {
        la = index;
        sleep();
        return super.set(index, element);
    }

    public Integer rAccess(int i) {
        return super.get(i);
    }

    public void rSet(int i, int v) {
        super.set(i, v);
    }

    public void swap(int i, int j) {
        la = j;
        sleep();
        sleep();
        Integer t1 = super.get(i);
        Integer t2 = super.get(j);
        super.set(i, t2);
        super.set(j, t1);
    }

    @Override
    public void sort(Comparator<? super Integer> c) {
        super.sort(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                sleep();
                sleep();
                return c.compare(o1, o2);
            }
        });
    }

    public void flip(int beg, int en) {
        int i = beg;
        int j = en-1;
        while (i < j) {
            swap(i,j);
            i++;
            j--;
        }
    }

    public void shiftR(int beg, int en) {
        int t = get(en-1);
        for (int i = beg; i<en; i++) {
            int t2 = rAccess(i);
            set(i, t);
            t = t2;
        }
    }
}

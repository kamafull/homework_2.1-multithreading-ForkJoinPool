package org.digitalhabits.impl;

import org.digitalhabits.Sorter;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

/**
 * Your parallel version of {@link Sorter} to implement
 */
public class ParallelSorter implements Sorter {
    @Override
    public void sort(int[] source) {
        ForkJoinPool pool = ForkJoinPool.commonPool();
        pool.invoke(new SortAction(source, 0, source.length - 1));
    }

    private class SortAction extends RecursiveAction {
        private final int bubbleBlock = 16;

        int[] a;
        int lo;
        int hi;

        SortAction(int[] a, int lo, int hi) {
            this.a = a;
            this.lo = lo;
            this.hi = hi;
        }

        @Override
        protected void compute() {
            if(hi <= lo) return;

            if ((hi - lo) > bubbleBlock) {
                // Находим средний элемент
                int j = partition(a, lo, hi);
                // Рекусивное вызов левой / правой подчасти
                invokeAll(new SortAction(a, lo, j - 1), new SortAction(a, j + 1, hi));
            }else{
                // Для маленького массива применим пызырьковую сортировку
                bubbleSort(a, lo, hi + 1);
            }
        }

        /**
         * Сортировка пузырьком, для ускорении сортировки маленьких подблоков
         */
        private void bubbleSort(int[] a, int lo, int hi){
            for (int i = lo; i < hi; i++) {
                for (int j = i; j < hi; j++) {
                    if (a[i] > a[j]) {
                        int tmp = a[i];
                        a[i] = a[j];
                        a[j] = tmp;
                    }
                }
            }
        }
    }

    private int partition(int[] a, int lo, int hi) {
        int i = lo, j = hi + 1;
        int v = a[lo];
        while (true) {
            while (less(a[++i], v)) if (i == hi) break;
            while (less(v, a[--j])) if (j == lo) break;
            if (i >= j ) break;
            exch(a, i, j);
        }

        exch(a, lo, j);
        return j;
    }

    private void exch(int[] a, int i, int j) {
        int tmp = a[i];
        a[i] = a[j];
        a[j] = tmp;
    }

    private boolean less(int i, int v) {
        return i < v;
    }


}

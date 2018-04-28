package com.robertboothby.djenni.sugar;

import com.robertboothby.djenni.SupplierBuilder;

public class RangeCatcher<T extends SupplierBuilder>{
    private int minimumSize;
    private int maximumSize;
    private final T generatorBuilder;

    public RangeCatcher(int defaultMinimumSize, int defaultMaximumSize, T generatorBuilder) {
        this.minimumSize = defaultMinimumSize;
        this.maximumSize = defaultMaximumSize;
        this.generatorBuilder = generatorBuilder;
    }

    public And<T, Integer> between(int minimumSize) {
        this.minimumSize = minimumSize;
        return maximumSize -> {
            if(maximumSize < minimumSize){
                throw new IllegalArgumentException("Maximum must be greater or equal to minimum size.");
            }
            this.maximumSize = maximumSize;
            return generatorBuilder;
        };
    }

    public int getMinimumSize() {
        return minimumSize;
    }

    public int getMaximumSize() {
        return maximumSize;
    }
}

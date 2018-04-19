package com.robertboothby.djenni.sugar;

import com.robertboothby.djenni.GeneratorBuilder;
import com.robertboothby.djenni.sugar.And;

public class SizeCatcher<T extends GeneratorBuilder>{
    private int minimumSize;
    private int maximumSize;
    private final T generatorBuilder;

    public SizeCatcher(int defaultMinimumSize, int defaultMaximumSize, T generatorBuilder) {
        this.minimumSize = defaultMinimumSize;
        this.maximumSize = defaultMaximumSize;
        this.generatorBuilder = generatorBuilder;
    }

    public And<T, Integer> between(int minimumSize) {
        this.minimumSize = minimumSize;
        return maximumSize -> {
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

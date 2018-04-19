package com.robertboothby.djenni.util;

import com.robertboothby.djenni.Generator;
import com.robertboothby.djenni.GeneratorBuilder;
import com.robertboothby.djenni.sugar.SizeCatcher;

import java.util.List;
import java.util.function.Consumer;

import static com.robertboothby.djenni.core.GeneratorHelper.buildAn;
import static com.robertboothby.djenni.lang.IntegerGeneratorBuilder.integerGenerator;

public class SimpleListGeneratorBuilder<T> implements GeneratorBuilder<List<T>>{

    private SizeCatcher<SimpleListGeneratorBuilder<T>> sizeCatcher = new SizeCatcher<>(1,1, this);
    private Generator<T> entryGenerator;

    @Override
    public Generator<List<T>> build() {
        return new SimpleListGenerator<>(
                buildAn(integerGenerator()
                        .between(sizeCatcher.getMinimumSize())
                        .and(sizeCatcher.getMaximumSize())
                ),
                entryGenerator);
    }

    public SimpleListGeneratorBuilder<T> withEntryGenerator(Generator<T> entryGenerator) {
        this.entryGenerator = entryGenerator;
        return this;
    }

    public SimpleListGeneratorBuilder<T> withEntryGenerator(GeneratorBuilder<T> builder) {
        this.entryGenerator = builder.build();
        return this;
    }

    public static <T> SimpleListGeneratorBuilder<T> simpleListGenerator(Consumer<SimpleListGeneratorBuilder<T>> consumer) {
        SimpleListGeneratorBuilder<T> builder = new SimpleListGeneratorBuilder<>();
        consumer.accept(builder);
        return builder;
    }

    public SizeCatcher<SimpleListGeneratorBuilder<T>> withSize() {
        return sizeCatcher;
    }
}

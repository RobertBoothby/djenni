package com.robertboothby.djenni.util;

import com.robertboothby.djenni.Generator;
import org.hamcrest.Description;

import java.util.ArrayList;
import java.util.List;

public class SimpleListGenerator<T> implements Generator<List<T>> {

    private final Generator<Integer> lengthGenerator;
    private final Generator<T> entryGenerator;

    public SimpleListGenerator(Generator<Integer> lengthGenerator, Generator<T> entryGenerator) {
        this.lengthGenerator = lengthGenerator;
        this.entryGenerator = entryGenerator;
    }

    @Override
    public List<T> generate() {
        List<T> list = new ArrayList<>();
        for(int i = lengthGenerator.generate(); i > 0; i --){
            list.add(entryGenerator.generate());
        }
        return list;
    }

    @Override
    public void describeTo(Description description) {
        //TODO fill this in.
    }
}

package com.robertboothby.djenni.sugar;

public abstract class Range<T, U extends Number> {

    private U minimum;
    private U maximum;
    private final T parent;

    protected Range(T parent) {
        this.parent = parent;
    }

    public abstract boolean inclusive();

    @SuppressWarnings("unchecked")
    public And<T, U> between(U minimum) {
        this.minimum = minimum;
        return maximum -> {
            //TODO throw exception as required.
            this.maximum = maximum;
            return parent;
        };
    }

    public U getMinimum() {
        return minimum;
    }

    public U getMaximum() {
        return maximum;
    }

    public static <T, U extends Number> Range<T, U> inclusive(T parent){
        return new Range<T, U>(parent) {
            @Override
            public boolean inclusive() {
                return true;
            }
        };
    }

    public static <T, U extends Number> Range<T, U> exclusive(T parent){
        return new Range<T, U>(parent) {
            @Override
            public boolean inclusive() {
                return false;
            }
        };
    }

}

package com.robertboothby.djenni.sugar;

import java.util.Objects;

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
        this.minimum = Objects.requireNonNull(minimum, "minimum");
        return maximum -> {
            U resolvedMaximum = Objects.requireNonNull(maximum, "maximum");
            validateRange(resolvedMaximum);
            this.maximum = resolvedMaximum;
            return parent;
        };
    }

    public U getMinimum() {
        if (minimum == null) {
            throw new IllegalStateException("Range minimum has not been configured.");
        }
        return minimum;
    }

    public U getMaximum() {
        if (maximum == null) {
            throw new IllegalStateException("Range maximum has not been configured.");
        }
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

    private void validateRange(U resolvedMaximum) {
        U minimum = this.minimum;
        if (minimum == null) {
            throw new IllegalStateException("Range minimum must be configured before the maximum.");
        }
        double minValue = minimum.doubleValue();
        double maxValue = resolvedMaximum.doubleValue();
        boolean invalid = inclusive() ? maxValue < minValue : maxValue <= minValue;
        if (invalid) {
            throw new IllegalArgumentException(
                    String.format("Maximum value %s must be greater %s minimum %s.",
                            resolvedMaximum,
                            inclusive() ? "than or equal to" : "than",
                            minimum));
        }
    }

}

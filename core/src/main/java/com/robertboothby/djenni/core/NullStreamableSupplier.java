package com.robertboothby.djenni.core;

/**
 * Singleton {@link StreamableSupplier} implementation that always supplies {@code null}.
 */
final class NullStreamableSupplier implements StreamableSupplier<Object> {

    private static final NullStreamableSupplier INSTANCE = new NullStreamableSupplier();

    private NullStreamableSupplier() {
    }

    /**
     * Get the singleton instance as any required generic type.
     * @param <T> the desired supplier value type
     * @return a {@link StreamableSupplier} that always returns {@code null}
     */
    @SuppressWarnings("unchecked")
    static <T> StreamableSupplier<T> instance() {
        return (StreamableSupplier<T>) INSTANCE;
    }

    @Override
    public Object get() {
        return null;
    }
}

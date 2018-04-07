package com.robertboothby.djenni.util;

/**
 * <P>
 *     Nullable class currently used a stand in until I can find a widely used open source version...
 * </P>
 * <P>
 *     This class is used in all code where I want an explicit indicator that a value may be null and so by extension
 *     any attributes, parameters or return types which are not nullable must always return a value. This keeps the
 *     interfaces semantically clear.
 * </P>
 *
 * @author robertboothby
 */
public final class Nullable<T> {

    private T value;

    public Nullable() {
    }

    public Nullable(T value) {
        this.value = value;
    }

    /**
     * Check whether this nullable has a value or not.
     * @return true if there is a value, false otherwise.
     */
    public final boolean hasValue() {
        return value !=null;
    }


    public final T value() {
        if(hasValue()){
            return value;
        }
        throw new RuntimeException("Attempt to directly access a null value");
    }

    public static <Q> Nullable<Q> nullValue(){
        return new Nullable<>();
    }

    public static <Q> Nullable<Q> nullable(Q value) {
        return new Nullable<>(value);
    }
}

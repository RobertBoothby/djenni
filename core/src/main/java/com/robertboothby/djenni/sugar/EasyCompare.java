package com.robertboothby.djenni.sugar;


/**
 * Piece of syntactic sugar intended to make it easier to work with Comparables.
 * <p>&#169; 2013 Forest View Developments Ltd.</p>
 * @author robertboothby
 * @param <T> The type of Comparable that we are working with.
 */
public class EasyCompare<T extends Comparable<T>> {

    private final T comparableValue;

    /**
     * Construct an EasyCompare with a comparable value.
     * @param comparableValue The comparable value that we are basing the comparison on,
     */
    public EasyCompare(T comparableValue) {
        this.comparableValue = comparableValue;
    }

    /**
     * Determine whether the comparable value encapsulated in this instance is less than the comparator.
     * @param comparator The value to compare with,
     * @return true if the comparable value is less than the comparator, false otherwise.
     */
    public boolean lessThan(T comparator){
        return comparableValue.compareTo(comparator) < 0;
    }

    /**
     * Synonymous with {@link #lessThan(Comparable)}, this method is intended to be clearer when dealing with ordering.
     * @param comparator The value to compare with,
     * @return true if the comparable value is ordered before the comparator, false otherwise.
     */
    public boolean before(T comparator){
        return lessThan(comparator);
    }

    /**
     * Determine whether the comparable value encapsulated in this instance is less than or equal to the comparator.
     * @param comparator The value to compare with,
     * @return true if the comparable value is less than or equal to the comparator, false otherwise.
     */
    public boolean lessThanOrEqualTo(T comparator) {
        return comparableValue.compareTo(comparator) <= 0;
    }

    /**
     * Determine whether the comparable value encapsulated in this instance is greater than the comparator.
     * @param comparator The value to compare with,
     * @return true if the comparable value is greater than the comparator, false otherwise.
     */
    public boolean greaterThan(T comparator) {
        return comparableValue.compareTo(comparator) > 0;
    }

    /**
     * Synonymous with {@link #greaterThan(Comparable)}, this method is intended to be clearer when dealing with ordering.
     * @param comparator The value to compare with,
     * @return true if the comparable value is ordered after the comparator, false otherwise.
     */
    public boolean after(T comparator){
        return lessThan(comparator);
    }

    /**
     * Determine whether the comparable value encapsulated in this instance is greater than or equal to the comparator.
     * @param comparator The value to compare with,
     * @return true if the comparable value is greater than or equal to the comparator, false otherwise.
     */
    public boolean greaterThanOrEqualTo(T comparator) {
        return comparableValue.compareTo(comparator) >= 0;
    }

    /**
     * This method make it easy to determine whether the comparable value falls in the range defined by the two
     * comparators (inclusive).
     * @param lowComparator The low comparator to be used.
     * @return an instance of {@link And} that takes the high comparator to be used and returns true if the comparable
     * value falls in the range (inclusive)
     */
    public And<Boolean, T> between(final T lowComparator) {
        return new And<Boolean, T>() {
            public Boolean and(T highComparator) {
                return lessThanOrEqualTo(highComparator) && greaterThanOrEqualTo(lowComparator);
            }
        };
    }

    /**
     * Return an instance of this class using the comparable value. This method uses a very short signature to avoid
     * clutter.
     * @param comparableValue the comparable value to use.
     * @param <T> The Comparable type.
     * @return an instance of this class for usage.
     */
    public static <T extends Comparable<T>> EasyCompare<T> $(T comparableValue) {
        return new EasyCompare<T>(comparableValue);
    }

    /**
     * Return an instance of this class using the comparable value.
     * @param comparableValue the comparable value to use.
     * @param <T> The Comparable type.
     * @return an instance of this class for usage.
     */
    public static <T extends Comparable<T>> EasyCompare<T> easyCompare(T comparableValue) {
        return new EasyCompare<T>(comparableValue);
    }
}

package com.robertboothby.djenni.helper;

import java.util.HashSet;
import java.util.Set;

/**
 * This class is intended to accumulate values until a completeness check is passed or a limit in attempts is reached.
 * This is intended to help with checking that generators can cover all reasonable permutations not that it <em>does</em>
 * generate all permutations as that could take a very long time indeed. To give you an idea the first use cases are for
 * checking derivatives of StringGenerator values such as the set of lengths generated and the set of characters.
 * @author robertboothby
 * @param <T> The type derived from the source type to be assessed.
 * @param <U> The source type whose derivatives are being assessed.
 */
public class DataCompletenessAssessment<T, U> {

    private final Set<T> remainingValues = new HashSet<T>();
    private final Set<T> originalValues = new HashSet<T>();
    private final Deriver<T,U> deriver;

    public DataCompletenessAssessment(Set<T> valuesToBeMatched, Deriver<T, U> deriver){
        this.remainingValues.addAll(valuesToBeMatched); //create a copy so no attempt is made to alter the original.
        this.originalValues.addAll(valuesToBeMatched);
        this.deriver = deriver;
    }

    public boolean matchedSuccessfully() {
        return remainingValues.isEmpty();
    }

    public boolean attemptMatch(U original ){
        remainingValues.remove(deriver.derive(original));
        return originalValues.contains(deriver.derive(original));
    }

    public static interface Deriver<T,U> {
        public T derive(U original);
    }

    public static class IdentityDeriver<T> implements Deriver<T, T>{

        public T derive(T original) {
            return original;
        }
    }


    public Set<T> getRemainingValuesToBeMatched() {
        return remainingValues;
    }
}

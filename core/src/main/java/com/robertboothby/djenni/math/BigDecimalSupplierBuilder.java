package com.robertboothby.djenni.math;

import com.robertboothby.djenni.ConfigurableSupplierBuilder;
import com.robertboothby.djenni.core.StreamableSupplier;
import com.robertboothby.djenni.sugar.And;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.function.Supplier;

import static com.robertboothby.djenni.core.SupplierHelper.fix;
import static com.robertboothby.djenni.math.BigIntegerSupplierBuilder.bigIntegers;

/**
 * FROM STACK OVERFLOW
 * http://stackoverflow.com/questions/2290057/how-to-generate-a-random-biginteger-value-in-java
 *
 * @author robertboothby
 */
public class BigDecimalSupplierBuilder implements ConfigurableSupplierBuilder<BigDecimal, BigDecimalSupplierBuilder> {

    private BigDecimal minimumInclusiveValue = BigDecimal.ZERO;
    private BigDecimal maximumExclusiveValue = BigDecimal.TEN;
    private Supplier<Integer> scaleSupplier = fix(1);


    @Override
    public StreamableSupplier<BigDecimal> build() {
        BigDecimal actualMinimumValue = minimumInclusiveValue;
        BigDecimal actualMaximumExclusiveValue = maximumExclusiveValue;

        //Create new references.
        Supplier<Integer> actualScaleSupplier = scaleSupplier;

        return () -> {
            Integer actualScale = actualScaleSupplier.get();
            MathContext mathContext = new MathContext(actualScale, RoundingMode.HALF_UP);
            BigDecimal exponent = BigDecimal.TEN.pow(actualScale);

            BigInteger actualRange =
                    scaleForRange(actualMaximumExclusiveValue, mathContext, exponent)
                            .subtract(
                                    scaleForRange(actualMinimumValue, mathContext, exponent))
                            .toBigIntegerExact();

            return actualMinimumValue.round(mathContext)
                    .add(
                            new BigDecimal(
                                    bigIntegers()
                                            .minimumInclusiveValue(BigInteger.ZERO)
                                            .maximumExclusiveValue(actualRange)
                                            .build()
                                            .get(),
                                    actualScale));
        };
    }

    private BigDecimal scaleForRange(BigDecimal actualMaximumExclusiveValue, MathContext mathContext, BigDecimal exponent) {
        return actualMaximumExclusiveValue
                .round(mathContext)
                .multiply(exponent);
    }

    public BigDecimalSupplierBuilder minimumInclusiveValue(BigDecimal minimumValue) {
        this.minimumInclusiveValue = minimumValue;
        return this;
    }

    public BigDecimalSupplierBuilder maximumExclusiveValue(BigDecimal maximumExclusiveValue) {
        this.maximumExclusiveValue = maximumExclusiveValue;
        return this;
    }

    public BigDecimalSupplierBuilder scale(Supplier<Integer> scaleSupplier) {
        this.scaleSupplier = scaleSupplier;
        return this;
    }

    public And<BigDecimalSupplierBuilder, BigDecimal> between(BigDecimal minimumInclusiveValue) {
        return maximumExclusiveValue ->
                minimumInclusiveValue(minimumInclusiveValue)
                        .maximumExclusiveValue(maximumExclusiveValue);
    }

    public static BigDecimalSupplierBuilder bigDecimals(){
        return new BigDecimalSupplierBuilder();
    }

}

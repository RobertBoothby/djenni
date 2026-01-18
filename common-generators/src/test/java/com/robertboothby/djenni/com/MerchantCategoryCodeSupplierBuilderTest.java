package com.robertboothby.djenni.com;

import com.robertboothby.djenni.common.MerchantCategoryCode;
import com.robertboothby.djenni.common.MerchantCategoryCodeSupplierBuilder;
import com.robertboothby.djenni.core.StreamableSupplier;
import com.robertboothby.djenni.core.util.Collections;
import com.robertboothby.djenni.matcher.Matchers;
import org.junit.jupiter.api.Test;

import static com.robertboothby.djenni.core.util.Collections.asSet;
import static com.robertboothby.djenni.matcher.Matchers.eventuallySuppliesAllValues;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;

public class MerchantCategoryCodeSupplierBuilderTest {
    @Test
    public void shouldReturnSupplierOfMerchantCategoryCodesWithDefaultedValues(){
        //Given
        StreamableSupplier<MerchantCategoryCode> underTest = MerchantCategoryCodeSupplierBuilder.merchantCategoryCodes();
        //When
        MerchantCategoryCode merchantCategoryCode = underTest.get();

        //Then
        assertThat(merchantCategoryCode, instanceOf(MerchantCategoryCode.class));
    }

    @Test
    public void shouldReturnSupplierOfMerchantCategoryCodesWithSuppliedValues(){
        //Given
        MerchantCategoryCode merchantCategoryCode1 = new MerchantCategoryCode(1, "A", "B", "C", "D", "E");
        MerchantCategoryCode merchantCategoryCode2 = new MerchantCategoryCode(2, "Z", "Y", "X", "W", "V");
        StreamableSupplier<MerchantCategoryCode> underTest = MerchantCategoryCodeSupplierBuilder
                .merchantCategoryCodes( $ -> {
                    $.withMerchantCategoryCodes(merchantCategoryCode1, merchantCategoryCode2);
        });

        //When

        //Then
        assertThat(underTest, eventuallySuppliesAllValues(asSet(merchantCategoryCode1, merchantCategoryCode2), 100));

    }
}

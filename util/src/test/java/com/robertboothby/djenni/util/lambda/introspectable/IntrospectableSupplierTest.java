package com.robertboothby.djenni.util.lambda.introspectable;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

public class IntrospectableSupplierTest {

    @Test
    public void shouldDetectBoundInstanceAccessor() {
        Sample sample = new Sample("VALUE");
        IntrospectableSupplier<String> supplier = sample::value;

        assertThat(supplier.isBoundInstance(), is(true));
        assertThat(supplier.isStaticMethod(), is(false));
        assertThat(supplier.getBoundInstance().orElse(null), is(sample));
    }

    @Test
    public void shouldDetectStaticAccessor() {
        IntrospectableSupplier<String> supplier = Sample::staticValue;

        assertThat(supplier.isBoundInstance(), is(false));
        assertThat(supplier.isStaticMethod(), is(true));
        assertThat(supplier.getBoundInstance().orElse(null), is(nullValue()));
    }

    public record Sample(String value) {
        public static String staticValue() {
            return "STATIC";
        }
    }
}

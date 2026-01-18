package com.robertboothby.djenni.util;

import com.robertboothby.djenni.util.lambda.introspectable.IntrospectableFunction;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNull;

public class RecordCopyHelperTest {

    @Test
    public void shouldCopyWithChange() {
        PersonRecord original = new PersonRecord("Ada", 32);

        PersonRecord updated = RecordCopyHelper.copyWithChange(original, PersonRecord::age, 33);

        assertThat(updated.name(), is("Ada"));
        assertThat(updated.age(), is(33));
    }

    @Test
    public void shouldAllowNullForReferenceComponent() {
        PersonRecord original = new PersonRecord("Ada", 32);

        PersonRecord updated = RecordCopyHelper.copyWithChange(original, PersonRecord::name, null);

        assertNull(updated.name());
        assertThat(updated.age(), is(32));
    }

    @Test
    public void shouldRejectNullForPrimitiveComponent() {
        PersonRecord original = new PersonRecord("Ada", 32);

        assertThrows(IllegalArgumentException.class, () ->
                RecordCopyHelper.copyWithChange(original, PersonRecord::age, null));
    }

    @Test
    public void shouldCopyWithBuilder() {
        PersonRecord original = new PersonRecord("Ada", 32);

        PersonRecord updated = RecordCopyHelper.copyOf(original)
                .with(PersonRecord::name, "Grace")
                .with(PersonRecord::age, 33)
                .build();

        assertThat(updated.name(), is("Grace"));
        assertThat(updated.age(), is(33));
    }

    @Test
    public void shouldRejectAccessorFromDifferentRecord() {
        PersonRecord original = new PersonRecord("Ada", 32);
        @SuppressWarnings("unchecked")
        IntrospectableFunction<PersonRecord, String> accessor = (IntrospectableFunction<PersonRecord, String>) (IntrospectableFunction<?, ?>) (IntrospectableFunction<OtherRecord, String>) OtherRecord::value;

        assertThrows(IllegalArgumentException.class, () -> RecordCopyHelper.copyWithChange(original, accessor, "value"));
    }

    public record PersonRecord(String name, int age) {
    }

    public record OtherRecord(String value) {
    }
}

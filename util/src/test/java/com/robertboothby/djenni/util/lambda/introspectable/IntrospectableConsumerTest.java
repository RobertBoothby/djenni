package com.robertboothby.djenni.util.lambda.introspectable;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicReference;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

public class IntrospectableConsumerTest {

    @Test
    public void shouldDetectBoundInstanceConsumer() {
        AtomicReference<String> sink = new AtomicReference<>();
        IntrospectableConsumer<String> consumer = sink::set;

        assertThat(consumer.isBoundInstance(), is(true));
        assertThat(consumer.isStaticMethod(), is(false));
        assertThat(consumer.getBoundInstance().orElse(null), is(sink));
    }

    @Test
    public void shouldDetectStaticConsumer() {
        IntrospectableConsumer<String> consumer = IntrospectableConsumerTest::consumeStatic;

        assertThat(consumer.isBoundInstance(), is(false));
        assertThat(consumer.isStaticMethod(), is(true));
        assertThat(consumer.getBoundInstance().orElse(null), is(nullValue()));
    }

    private static void consumeStatic(String value) {
        // no-op
    }
}

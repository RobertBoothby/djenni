package com.robertboothby.djenni.util;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;

public class CopyOnWriteConfigurableTest {
    @Test public void shouldFluentlyConfigureCopyOnWriteObject(){
        //Given
        TestCopyOnWriteConfigurable testCopyOnWriteConfigurable = new TestCopyOnWriteConfigurable();

        //When
        testCopyOnWriteConfigurable.configure(tc -> tc.setConfigured(true)).setConfigured2(true);

        //Then
        assertThat(testCopyOnWriteConfigurable.isConfigured(), Matchers.is(true));
        assertThat(testCopyOnWriteConfigurable.isConfigured2(), Matchers.is(true));
    }

    private static TestCopyOnWriteConfigurable preConfigured(){
        return new TestCopyOnWriteConfigurable().configure(tc -> tc.setConfigured(true));
    }

    @Test public void shouldFluentlyConfigureCopyOnWriteObjectWithPreConfigured(){
        //Given
        TestCopyOnWriteConfigurable testCopyOnWriteConfigurable = preConfigured().setConfigured2(true);

        //Then
        assertThat(testCopyOnWriteConfigurable.isConfigured(), Matchers.is(true));
        assertThat(testCopyOnWriteConfigurable.isConfigured2(), Matchers.is(true));
    }


    private static class TestCopyOnWriteConfigurable implements CopyOnWriteConfigurable<TestCopyOnWriteConfigurable> {
        private boolean configured = false;
        private boolean configured2 = false;

        public boolean isConfigured() {
            return configured;
        }

        public boolean isConfigured2() {
            return configured2;
        }

        public TestCopyOnWriteConfigurable setConfigured(boolean configured) {
            this.configured = configured;
            return this;
        }

        public TestCopyOnWriteConfigurable setConfigured2(boolean configured2) {
            this.configured2 = configured2;
            return this;
        }

    }
}

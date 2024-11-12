package com.robertboothby.djenni.util;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ConfigurableTest {

    @Test
    public void shouldConfigureAnObject(){
        //Given
        TestConfigurable testConfigurable = new TestConfigurable();

        //When
        testConfigurable.configure(tc -> tc.setConfigured(true));

        //Then
        assertThat(testConfigurable.isConfigured(), is(true));
    }

    private static class TestConfigurable implements Configurable<TestConfigurable> {
        private boolean configured = false;

        public boolean isConfigured() {
            return configured;
        }

        public void setConfigured(boolean configured) {
            this.configured = configured;
        }
    }
}

package org.jenkinsci.plugins.heisentest;

import org.junit.Test;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class HelloWorldBuilderTest {

    @Test
    public void fakeTest() {
        assertThat("this", equalTo("this"));
    }
}

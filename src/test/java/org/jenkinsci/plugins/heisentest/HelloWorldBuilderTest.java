package org.jenkinsci.plugins.heisentest;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class HelloWorldBuilderTest {

    @Test
    public void fakeTest() {
        assertThat("this", equalTo("this"));
    }
}

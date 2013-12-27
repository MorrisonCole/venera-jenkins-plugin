package org.jenkinsci.plugins.heisentest.converters;

import hudson.tasks.junit.SuiteResult;
import hudson.tasks.junit.TestResult;
import org.easymock.EasyMock;
import org.jenkinsci.plugins.heisentest.results.HeisentestTestResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static java.util.Arrays.asList;
import static org.easymock.EasyMock.*;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;

@RunWith(PowerMockRunner.class)
@PrepareForTest({TestResult.class, SuiteResult.class})
public class JUnitTestResultToHeisentestTestResultConverterTest {

	private static final SuiteResult SUITE_RESULT_1 = EasyMock.createMock(SuiteResult.class);
	private static final SuiteResult SUITE_RESULT_2 = EasyMock.createMock(SuiteResult.class);
	private static final SuiteResult[] SUITES = {SUITE_RESULT_1, SUITE_RESULT_2};

	private final JUnitTestResultToHeisentestTestResultConverter jUnitTestResultToHeisentestTestResultConverter = new JUnitTestResultToHeisentestTestResultConverter();
	private final TestResult testResult = PowerMock.createMock(TestResult.class);

	@Test
	public void maintainsSuites() {
		expect(testResult.getSuites()).andReturn(asList(SUITES));

		assertThat(convertedTestResult().getSuites(), containsInAnyOrder(SUITES));
	}

	private HeisentestTestResult convertedTestResult() {
		replay(testResult);
		final HeisentestTestResult heisentestTestResult = jUnitTestResultToHeisentestTestResultConverter.convert(testResult);
		verify(testResult);

		return heisentestTestResult;
	}
}

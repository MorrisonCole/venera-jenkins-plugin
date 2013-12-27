package org.jenkinsci.plugins.heisentest.converters;

import hudson.tasks.junit.CaseResult;
import org.jenkinsci.plugins.heisentest.results.HeisentestCaseResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(PowerMockRunner.class)
@PrepareForTest(CaseResult.class)
public class JUnitCaseResultToHeisentestCaseResultConverterTest {

	private static final String TEST_NAME = "test_name";
	private static final String CLASS_NAME = "class_name";
	private static final float DURATION = 10f;
	private static final boolean SKIPPED = true;
	private static final String ERROR_STACKTRACE = "error_stacktrace";
	private static final String ERROR_DETAILS = "error details";

	private final JUnitCaseResultToHeisentestCaseResultConverter jUnitCaseResultToHeisentestCaseResultConverter = new JUnitCaseResultToHeisentestCaseResultConverter();
	private final CaseResult caseResult = PowerMock.createNiceMock(CaseResult.class);

	@Test
	public void maintainsDuration() {
		expect(caseResult.getDuration()).andReturn(DURATION);

		assertThat(convertedCaseResult().getDuration(), equalTo(DURATION));
	}

	@Test
	public void maintainsClassName() {
		expect(caseResult.getClassName()).andReturn(CLASS_NAME);

		assertThat(convertedCaseResult().getClassName(), equalTo(CLASS_NAME));
	}

	@Test
	public void maintainsTestName() {
		expect(caseResult.getName()).andReturn(TEST_NAME);

		assertThat(convertedCaseResult().getName(), equalTo(TEST_NAME));
	}

	@Test
	public void maintainsSkipped() {
		expect(caseResult.isSkipped()).andReturn(SKIPPED);

		assertThat(convertedCaseResult().getSkipped(), equalTo(SKIPPED));
	}

	@Test
	public void maintainsErrorStackTrace() {
		expect(caseResult.getErrorStackTrace()).andReturn(ERROR_STACKTRACE);

		assertThat(convertedCaseResult().getErrorStackTrace(), equalTo(ERROR_STACKTRACE));
	}

	@Test
	public void maintainsErrorDetails() {
		expect(caseResult.getErrorDetails()).andReturn(ERROR_DETAILS);

		assertThat(convertedCaseResult().getErrorDetails(), equalTo(ERROR_DETAILS));
	}

	private HeisentestCaseResult convertedCaseResult() {
		replay(caseResult);
		final HeisentestCaseResult heisentestCaseResult = jUnitCaseResultToHeisentestCaseResultConverter.convert(caseResult);
		verify(caseResult);

		return heisentestCaseResult;
	}
}

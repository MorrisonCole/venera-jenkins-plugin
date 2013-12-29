package org.jenkinsci.plugins.heisentest.converters;

import hudson.tasks.junit.CaseResult;
import hudson.tasks.junit.SuiteResult;
import org.easymock.EasyMock;
import org.jenkinsci.plugins.heisentest.results.HeisentestSuiteResult;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static java.util.Arrays.asList;
import static org.easymock.EasyMock.*;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;

@RunWith(PowerMockRunner.class)
@PrepareForTest({SuiteResult.class, CaseResult.class})
public class JUnitSuiteResultToHeisentestSuiteResultConverterTest {

	private static final CaseResult CASE_RESULT_1 = EasyMock.createMock(CaseResult.class);
	private static final CaseResult CASE_RESULT_2 = EasyMock.createMock(CaseResult.class);
	private static final CaseResult[] CASES = {CASE_RESULT_1, CASE_RESULT_2};

//	private final JUnitSuiteResultToHeisentestSuiteResultConverter jUnitSuiteResultToHeisentestSuiteResultConverter = new JUnitSuiteResultToHeisentestSuiteResultConverter();
	private final SuiteResult suiteResult = EasyMock.createNiceMock(SuiteResult.class);

	@Ignore("Not even testing something we want!")
	@Test
	public void maintainsCases() {
		expect(suiteResult.getCases()).andReturn(asList(CASES));

//		assertThat(convertedSuiteResult().getCases(), containsInAnyOrder(CASES));
	}

//	private HeisentestSuiteResult convertedSuiteResult() {
//		replay(suiteResult);
//		final HeisentestSuiteResult heisentestSuiteResult = jUnitSuiteResultToHeisentestSuiteResultConverter.convert(suiteResult);
//		verify(suiteResult);
//
//		return heisentestSuiteResult;
//	}
}

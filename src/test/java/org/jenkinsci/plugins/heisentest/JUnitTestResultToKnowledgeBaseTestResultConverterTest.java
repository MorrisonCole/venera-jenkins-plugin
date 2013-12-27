package org.jenkinsci.plugins.heisentest;

import hudson.tasks.junit.TestResult;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(PowerMockRunner.class)
@PrepareForTest(TestResult.class)
public class JUnitTestResultToKnowledgeBaseTestResultConverterTest {

	private static final int FAIL_COUNT = 4;
	private final JUnitTestResultToKnowledgeBaseTestResultConverter jUnitTestResultToKnowledgeBaseTestResultConverter = new JUnitTestResultToKnowledgeBaseTestResultConverter();
	private TestResult testResult;

	@Before
	public void setUp() throws Exception {
		testResult = PowerMock.createMock(TestResult.class);
	}

	@Test
	public void maintainsFailedTests() {
		expect(testResult.getFailCount()).andReturn(FAIL_COUNT);

		replay(testResult);

		KnowledgeBaseTestResult knowledgeBaseTestResult = jUnitTestResultToKnowledgeBaseTestResultConverter.convert(testResult);

		verify(testResult);

		assertThat(knowledgeBaseTestResult.getFailCount(), equalTo(FAIL_COUNT));
	}
}

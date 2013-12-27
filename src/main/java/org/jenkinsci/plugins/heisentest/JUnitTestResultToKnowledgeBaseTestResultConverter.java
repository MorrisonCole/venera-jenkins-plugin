package org.jenkinsci.plugins.heisentest;

import hudson.tasks.junit.TestResult;

import static org.jenkinsci.plugins.heisentest.KnowledgeBaseTestResult.Builder.knowledgeBaseTestResult;

public class JUnitTestResultToKnowledgeBaseTestResultConverter implements Converter<TestResult, KnowledgeBaseTestResult> {

	public KnowledgeBaseTestResult convert(TestResult testResult) {
		final KnowledgeBaseTestResult knowledgeBaseTestResult = knowledgeBaseTestResult()
				.withFailCount(testResult.getFailCount()).build();
		return knowledgeBaseTestResult;
	}
}

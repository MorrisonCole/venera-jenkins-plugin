package org.jenkinsci.plugins.heisentest.converters;

import hudson.tasks.junit.TestResult;
import org.jenkinsci.plugins.heisentest.results.HeisentestTestResult;

import static org.jenkinsci.plugins.heisentest.results.HeisentestTestResult.Builder.heisentestTestResult;

public class JUnitTestResultToHeisentestTestResultConverter implements Converter<TestResult, HeisentestTestResult> {

	public HeisentestTestResult convert(TestResult testResult) {
		return heisentestTestResult()
				.withSuites(testResult.getSuites())
				.build();
	}
}

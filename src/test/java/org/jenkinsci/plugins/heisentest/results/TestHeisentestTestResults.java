package org.jenkinsci.plugins.heisentest.results;

import static java.util.Arrays.asList;
import static org.jenkinsci.plugins.heisentest.results.HeisentestTestResult.Builder.heisentestTestResult;
import static org.jenkinsci.plugins.heisentest.results.TestHeisentestSuiteResults.aHeisentestSuiteResult;

public class TestHeisentestTestResults {
	public static HeisentestTestResult aHeisentestTestResult() {
		return heisentestTestResult()
				.withSuites(asList(aHeisentestSuiteResult()))
				.build();
	}
}

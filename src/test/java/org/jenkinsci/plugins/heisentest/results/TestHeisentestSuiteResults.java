package org.jenkinsci.plugins.heisentest.results;

import static java.util.Arrays.asList;
import static org.jenkinsci.plugins.heisentest.results.HeisentestSuiteResult.Builder.heisentestSuiteResult;
import static org.jenkinsci.plugins.heisentest.results.TestHeisentestCaseResults.aHeisentestCaseResult;

public class TestHeisentestSuiteResults {
	public static HeisentestSuiteResult aHeisentestSuiteResult() {
		return heisentestSuiteResult()
				.withCases(asList(aHeisentestCaseResult()))
				.build();
	}
}

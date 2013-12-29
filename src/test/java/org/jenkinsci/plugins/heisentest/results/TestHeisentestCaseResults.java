package org.jenkinsci.plugins.heisentest.results;

import static org.jenkinsci.plugins.heisentest.results.HeisentestCaseResult.Builder.heisentestCaseResult;

public class TestHeisentestCaseResults {
	public static HeisentestCaseResult aHeisentestCaseResult() {
		return heisentestCaseResult()
				.withClassName("class_name")
				.withDuration(11.4f)
				.withErrorDetails("error_details")
				.withErrorStackTrace("stack_trace")
				.withName("name")
				.withSkipped(false)
				.build();
	}
}

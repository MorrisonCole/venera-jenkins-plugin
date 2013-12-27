package org.jenkinsci.plugins.heisentest.converters;

import hudson.tasks.junit.SuiteResult;
import org.jenkinsci.plugins.heisentest.results.HeisentestSuiteResult;

import static org.jenkinsci.plugins.heisentest.results.HeisentestSuiteResult.Builder.heisentestSuiteResult;

public class JUnitSuiteResultToHeisentestSuiteResultConverter implements Converter<SuiteResult, HeisentestSuiteResult> {

	public HeisentestSuiteResult convert(SuiteResult suiteResult) {
		return heisentestSuiteResult()
				.withCases(suiteResult.getCases())
				.build();
	}
}

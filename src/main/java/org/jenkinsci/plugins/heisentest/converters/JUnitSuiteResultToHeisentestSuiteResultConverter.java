package org.jenkinsci.plugins.heisentest.converters;

import hudson.tasks.junit.CaseResult;
import hudson.tasks.junit.SuiteResult;
import org.jenkinsci.plugins.heisentest.results.HeisentestCaseResult;
import org.jenkinsci.plugins.heisentest.results.HeisentestSuiteResult;

import java.util.Collection;

import static org.jenkinsci.plugins.heisentest.results.HeisentestSuiteResult.Builder.heisentestSuiteResult;

public class JUnitSuiteResultToHeisentestSuiteResultConverter implements Converter<SuiteResult, HeisentestSuiteResult> {

	private final Converter<Collection<CaseResult>, Collection<HeisentestCaseResult>> jUnitCaseResultToHeisentestCaseResultConverter;

	public JUnitSuiteResultToHeisentestSuiteResultConverter(Converter<Collection<CaseResult>, Collection<HeisentestCaseResult>> jUnitCaseResultToHeisentestCaseResultConverter) {
		this.jUnitCaseResultToHeisentestCaseResultConverter = jUnitCaseResultToHeisentestCaseResultConverter;
	}

	public HeisentestSuiteResult convert(SuiteResult suiteResult) {
		return heisentestSuiteResult()
				.withCases(jUnitCaseResultToHeisentestCaseResultConverter.convert(suiteResult.getCases()))
				.build();
	}
}

package org.jenkinsci.plugins.heisentest.converters;

import hudson.tasks.junit.SuiteResult;
import hudson.tasks.junit.TestResult;
import org.hibernate.annotations.CollectionId;
import org.jenkinsci.plugins.heisentest.results.HeisentestSuiteResult;
import org.jenkinsci.plugins.heisentest.results.HeisentestTestResult;

import java.util.Collection;

import static org.jenkinsci.plugins.heisentest.results.HeisentestTestResult.Builder.heisentestTestResult;

public class JUnitTestResultToHeisentestTestResultConverter implements Converter<TestResult, HeisentestTestResult> {

	private final Converter<Collection<SuiteResult>, Collection<HeisentestSuiteResult>> jUnitSuiteResultToHeisentestSuiteResultConverter;

	public JUnitTestResultToHeisentestTestResultConverter(Converter<Collection<SuiteResult>, Collection<HeisentestSuiteResult>> jUnitSuiteResultToHeisentestSuiteResultConverter) {
		this.jUnitSuiteResultToHeisentestSuiteResultConverter = jUnitSuiteResultToHeisentestSuiteResultConverter;
	}

	public HeisentestTestResult convert(TestResult testResult) {
		return heisentestTestResult()
				.withSuites(jUnitSuiteResultToHeisentestSuiteResultConverter.convert(testResult.getSuites()))
				.build();
	}
}

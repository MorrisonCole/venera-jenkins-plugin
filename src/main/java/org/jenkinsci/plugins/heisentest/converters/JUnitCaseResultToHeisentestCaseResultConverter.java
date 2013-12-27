package org.jenkinsci.plugins.heisentest.converters;

import hudson.tasks.junit.CaseResult;
import org.jenkinsci.plugins.heisentest.results.HeisentestCaseResult;

import static org.jenkinsci.plugins.heisentest.results.HeisentestCaseResult.Builder.heisentestCaseResult;

public class JUnitCaseResultToHeisentestCaseResultConverter implements Converter<CaseResult, HeisentestCaseResult> {

	public HeisentestCaseResult convert(CaseResult caseResult) {
		return heisentestCaseResult()
				.withDuration(caseResult.getDuration())
				.withClassName(caseResult.getClassName())
				.withName(caseResult.getName())
				.withSkipped(caseResult.isSkipped())
				.withErrorStackTrace(caseResult.getErrorStackTrace())
				.withErrorDetails(caseResult.getErrorDetails())
				.build();
	}
}

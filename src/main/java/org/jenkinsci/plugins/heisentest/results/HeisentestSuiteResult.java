package org.jenkinsci.plugins.heisentest.results;

import hudson.tasks.junit.CaseResult;

import java.util.List;

public class HeisentestSuiteResult {

	private Iterable<CaseResult> cases;

	private HeisentestSuiteResult(Builder builder) {
		this.cases = builder.cases;
	}

	public Iterable<CaseResult> getCases() {
		return cases;
	}

	public static class Builder {
		private Iterable<CaseResult> cases;

		public static Builder heisentestSuiteResult() {
			return new Builder();
		}

		public Builder withCases(Iterable<CaseResult> cases) {
			this.cases = cases;
			return this;
		}

		public HeisentestSuiteResult build() {
			return new HeisentestSuiteResult(this);
		}
	}
}

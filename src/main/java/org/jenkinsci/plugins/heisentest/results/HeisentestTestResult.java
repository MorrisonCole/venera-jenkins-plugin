package org.jenkinsci.plugins.heisentest.results;

import hudson.tasks.junit.SuiteResult;

public class HeisentestTestResult {

	private Iterable<SuiteResult> suites;

	private HeisentestTestResult(Builder builder) {
		this.suites = builder.suites;
	}

	public Iterable<SuiteResult> getSuites() {
		return suites;
	}

	public static class Builder {
		private Iterable<SuiteResult> suites;

		public static Builder heisentestTestResult() {
			return new Builder();
		}

		public Builder withSuites(Iterable<SuiteResult> suites) {
			this.suites = suites;
			return this;
		}

		public HeisentestTestResult build() {
			return new HeisentestTestResult(this);
		}
	}
}

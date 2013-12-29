package org.jenkinsci.plugins.heisentest.results;

import javax.persistence.*;
import java.util.Collection;

@Entity
public class HeisentestTestResult {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@OneToMany(cascade = CascadeType.ALL)
	private Collection<HeisentestSuiteResult> suites;

	public HeisentestTestResult() {
	}

	public Collection<HeisentestSuiteResult> getSuites() {
		return suites;
	}

	private HeisentestTestResult(Builder builder) {
		this.suites = builder.suites;
	}

	public static class Builder {
		private Collection<HeisentestSuiteResult> suites;

		public static Builder heisentestTestResult() {
			return new Builder();
		}

		public Builder withSuites(Collection<HeisentestSuiteResult> suites) {
			this.suites = suites;
			return this;
		}

		public HeisentestTestResult build() {
			return new HeisentestTestResult(this);
		}
	}
}

package org.jenkinsci.plugins.heisentest.results;

import javax.persistence.*;
import java.util.Collection;

@Entity
public class HeisentestSuiteResult {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@OneToMany(cascade = CascadeType.ALL)
	private Collection<HeisentestCaseResult> cases;

	public HeisentestSuiteResult() {
	}

	private HeisentestSuiteResult(Builder builder) {
		this.cases = builder.cases;
	}

	public Collection<HeisentestCaseResult> getCases() {
		return cases;
	}

	public static class Builder {
		private Collection<HeisentestCaseResult> cases;

		public static Builder heisentestSuiteResult() {
			return new Builder();
		}

		public Builder withCases(Collection<HeisentestCaseResult> cases) {
			this.cases = cases;
			return this;
		}

		public HeisentestSuiteResult build() {
			return new HeisentestSuiteResult(this);
		}
	}
}

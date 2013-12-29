package org.jenkinsci.plugins.heisentest.results;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class HeisentestCaseResult {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	private String name;
	private String className;
	private float duration;
	private boolean skipped;
	private String errorStackTrace;
	private String errorDetails;

	public HeisentestCaseResult() {
	}

	private HeisentestCaseResult(Builder builder) {
		this.name = builder.name;
		this.className = builder.className;
		this.duration = builder.duration;
		this.skipped = builder.skipped;
		this.errorStackTrace = builder.errorStackTrace;
		this.errorDetails = builder.errorDetails;
	}

	public String getName() {
		return name;
	}

	public String getClassName() {
		return className;
	}

	public float getDuration() {
		return duration;
	}

	public boolean getSkipped() {
		return skipped;
	}

	public String getErrorStackTrace() {
		return errorStackTrace;
	}

	public String getErrorDetails() {
		return errorDetails;
	}

	public static class Builder {
		private String name;
		private String className;
		private float duration;
		private boolean skipped;
		private String errorStackTrace;
		private String errorDetails;

		public static Builder heisentestCaseResult() {
			return new Builder();
		}

		public Builder withName(String name) {
			this.name = name;
			return this;
		}

		public Builder withClassName(String className) {
			this.className = className;
			return this;
		}

		public Builder withDuration(float duration) {
			this.duration = duration;
			return this;
		}

		public Builder withSkipped(boolean skipped) {
			this.skipped = skipped;
			return this;
		}

		public Builder withErrorStackTrace(String errorStackTrace) {
			this.errorStackTrace = errorStackTrace;
			return this;
		}

		public Builder withErrorDetails(String errorDetails) {
			this.errorDetails = errorDetails;
			return this;
		}

		public HeisentestCaseResult build() {
			return new HeisentestCaseResult(this);
		}
	}
}

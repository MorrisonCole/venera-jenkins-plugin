package org.jenkinsci.plugins.heisentest;

public class KnowledgeBaseTestResult {

	private int failCount;

	private KnowledgeBaseTestResult(Builder builder) {
		this.failCount = builder.failCount;
	}

	public int getFailCount() {
		return failCount;
	}

	public static class Builder {
		private int failCount;

		public static Builder knowledgeBaseTestResult() {
			return new Builder();
		}

		public Builder withFailCount(int failCount) {
			this.failCount = failCount;
			return this;
		}

		public KnowledgeBaseTestResult build() {
			return new KnowledgeBaseTestResult(this);
		}
	}
}

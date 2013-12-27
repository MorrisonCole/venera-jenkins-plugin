package org.jenkinsci.plugins.heisentest.database;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;

public class HsqlDatabaseTest {

	private HsqlDatabase hsqlDatabase;

	@Before
	public void setUp() throws Exception {
		hsqlDatabase = new HsqlDatabase("jdbc:hsqldb:mem:unit_test");
		// Should really have a JAR with a test database that we can query. Shouldn't be generating the same data every time!
		hsqlDatabase.update("CREATE TABLE test_results (test_result_column OTHER)");
	}

	@After
	public void tearDown() throws Exception {
		hsqlDatabase.shutdown();
	}

	@Test
	public void getsHeisentestTestResult() throws SQLException {

	}
}

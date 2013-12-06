package org.jenkinsci.plugins.heisentest;

import java.sql.*;

public class HsqlDatabase {

    private static final String JDBC_HSQLDB_URL_PREFIX = "jdbc:hsqldb:";
    private static final String USER = "Heisentest";
    private static final String PASSWORD = "";
    private static final int ERROR_RESULT_CODE = -1;

    Connection connection;

    public HsqlDatabase(String dbFileName) throws Exception {
        DriverManager.registerDriver(new org.hsqldb.jdbcDriver());

        connection = DriverManager.getConnection(JDBC_HSQLDB_URL_PREFIX + dbFileName,
                                                 USER,
                                                 PASSWORD);
    }

    public void shutdown() throws SQLException {
        Statement statement = createStatement();

        // db writes out to files and performs clean shuts down
        // otherwise there will be an unclean shutdown
        // when program ends
        statement.execute("SHUTDOWN");

        if (connectionIsActive()) {
            connection.close();
        }
    }

    private boolean connectionIsActive() throws SQLException {
        return connection.isClosed();
    }

    // Use for SQL command SELECT
    public synchronized void query(String expression) throws SQLException {
        Statement statement = createStatement();

        ResultSet resultSet = statement.executeQuery(expression);

        // do something with the result set.
        dump(resultSet);
        statement.close();

        // NOTE!! if you close a statement the associated ResultSet is
        // closed too
        // so you should copy the contents to some other object.
        // the result set is invalidated also  if you recycle an Statement
        // and try to execute some other query before the result set has been
        // completely examined.
    }

    private Statement createStatement() throws SQLException {
        return connection.createStatement();
    }

    // Use for SQL commands CREATE, DROP, INSERT and UPDATE
    public synchronized void update(String expression) throws SQLException {
        Statement statement = createStatement();

        int resultCode = statement.executeUpdate(expression);

        if (resultCode == ERROR_RESULT_CODE) {
            System.out.println("db error : " + expression);
        }

        statement.close();
    }

    public static void dump(ResultSet rs) throws SQLException {

        // the order of the rows in a cursor
        // are implementation dependent unless you use the SQL ORDER statement
        ResultSetMetaData meta = rs.getMetaData();
        int colmax = meta.getColumnCount();
        int i;
        Object o = null;

        // the result set is a cursor into the data.  You can only
        // point to one row at a time
        // assume we are pointing to BEFORE the first row
        // rs.next() points to next row and returns true
        // or false if there is no next row, which breaks the loop
        for (; rs.next(); ) {
            for (i = 0; i < colmax; ++i) {
                o = rs.getObject(i + 1);    // Is SQL the first column is indexed

                // with 1 not 0
                System.out.print(o.toString() + " ");
            }

            System.out.println(" ");
        }
    }
}
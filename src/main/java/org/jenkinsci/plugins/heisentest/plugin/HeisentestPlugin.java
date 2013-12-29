package org.jenkinsci.plugins.heisentest.plugin;

import hudson.Plugin;
import org.jenkinsci.plugins.heisentest.persistence.HsqlDatabase;
import weka.core.FastVector;
import weka.core.Instances;
import weka.core.converters.DatabaseSaver;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HeisentestPlugin extends Plugin {

    private static final Logger logger = Logger.getLogger(HeisentestPlugin.class.getName());

    @Override
    public void start() throws Exception {
        super.start();

        logger.log(Level.INFO, "Starting up Heisentest plugin...");

        HsqlDatabase hsqlDatabase;

        try {
            hsqlDatabase = new HsqlDatabase();
        } catch (Exception exception) {
            logger.log(Level.SEVERE, "Exception was: ", exception);
            return;
        }

        try {
            hsqlDatabase.update("CREATE TABLE historical_test_results ( id INTEGER IDENTITY, str_col VARCHAR(256), num_col INTEGER)");
        } catch (SQLException ex2) {
            //ignore
            //ex2.printStackTrace();  // second time we run program
            //  should throw execption since table
            // already there
            //
            // this will have no effect on the hsqlDatabase
        }

        try {
            // add some rows - will create duplicates if run more then once
            // the id column is automatically generated
            hsqlDatabase.update(
                    "INSERT INTO sample_table(str_col,num_col) VALUES('Ford', 100)");
            hsqlDatabase.update(
                    "INSERT INTO sample_table(str_col,num_col) VALUES('Toyota', 200)");
            hsqlDatabase.update(
                    "INSERT INTO sample_table(str_col,num_col) VALUES('Honda', 300)");
            hsqlDatabase.update(
                    "INSERT INTO sample_table(str_col,num_col) VALUES('GM', 400)");

            // do a query
            hsqlDatabase.query("SELECT * FROM sample_table WHERE num_col < 250");

            // at end of program
            hsqlDatabase.shutdown();
        } catch (SQLException ex3) {
            ex3.printStackTrace();
        }

        Instances data = new Instances("test", new FastVector(1), 1);

        System.out.println("\nSaving data...");
        DatabaseSaver saver = new DatabaseSaver();

        saver.setDestination("heisentestDb/database", "heisentest", "");
        // we explicitly specify the table name here:
        saver.setTableName("sample_table");
        saver.setRelationForTableName(false);
        // or we could just update the name of the dataset:
        // saver.setRelationForTableName(true);
        // data.setRelationName("whatsoever2");
        saver.setInstances(data);
        saver.writeBatch();
    }
}

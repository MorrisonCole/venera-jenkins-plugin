package org.jenkinsci.plugins.heisentest;

import hudson.Plugin;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HeisentestPlugin extends Plugin {

    private static final Logger logger = Logger.getLogger(HeisentestPlugin.class.getName());

    @Override
    public void start() throws Exception {
        super.start();

        logger.log(Level.INFO, "Starting up Heisentest plugin...");

        // TODO: Load settings from a configuration file.

        HsqlDatabase db = null;

        try {
            db = new HsqlDatabase("heisentestDb");
        } catch (Exception ex1) {
            ex1.printStackTrace();    // could not start db

            return;                   // bye bye
        }

        try {
            //make an empty table
            //
            // by declaring the id column IDENTITY, the db will automatically
            // generate unique values for new rows- useful for row keys
            db.update(
                    "CREATE TABLE sample_table ( id INTEGER IDENTITY, str_col VARCHAR(256), num_col INTEGER)");
        } catch (SQLException ex2) {

            //ignore
            //ex2.printStackTrace();  // second time we run program
            //  should throw execption since table
            // already there
            //
            // this will have no effect on the db
        }

        try {

            // add some rows - will create duplicates if run more then once
            // the id column is automatically generated
            db.update(
                    "INSERT INTO sample_table(str_col,num_col) VALUES('Ford', 100)");
            db.update(
                    "INSERT INTO sample_table(str_col,num_col) VALUES('Toyota', 200)");
            db.update(
                    "INSERT INTO sample_table(str_col,num_col) VALUES('Honda', 300)");
            db.update(
                    "INSERT INTO sample_table(str_col,num_col) VALUES('GM', 400)");

            // do a query
            db.query("SELECT * FROM sample_table WHERE num_col < 250");

            logger.log(Level.INFO, "Shutting down DB");

            // at end of program
            db.shutdown();
        } catch (SQLException ex3) {
            ex3.printStackTrace();
        }

//        Instances data = new Instances("test", new FastVector(1), 1);
//
//        System.out.println("\nSaving data...");
//        DatabaseSaver saver = new DatabaseSaver();
//
//        saver.setDestination("jdbc_url", "the_user", "the_password");
//        // we explicitly specify the table name here:
//        saver.setTableName("whatsoever2");
//        saver.setRelationForTableName(false);
//        // or we could just update the name of the dataset:
//        // saver.setRelationForTableName(true);
//        // data.setRelationName("whatsoever2");
//        saver.setInstances(data);
//        saver.writeBatch();
    }
}

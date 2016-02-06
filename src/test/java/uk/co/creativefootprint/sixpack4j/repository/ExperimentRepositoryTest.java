package uk.co.creativefootprint.sixpack4j.repository;

import org.junit.Before;
import org.junit.Test;
import uk.co.creativefootprint.sixpack4j.model.Alternative;
import uk.co.creativefootprint.sixpack4j.model.Experiment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class ExperimentRepositoryTest {

    ExperimentRepository repository;

    private static String DB_DRIVER="org.h2.Driver";
    private static String DB_CONNECTION="jdbc:h2:~/sixpack-test";
    private static String DB_USER="";
    private static String DB_PASSWORD="";

    @Before
    public void before() throws SQLException {

        repository = new ExperimentRepository(DB_DRIVER, DB_CONNECTION, DB_USER, DB_PASSWORD);
        deleteDb();
        repository.createDb("db.sql");
    }

    @Test
    public void getExperiment(){

        //arrange
        Experiment experiment = new Experiment("example experiment",
                Arrays.asList(
                        new Alternative("a"),
                        new Alternative("b")
                ));
        experiment.setDescription("my description");

        repository.create(experiment);

        //act
        Experiment result = repository.get("example experiment");

        //assert
        assertThat(result.getName(), is("example experiment"));
        assertThat(result.getDescription(), is("my description"));
    }

    private void deleteDb() {
        Connection dbConnection = null;
        try {
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        try {
            dbConnection = DriverManager.getConnection(DB_CONNECTION, DB_USER,
                    DB_PASSWORD);
            PreparedStatement s = dbConnection.prepareStatement("DROP ALL OBJECTS");
            s.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
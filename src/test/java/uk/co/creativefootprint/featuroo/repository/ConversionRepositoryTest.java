package uk.co.creativefootprint.featuroo.repository;

import org.junit.Before;
import org.junit.Test;
import uk.co.creativefootprint.featuroo.model.Alternative;
import uk.co.creativefootprint.featuroo.model.Client;
import uk.co.creativefootprint.featuroo.model.Experiment;
import uk.co.creativefootprint.featuroo.model.Goal;

import java.sql.SQLException;
import java.util.Date;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ConversionRepositoryTest {

    ConversionRepository repository;

    private static String DB_DRIVER="org.h2.Driver";
    private static String DB_CONNECTION="jdbc:h2:~/featuroo-test";
    private static String DB_USER="";
    private static String DB_PASSWORD="";

    private Experiment existingExperiment;

    @Before
    public void before() throws SQLException {

        repository = new ConversionRepository(DB_DRIVER, DB_CONNECTION, DB_USER, DB_PASSWORD);
        SqlTestHelper.resetDb();
        setupDb();
    }

    private void setupDb(){
        //arrange
        existingExperiment = new Experiment("example experiment",
                Arrays.asList(
                        new Alternative("a"),
                        new Alternative("b")
                ));

        ExperimentRepository e = new ExperimentRepository(DB_DRIVER, DB_CONNECTION, DB_USER, DB_PASSWORD);
        e.create(existingExperiment);
    }
    
    @Test
    public void convert() {

        Client client = new Client("my client");
        repository.convert(existingExperiment, client, new Goal("goal1"), new Date());
    }

    @Test
    public void convertTwiceSameGoal() {

        Client client = new Client("my client");
        repository.convert(existingExperiment, client, new Goal("goal1"), new Date());
        repository.convert(existingExperiment, client, new Goal("goal1"), new Date());
    }

    @Test
    public void convertTwiceDifferentGoal() {

        Client client = new Client("my client");
        repository.convert(existingExperiment, client, new Goal("goal1"), new Date());
        repository.convert(existingExperiment, client, new Goal("goal2"), new Date());
    }
}
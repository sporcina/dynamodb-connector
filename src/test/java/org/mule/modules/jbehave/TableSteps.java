package org.mule.modules.jbehave;

import junit.framework.Assert;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.core.embedder.Embedder;
import org.mule.modules.RepositoryFlows;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * Specification-to-methods map for AWS DynamoDB table behaviours
 */
@Configuration
public class TableSteps extends Embedder {

    //TestProperties testProperties = TestProperties.getInstance();

    @Given("I have DynamoDB credentials")
    public void haveDynamoDBCredentials() {
        /*
            TODO:  Need to add other means for users to pass credentials
            For the purpose of these tests, the credentials are stored inside the file "AwsCredentials.properties".
            The AWS SDK does provide other means of acquiring the credentials.
         */
    }

    @When("I request a new repository")
    public void requestNewRepository() {
        try {
            RepositoryFlows repositoryFlows = new RepositoryFlows();
            repositoryFlows.create();
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Failed to request new repository: " + e.getCause() + ", " + e.getMessage());
        }
    }

    @Given("I have a repository that is $state")
    @Then("I have a repository that is $state")
    public void haveRepository(String state) throws IOException {
        try {
            RepositoryFlows repositoryFlows = new RepositoryFlows();
            repositoryFlows.stateShouldBe(state);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Failed to determine if the repository is " + state + ": "+ e.getCause() + ", " + e.getMessage());
        }
    }
}

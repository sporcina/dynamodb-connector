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

    @When("I request to delete the repository")
    public void deleteRepository() {
        try {
            RepositoryFlows repositoryFlows = new RepositoryFlows();
            repositoryFlows.delete();
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Failed to delete the repository: " + e.getCause() + ", " + e.getMessage());
        }
    }

    @Then("the repository does not exist")
    public void repositoryDoesNotExist() {
        try {
            RepositoryFlows repositoryFlows = new RepositoryFlows();
            // TODO: I would rather not include AWS-specific items in a steps/behaviour file.  It should be lower level - sporcina (Oct.13,2013)
            repositoryFlows.stateShouldBe("ResourceNotFoundException");
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Failed to confirm that the repository was deleted: " + e.getCause() + ", " + e.getMessage());
        }
    }
}

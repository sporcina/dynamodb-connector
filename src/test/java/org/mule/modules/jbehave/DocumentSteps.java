package org.mule.modules.jbehave;

import com.rits.cloning.Cloner;
import junit.framework.Assert;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.core.embedder.Embedder;
import org.mule.modules.DocumentFlows;
import org.mule.modules.samples.FakeCustomer;
import org.mule.modules.tools.Conditions;
import org.springframework.context.annotation.Configuration;

/**
 * Specification-to-methods map for AWS DynamoDB document behaviours
 */
@Configuration
public class DocumentSteps extends Embedder {

    private FakeCustomer document;

    public DocumentSteps(FakeCustomer document) {
        this.document = document;
    }

    @Given("I have a saved document")
    @When("I save a document")
    public void saveDocument() {
        DocumentFlows documentFlows = new DocumentFlows();
        try {
            this.document = (FakeCustomer) documentFlows.shouldSaveDocument();
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Failed to save a document: " + e.getCause() + ", " + e.getMessage());
        }

    }

    @Then("the document is saved in the repository")
    public void documentIsSaved() {
        FakeCustomer fakeCustomerWithOnlyNum = new FakeCustomer();
        fakeCustomerWithOnlyNum.setNum(document.getNum());
        try {
            DocumentFlows documentFlows = new DocumentFlows();
            FakeCustomer response = (FakeCustomer) documentFlows.shouldGetDocument(fakeCustomerWithOnlyNum);
            new Conditions().expect(document).includeTheHashKey().verify(response);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Failed to get document " + document.getNum() + ": " + e.getCause() + ", " + e.getMessage());
        }
    }

    @When("I update the document")
    public void updateTheDocument() {

    }

    @Then("the document is updated in the repository")
    public void documentIsUpdated() {
        Cloner cloner = new Cloner();
        FakeCustomer fakeCustomerClone = cloner.deepClone(document);
        new Conditions().expect(document).includeTheHashKey().verify(fakeCustomerClone);

        fakeCustomerClone.setName("My New Name");

        try {
            DocumentFlows documentFlows = new DocumentFlows();
            documentFlows.shouldUpdateDocument(fakeCustomerClone);

            // Updating a document through the AWS mapper at this time does not return the modified document.  We need
            // to get the document from the repository and verify it.
            FakeCustomer response = (FakeCustomer) documentFlows.shouldGetDocument(fakeCustomerClone);
            new Conditions().expect(fakeCustomerClone).includeTheHashKey().verify(response);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Failed to update document " + document.getNum() + ": " + e.getCause() + ", " + e.getMessage());
        }
    }
}

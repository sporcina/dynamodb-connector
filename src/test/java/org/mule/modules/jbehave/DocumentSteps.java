package org.mule.modules.jbehave;

import junit.framework.Assert;
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
}

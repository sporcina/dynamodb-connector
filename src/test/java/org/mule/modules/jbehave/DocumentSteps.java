package org.mule.modules.jbehave;

import com.rits.cloning.Cloner;
import junit.framework.Assert;
import org.apache.commons.collections.CollectionUtils;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.core.embedder.Embedder;
import org.mule.modules.DocumentFlows;
import org.mule.modules.samples.FakeCustomer;
import org.mule.modules.tools.Conditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Specification-to-methods map for AWS DynamoDB document behaviours
 */
@Configuration
public class DocumentSteps extends Embedder {

    private static final Logger logger = LoggerFactory.getLogger(DocumentSteps.class);

    private FakeCustomer document;
    private FakeCustomer updatedDocument;
    private List<FakeCustomer> documents;

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
            String msg = "Failed to save a document: " + e.getCause() + ", " + e.getMessage();
            Assert.fail(msg);
            logger.error(msg);
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
            String msg = "Failed to get document " + document.getNum() + ": " + e.getCause() + ", " + e.getMessage();
            Assert.fail(msg);
            logger.error(msg);
        }
    }

    @When("I update the document")
    public void updateTheDocument() {
        Cloner cloner = new Cloner();
        updatedDocument = cloner.deepClone(document);
        new Conditions().expect(document).includeTheHashKey().verify(updatedDocument);

        updatedDocument.setName("My New Name");

        try {
            DocumentFlows documentFlows = new DocumentFlows();
            documentFlows.shouldUpdateDocument(updatedDocument);
        } catch (Exception e) {
            String msg = "Failed to update document " + updatedDocument.getNum() + ": " + e.getCause() + ", " + e.getMessage();
            logger.error(msg);
            Assert.fail(msg);
        }
    }

    @Then("the document is updated in the repository")
    public void documentIsUpdated() {
        try {
            DocumentFlows documentFlows = new DocumentFlows();

            // Updating a document through the AWS mapper at this time does not return the modified document.  We need
            // to get the document from the repository and verify it.
            FakeCustomer response = (FakeCustomer) documentFlows.shouldGetDocument(updatedDocument);
            new Conditions().expect(updatedDocument).includeTheHashKey().verify(response);
        } catch (Exception e) {
            String msg = "Failed to update document " + updatedDocument.getNum() + ": " + e.getCause() + ", " + e.getMessage();
            logger.error(msg);
            Assert.fail(msg);
        }
    }

    @When("I get all documents")
    public void getAllDocuments() {
        try {
            DocumentFlows documentFlows = new DocumentFlows();
            documents = (List<FakeCustomer>)documentFlows.shouldGetAllDocuments(document);
        } catch (Exception e) {
            String msg = "Failed to get all documents: " + e.getCause() + ", " + e.getMessage();
            logger.error(msg);
            Assert.fail(msg);
        }
    }

    @Then("all documents are returned from the repository")
    public void allDocumentsAreReturned() {
        Assert.assertTrue("Number of documents should be greater than zero.", CollectionUtils.isNotEmpty(documents));
    }


    @When("I delete the document")
    public void deleteTheDocument() {
        try {
            DocumentFlows documentFlows = new DocumentFlows();
            documentFlows.shouldDeleteDocument(document);
        } catch (Exception e) {
            String msg = "Failed to update document " + document.getNum() + ": " + e.getCause() + ", " + e.getMessage();
            logger.error(msg);
            Assert.fail(msg);
        }
    }

    @Then("the document is deleted in the repository")
    public void documentIsDeleted() {
        try {
            DocumentFlows documentFlows = new DocumentFlows();

            // Updating a document through the AWS mapper at this time does not return the modified document.  We need
            // to get the document from the repository and verify it.
            Object response = documentFlows.shouldGetDocument(document);
            Assert.assertTrue("A document should not be returned", response == null);
        } catch (Exception e) {
            String msg = "Failed to confirm that document #" + document.getNum() + " was deleted: " + e.getCause() + ", " + e.getMessage();
            logger.error(msg);
            Assert.fail(msg);
        }
    }

    @When("I delete all the documents")
    public void deleteAllDocuments() {
        try {
            DocumentFlows documentFlows = new DocumentFlows();
            documentFlows.shouldDeleteAllDocuments();
        } catch (Exception e) {
            String msg = "Failed to delete all documents: " + e.getCause() + ", " + e.getMessage();
            logger.error(msg);
            Assert.fail(msg);
        }
    }

    @Then("there are no documents in the repository")
    public void noDocumentsAreInTheRepository() {
        getAllDocuments();
        Assert.assertTrue("Should have returned no documents", CollectionUtils.isEmpty(documents));
    }
}

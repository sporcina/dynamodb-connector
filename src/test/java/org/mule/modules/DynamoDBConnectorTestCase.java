package org.mule.modules;


import org.junit.Test;
import org.mule.modules.jbehave.DocumentSteps;
import org.mule.modules.jbehave.TableSteps;
import org.mule.modules.stubs.FakeCustomer;


public class DynamoDBConnectorTestCase {

    @Test
    public void shouldCreateDynamoDBRepository() {
        TableSteps tableSteps = new TableSteps();
        tableSteps.requestNewRepository();
        tableSteps.haveRepository("ACTIVE");
    }


    @Test
    public void shouldSaveDocument() {
        TableSteps tableSteps = new TableSteps();
        DocumentSteps documentSteps = new DocumentSteps(new FakeCustomer());

        tableSteps.haveRepository("ACTIVE");
        documentSteps.saveDocument();
        documentSteps.documentIsSaved();
    }


    @Test
    public void shouldUpdateDocument() {
        TableSteps tableSteps = new TableSteps();
        DocumentSteps documentSteps = new DocumentSteps(new FakeCustomer());

        tableSteps.haveRepository("ACTIVE");
        documentSteps.saveDocument();
        documentSteps.documentIsSaved();
        documentSteps.updateTheDocument();
        documentSteps.documentIsUpdated();
    }


    @Test
    public void shouldRetrieveAllDocuments() {
        TableSteps tableSteps = new TableSteps();
        DocumentSteps documentSteps = new DocumentSteps(new FakeCustomer());

        tableSteps.haveRepository("ACTIVE");
        documentSteps.saveDocument();
        documentSteps.documentIsSaved();
        documentSteps.getAllDocuments();
        documentSteps.allDocumentsAreReturned();
    }


    @Test
    public void shouldDeleteDocument() {
        TableSteps tableSteps = new TableSteps();
        DocumentSteps documentSteps = new DocumentSteps(new FakeCustomer());

        tableSteps.haveRepository("ACTIVE");
        documentSteps.saveDocument();
        documentSteps.deleteTheDocument();
        documentSteps.documentIsDeleted();
    }


    @Test
    public void shouldDeleteAllDocuments() {
        TableSteps tableSteps = new TableSteps();
        DocumentSteps documentSteps = new DocumentSteps(new FakeCustomer());

        tableSteps.haveRepository("ACTIVE");
        documentSteps.saveDocument(); // TODO: should update this to do multiple documents - sporcina (Dec.17/2013)
        documentSteps.deleteAllDocuments();
        documentSteps.noDocumentsAreInTheRepository();
    }


    @Test
    public void shouldDeleteTheRepository() {
        TableSteps tableSteps = new TableSteps();
        tableSteps.haveRepository("ACTIVE");
        tableSteps.deleteRepository();
        tableSteps.repositoryDoesNotExist();
    }
}

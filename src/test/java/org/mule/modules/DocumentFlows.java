package org.mule.modules;

import org.junit.Assert;
import org.mule.modules.samples.FakeCustomer;
import org.mule.modules.tools.DataFactory;
import org.mule.modules.tools.FlowHelper;

import java.util.ArrayList;
import java.util.List;


public class DocumentFlows {

    public Object shouldSaveDocument() {
        FakeCustomer fakeCustomer = DataFactory.createFakeCustomer();
        FakeCustomer payload = DataFactory.createFakeCustomer();

        Assert.assertTrue("The expected and payload objects must equal each other for this test", fakeCustomer.equals(payload));

        FlowHelper flowHelper = new FlowHelper().run("Should_Save_Document").withPayloadNew(payload).expecting(new FakeCustomer());
        return flowHelper.getResponse().getResponse();
    }


    public Object shouldGetDocument(FakeCustomer fakeCustomer) {
        FlowHelper flowHelper = new FlowHelper().run("Should_Get_Document").withPayloadNew(fakeCustomer).expecting(new FakeCustomer());
        return flowHelper.getResponse().getResponse();
    }


    public Object shouldUpdateDocument(FakeCustomer fakeCustomer) {
        FlowHelper flowHelper = new FlowHelper().run("Should_Update_Document").withPayloadNew(fakeCustomer).expecting(new FakeCustomer());
        return flowHelper.getResponse().getResponse();
    }


    public void shouldDeleteDocument(FakeCustomer fakeCustomer) {
        new FlowHelper().run("Should_Delete_Document").withPayloadNew(fakeCustomer).expecting(new FakeCustomer());
    }


    public void shouldDeleteAllDocuments() {
        new FlowHelper().run("Should_Delete_All_Documents").withPayloadNew(new FakeCustomer()).expecting(new FakeCustomer());
    }


    public <T> Object shouldGetAllDocuments(T payload) {
        List<T> list = new ArrayList<T>();
        FlowHelper flowHelper = new FlowHelper().run("Should_Get_All_Documents").withPayloadNew(payload).expecting(list);
        // TODO: need to change these calls to avoid a long tail and identical names - sporcina (Oct.12,2013)
        return flowHelper.getResponse().getResponse();
    }
}

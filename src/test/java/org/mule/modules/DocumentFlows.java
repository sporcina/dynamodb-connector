package org.mule.modules;

import org.mule.modules.samples.FakeCustomer;
import org.mule.modules.tools.CustomerFactory;
import org.mule.modules.tools.FlowHelper;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;


public class DocumentFlows {

    @NotNull
    public Object shouldSaveDocument() {
        FakeCustomer payload = CustomerFactory.createFakeCustomer();
        return FlowHelper.run("Should_Save_Document")
                .withPayload(payload)
                .expectingType(new FakeCustomer())
                .execute();
    }


    @NotNull
    public Object shouldGetDocument(@NotNull FakeCustomer fakeCustomer) {
        return FlowHelper.run("Should_Get_Document")
                .withPayload(fakeCustomer)
                .expectingType(new FakeCustomer())
                .execute();
    }


    @NotNull
    public Object shouldUpdateDocument(@NotNull FakeCustomer fakeCustomer) {
        return FlowHelper.run("Should_Update_Document")
                .withPayload(fakeCustomer)
                .expectingType(new FakeCustomer())
                .execute();
    }


    public void shouldDeleteDocument(@NotNull FakeCustomer fakeCustomer) {
        FlowHelper.run("Should_Delete_Document")
                .withPayload(fakeCustomer)
                .expectingType(new FakeCustomer());
    }


    public void shouldDeleteAllDocuments() {
        FlowHelper.run("Should_Delete_All_Documents")
                .withPayload(new FakeCustomer())
                .expectingType(new FakeCustomer());
    }


    @NotNull
    public <T> Object shouldGetAllDocuments(@NotNull T payload) {
        List<T> list = new ArrayList<T>();
        return FlowHelper.run("Should_Get_All_Documents")
                .withPayload(payload)
                .expectingType(list)
                .execute();
    }
}

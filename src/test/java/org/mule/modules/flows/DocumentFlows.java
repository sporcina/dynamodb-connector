package org.mule.modules.flows;

import org.mule.modules.stubs.FakeCustomer;
import org.mule.modules.tools.CustomerFactory;
import org.mule.modules.tools.FlowBuilder;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;


public class DocumentFlows {

    @NotNull
    public Object shouldSaveDocument() {
        FakeCustomer payload = CustomerFactory.createFakeCustomer();
        return FlowBuilder.run("Should_Save_Document")
                .withPayload(payload)
                .expectingType(new FakeCustomer())
                .execute();
    }


    @NotNull
    public Object shouldGetDocument(@NotNull FakeCustomer fakeCustomer) {
        return FlowBuilder.run("Should_Get_Document")
                .withPayload(fakeCustomer)
                .expectingType(new FakeCustomer())
                .execute();
    }


    @NotNull
    public Object shouldUpdateDocument(@NotNull FakeCustomer fakeCustomer) {
        return FlowBuilder.run("Should_Update_Document")
                .withPayload(fakeCustomer)
                .expectingType(new FakeCustomer())
                .execute();
    }


    public void shouldDeleteDocument(@NotNull FakeCustomer fakeCustomer) {
        FlowBuilder.run("Should_Delete_Document")
                .withPayload(fakeCustomer)
                .expectingType(new FakeCustomer());
    }


    public void shouldDeleteAllDocuments() {
        FlowBuilder.run("Should_Delete_All_Documents")
                .withPayload(new FakeCustomer())
                .expectingType(new FakeCustomer());
    }


    @NotNull
    public <T> Object shouldGetAllDocuments(@NotNull T payload) {
        List<T> list = new ArrayList<T>();
        return FlowBuilder.run("Should_Get_All_Documents")
                .withPayload(payload)
                .expectingType(list)
                .execute();
    }
}

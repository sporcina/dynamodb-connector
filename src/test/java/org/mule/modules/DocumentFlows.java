package org.mule.modules;

import org.junit.Assert;
import org.junit.Test;
import org.mule.api.MuleEvent;
import org.mule.construct.Flow;
import org.mule.modules.samples.FakeCustomer;
import org.mule.modules.tools.Conditions;
import org.mule.modules.tools.DataFactory;
import org.mule.modules.tools.FlowHelper;
import org.mule.tck.junit4.AbstractMuleTestCase;
import org.mule.tck.junit4.FunctionalTestCase;

import java.util.Properties;


public class DocumentFlows {

    public Object shouldSaveDocument() throws Exception {
        FakeCustomer fakeCustomer = DataFactory.createFakeCustomer();
        FakeCustomer payload = DataFactory.createFakeCustomer();

        Assert.assertTrue("The expected and payload objects must equal each other for this test", fakeCustomer.equals(payload));

        FlowHelper flowHelper = new FlowHelper().run("Should_Save_Document").withPayload(payload);
        return flowHelper.getFlowResponsePayload();
    }


    public Object shouldGetDocument(FakeCustomer fakeCustomer) throws Exception {
        FlowHelper flowHelper = new FlowHelper().run("Should_Get_Document").withPayload(fakeCustomer);
        return flowHelper.getFlowResponsePayload();
    }

    public Object shouldUpdateDocument(FakeCustomer fakeCustomer) throws Exception {
        FlowHelper flowHelper = new FlowHelper().run("Should_Update_Document").withPayload(fakeCustomer);
        return flowHelper.getFlowResponsePayload();
    }
}

package org.mule.modules.tools;

import org.junit.Assert;
import org.mule.api.MuleEvent;
import org.mule.construct.Flow;
import org.mule.modules.samples.FakeCustomer;
import org.mule.tck.junit4.FunctionalTestCase;

/**
 * Created with IntelliJ IDEA.
 * User: sporcina
 * Date: 9/23/13
 * Time: 10:10 AM
 * To change this template use File | Settings | File Templates.
 */
public class FlowHelper extends FunctionalTestCase {

    private String flowName;
    private Object flowResponsePayload;
    private Object responsePayload;

    public FlowHelper() {
        try {
            setUpMuleContext();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public FlowHelper run(String flowName) {
        this.flowName = flowName;
        return this;
    }

    public MuleEvent withoutPayload() {
        try {
            return runFlowAndExpect(flowName);
        } catch (Exception e) {
            // TODO: need to log exceptions in this file - sporcina (Oct.3/2013)
            e.printStackTrace();
        }
        return null;
    }

    public FlowHelper withPayload(FakeCustomer payload) {
        try {
            runFlowWithPayloadAndExpect(flowName, payload);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

//    public void expect(Conditions conditions) {
//
//        conditions.verify(flowResponsePayload);
//
//
//    }

    /**
     * Run the flow specified by name and assert equality on the expected output
     *
     * @param flowName The name of the flow to run
     */
    protected MuleEvent runFlowAndExpect(String flowName) throws Exception {
        Flow flow = lookupFlowConstruct(flowName);
        MuleEvent event = FunctionalTestCase.getTestEvent(null);
        return flow.process(event);
    }

    /**
     * Run the flow specified by name using the specified payload and assert
     * equality on the expected output
     *
     * @param flowName
     *              The name of the flow to run
     * @param payload
     *              The data object with data to match against
     */
    protected <T, U> void runFlowWithPayloadAndExpect(String flowName, /*FakeCustomer expected, */FakeCustomer payload/*, Boolean ignoreTheHashKey*/) throws Exception {
        Flow flow = lookupFlowConstruct(flowName);
        MuleEvent event = FunctionalTestCase.getTestEvent(payload);
        MuleEvent responseEvent = flow.process(event);

//        if (ignoreTheHashKey)
//            expected.equals_IgnoreNumValue(responseEvent.getMessage().getPayload());
//        else
//            expected.equals(responseEvent.getMessage().getPayload());

        flowResponsePayload = responseEvent.getMessage().getPayload();
    }

    /**
     * Retrieve a flow by name from the registry
     *
     * @param name Name of the flow to retrieve
     */
    protected static Flow lookupFlowConstruct(String name) {
        return (Flow) FunctionalTestCase.muleContext.getRegistry().lookupFlowConstruct(name);
    }

    @Override
    protected String getConfigResources() {
        return "mule-config.xml";
    }

    public Object getFlowResponsePayload() {
        return flowResponsePayload;
    }
}

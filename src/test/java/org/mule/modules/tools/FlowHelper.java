package org.mule.modules.tools;

import org.mule.api.MuleEvent;
import org.mule.construct.Flow;
import org.mule.tck.junit4.FunctionalTestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * TODO: need a description
 */
public class FlowHelper extends FunctionalTestCase {

    private static final Logger logger = LoggerFactory.getLogger(FlowHelper.class);

    private String flowName;
    private Object payload;
    private FlowResponse response;


    public FlowHelper() {
        try {
            setUpMuleContext();
        } catch (Exception e) {
            // TODO: is getMessage() enough information for an error log? - sporcina (Oct.13,2013)
            logger.error("Unable to setup mule context: {}", e.getMessage());
        }
    }


    public FlowHelper run(String flowName) {
        this.flowName = flowName;
        return this;
    }


    public <T> FlowHelper expecting(T responseType) {
        this.response = new FlowResponse<T>();

        try {
            runFlowWithPayloadAndExpectNew();
        } catch (Exception e) {
            logger.error("Unable to run flow with payload: {}\nflow = {}\npayload = {}", e.getMessage(), flowName, payload.toString());
        }

        return this;
    }


    public MuleEvent withoutPayload() throws Exception {
        return runFlowAndExpect(flowName);
    }


    public <T> FlowHelper withPayloadNew(T payload) {
        this.payload = payload;
        return this;
    }


    /**
     * Run the flow specified by name and assert equality on the expected output
     *
     * @param flowName
     *         The name of the flow to run
     */
    protected MuleEvent runFlowAndExpect(String flowName) throws Exception {
        Flow flow = lookupFlowConstruct(flowName);
        MuleEvent event = FunctionalTestCase.getTestEvent(null);
        return flow.process(event);
    }


    protected void runFlowWithPayloadAndExpectNew() throws Exception {
        Flow flow = lookupFlowConstruct(flowName);
        MuleEvent event = FunctionalTestCase.getTestEvent(payload);
        MuleEvent responseEvent = flow.process(event);
        response.setResponse(responseEvent.getMessage().getPayload());
    }


    /**
     * Retrieve a flow by name from the registry
     *
     * @param name
     *         Name of the flow to retrieve
     */
    protected static Flow lookupFlowConstruct(String name) {
        return (Flow) FunctionalTestCase.muleContext.getRegistry().lookupFlowConstruct(name);
    }


    @Override
    protected String getConfigResources() {
        return "mule-config.xml";
    }


    public FlowResponse getResponse() {
        return response;
    }
}

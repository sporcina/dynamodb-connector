package org.mule.modules.flows;

import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import org.junit.Assert;
import org.mule.api.MuleEvent;
import org.mule.modules.tools.FlowBuilder;

import javax.validation.constraints.NotNull;


public class RepositoryFlows {


    public void create() throws Exception {
        FlowBuilder.run("Should_Create_Table")
                .withoutPayload();
    }


    public void delete() throws Exception {
        FlowBuilder.run("Should_Delete_Table")
                .withoutPayload();
    }


    public void stateShouldBe(@NotNull String state) {
        try {
            MuleEvent responseEvent = FlowBuilder.run("Should_Get_Table_Info").withoutPayload();
            Assert.assertEquals(state, responseEvent.getMessage().getPayload());
        } catch (Exception e) {
            // TODO: I'm not happy with this design.  Can we find a better way to determine the exception thrown? - sporcina (Oct.13,2013)
            Assert.assertTrue("Was expectingType ResourceNotFoundException, not " + e.getMessage(), e.getCause() instanceof ResourceNotFoundException);
        }
    }
}

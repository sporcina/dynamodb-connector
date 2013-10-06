package org.mule.modules;

import org.junit.Assert;
import org.mule.api.MuleEvent;
import org.mule.construct.Flow;
import org.mule.modules.samples.FakeCustomer;
import org.mule.modules.tools.FlowHelper;
import org.mule.tck.junit4.AbstractMuleTestCase;
import org.mule.tck.junit4.FunctionalTestCase;

import java.util.Properties;

public class RepositoryFlows {


// TODO: need to increase the timeout for JBehave.  Can I do it for the table creation test? - sporcina (Oct.3/2013)
//    @BeforeClass
//    public static void beforeClass() {
//        increaseMaxTimeoutForTests();
//    }


    public void create() throws Exception {
        new FlowHelper().run("Should_Create_Table").withoutPayload();
    }


    public void stateShouldBe(String state) throws Exception {
        MuleEvent responseEvent = new FlowHelper().run("Should_Get_Table_Info").withoutPayload();
        Assert.assertEquals(state, responseEvent.getMessage().getPayload());
    }


    /**
     * Increase the maximum timeout for a test to fail
     * <p/>
     * The creation of a table in dynamoDB can be time-consuming depending on the complexity of the table and the
     * read/write capacity requested.  The default timeout for the average test is 60 seconds, defined as
     * AbstractMuleTestCase.DEFAULT_TEST_TIMEOUT_SECS.  For this test, table creation typically takes less than a
     * minute, but we increase the timeout to 5 minutes which will allow ample time to for the create table
     * request to complete.
     * <p/>
     * note:  This method affects all tests in this test class.  Ultimately we would like to alter the timeout for
     * those tests that requires it.  We have not found a good way to do that at this time.
     */
    private static void increaseMaxTimeoutForTests() {
        Properties props = System.getProperties();
        props.setProperty(AbstractMuleTestCase.TEST_TIMEOUT_SYSTEM_PROPERTY, "300"); // 300 seconds = 5 minutes
        System.setProperties(props);
    }
}

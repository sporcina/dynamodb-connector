package org.mule.modules;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mule.api.MuleEvent;
import org.mule.construct.Flow;
import org.mule.modules.samples.FakeCustomer;
import org.mule.modules.tools.DataFactory;
import org.mule.tck.junit4.AbstractMuleTestCase;
import org.mule.tck.junit4.FunctionalTestCase;

import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: sporcina
 * Date: 9/18/13
 * Time: 4:20 PM
 * To change this template use File | Settings | File Templates.
 */
@RunWith(JUnit4.class)
public class DocumentTest extends FunctionalTestCase {

    @BeforeClass
    public static void beforeClass() {
        increaseMaxTimeoutForTests();
//        try {
//            runFlowAndExpect("Should_Create_Table", TableStatus.ACTIVE.toString());
//        } catch (Exception e) {
//            e.printStackTrace();
//            org.junit.Assert.assertTrue("Failed to create the DynamoDB table", false);
//        }
    }

    @Override
    protected String getConfigResources()
    {
        return "mule-config.xml";
    }

    @Test
    public void shouldSaveDocument() throws Exception
    {
        FakeCustomer expected = DataFactory.createFakeCustomer();
        FakeCustomer payload = DataFactory.createFakeCustomer();

        Assert.assertTrue("The expected and payload objects must equal each other for this test", expected.equals(payload));

        Boolean ignoreHashKey = true;
        runFlowWithPayloadAndExpect("Should_Save_Document", expected, payload, ignoreHashKey);
    }

    @Test
    public void shouldGetDocument() throws Exception
    {
        FakeCustomer expected = DataFactory.createFakeCustomer();
        FakeCustomer payload = DataFactory.createFakeCustomer();

        Assert.assertTrue("The expected and payload objects must equal each other for this test", expected.equals(payload));

        Boolean ignoreHashKey = true;
        FakeCustomer template =  (FakeCustomer) runFlowWithPayloadAndExpect("Should_Save_Document", expected, payload, ignoreHashKey);

//        Cloner cloner=new Cloner();
//        FakeCustomer clone = cloner.deepClone(documentWithNum);

//        FakeCustomer template = new FakeCustomer()
//                .setNum(documentWithNum.getNum());
//                .setNum(" ")
//                .setName(documentWithNum.getName())
//                .setNotes(" ")
//                .setPhone(" ");

        //runFlowWithPayloadAndExpect("Should_Save_Document", expected, payload);

        //FakeCustomer template = FakeCustomer.copy(payload); //payload.removeEverythingButTheId();
        ignoreHashKey = false;
        runFlowWithPayloadAndExpect("Should_Get_Document", expected, template, ignoreHashKey);
    }

    /**
     * Run the flow specified by name and assert equality on the expected output
     *
     * @param flowName
     *              The name of the flow to run
     * @param expect
     *              The expected output
     */
    protected static <T> void runFlowAndExpect(String flowName, T expect) throws Exception
    {
        Flow flow = lookupFlowConstruct(flowName);
        MuleEvent event = FunctionalTestCase.getTestEvent(null);
        MuleEvent responseEvent = flow.process(event);

        Assert.assertEquals(expect, responseEvent.getMessage().getPayload());
    }

    /**
     * Run the flow specified by name using the specified payload and assert
     * equality on the expected output
     *
     * @param flowName
     *              The name of the flow to run
     * @param expected
     *              The expected output
     * @param payload
     * @param ignoreHashKey
     */

    // TODO: encapsulate this in to a builder pattern?!

    protected <T, U> Object runFlowWithPayloadAndExpect(String flowName, FakeCustomer expected, FakeCustomer payload, Boolean ignoreHashKey) throws Exception
    {
        Flow flow = lookupFlowConstruct(flowName);
        MuleEvent event = FunctionalTestCase.getTestEvent(payload);
        MuleEvent responseEvent = flow.process(event);

        if (ignoreHashKey)
            expected.equals_IgnoreNumValue(responseEvent.getMessage().getPayload());
        else
            expected.equals(responseEvent.getMessage().getPayload());

        return responseEvent.getMessage().getPayload();
    }


    /**
     * Retrieve a flow by name from the registry
     *
     * @param name
     *          Name of the flow to retrieve
     */
    protected static Flow lookupFlowConstruct(String name)
    {
        return (Flow) FunctionalTestCase.muleContext.getRegistry().lookupFlowConstruct(name);
    }


    /**
     * Increase the maximum timeout for a test to fail
     *
     * The creation of a table in dynamoDB can be time-consuming depending on the complexity of the table and the
     * read/write capacity requested.  The default timeout for the average test is 60 seconds, defined as
     * AbstractMuleTestCase.DEFAULT_TEST_TIMEOUT_SECS.  For this test, table creation typically takes less than a
     * minute, but we increase the timeout to 5 minutes which will allow ample time to for the create table
     * request to complete.
     *
     * note:  This method affects all tests in this test class.  Ultimately we would like to alter the timeout for
     * those tests that requires it.  We have not found a good way to do that at this time.
     */
    private static void increaseMaxTimeoutForTests() {
        Properties props = System.getProperties();
        props.setProperty(AbstractMuleTestCase.TEST_TIMEOUT_SYSTEM_PROPERTY, "300"); // 300 seconds = 5 minutes
        System.setProperties(props);
    }
}

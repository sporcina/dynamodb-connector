package org.mule.modules.tools;

import org.junit.Assert;
import org.mule.modules.samples.FakeCustomer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Mule Flow Response Conditions
 *
 *
 */
public class Conditions {

    private static Logger logger = LoggerFactory.getLogger(Conditions.class);

    private FakeCustomer fakeCustomer;
    private Boolean ignoreHashKey;

    public Conditions expect(FakeCustomer fakeCustomer) {
        this.fakeCustomer = fakeCustomer;
        return this;
    }

    public Conditions ignoreTheHashKey() {
        this.ignoreHashKey = true;
        return this;
    }

    public Conditions includeTheHashKey() {
        this.ignoreHashKey = false;
        return this;
    }

    public Boolean getIgnoreHashKey() {
        Assert.assertTrue("ignoreHashKey was null.  It must be set to true or false using ignoreTheHashKey() or includeTheHashKey()", ignoreHashKey != null);
        return ignoreHashKey;
    }

    public FakeCustomer getExpected() {
        return fakeCustomer;
    }

    public void verify(Object flowResponsePayload) {
        Boolean doTheyMatch;
        if (getIgnoreHashKey())
            doTheyMatch = getExpected().equals_IgnoreNumValue(flowResponsePayload);
        else
            doTheyMatch = getExpected().equals(flowResponsePayload);

        Assert.assertTrue("The documents did not match", doTheyMatch);
    }
}

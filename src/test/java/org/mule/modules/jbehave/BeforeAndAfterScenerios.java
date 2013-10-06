package org.mule.modules.jbehave;

import org.jbehave.core.annotations.AfterScenario;
import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.core.steps.Steps;
import org.mule.modules.samples.FakeCustomer;

public class BeforeAndAfterScenerios extends Steps {

    private final FakeCustomer fakeCustomer;

    public BeforeAndAfterScenerios(FakeCustomer fakeCustomer) {
        this.fakeCustomer = fakeCustomer;
    }

    @BeforeScenario
    public void beforeScenarios() throws Exception {
        fakeCustomer.reset();
    }

    @AfterScenario
    public void afterScenarios() throws Exception {
        fakeCustomer.reset();
    }
}

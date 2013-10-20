package org.mule.modules.jbehave;

import org.jbehave.core.annotations.AfterScenario;
import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.core.steps.Steps;
import org.mule.modules.samples.FakeCustomer;

import javax.validation.constraints.NotNull;


public class BeforeAndAfterScenarios extends Steps {

    private final FakeCustomer fakeCustomer;

    public BeforeAndAfterScenarios(@NotNull FakeCustomer fakeCustomer) {
        this.fakeCustomer = fakeCustomer;
    }

    @BeforeScenario
    public void beforeScenarios() {
        fakeCustomer.reset();
    }

    @AfterScenario
    public void afterScenarios() {
        fakeCustomer.reset();
    }
}

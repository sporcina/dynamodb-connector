package org.mule.modules.jbehave;


import de.codecentric.jbehave.junit.monitoring.JUnitReportingRunner;
import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.configuration.MostUsefulConfiguration;
import org.jbehave.core.embedder.EmbedderControls;
import org.jbehave.core.io.CodeLocations;
import org.jbehave.core.io.LoadFromClasspath;
import org.jbehave.core.io.StoryFinder;
import org.jbehave.core.junit.JUnitStories;
import org.jbehave.core.reporters.Format;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.jbehave.core.steps.InjectableStepsFactory;
import org.jbehave.core.steps.InstanceStepsFactory;
import org.junit.runner.RunWith;
import org.mule.modules.samples.FakeCustomer;

import java.util.List;


/**
 * Configures the JBehave ecosystem to run stories
 */
@RunWith(JUnitReportingRunner.class)
public class JBehaveStories extends JUnitStories {

    private static final String STORIES = "**/*.story";
    private static final Integer TEN_MINUTES_AS_SECONDS = 600;


    public JBehaveStories() {
        super();

        // TODO: review how this works internally - sporcina (Oct.13,2013)
        EmbedderControls embedderControls = configuredEmbedder().embedderControls();
        embedderControls.useStoryTimeoutInSecs(TEN_MINUTES_AS_SECONDS);
    }


    @Override
    public InjectableStepsFactory stepsFactory() {
        FakeCustomer fakeCustomer = new FakeCustomer();
        return new InstanceStepsFactory(
                configuration(),
                new TableSteps(),
                new DocumentSteps(fakeCustomer),
                new BeforeAndAfterScenarios(fakeCustomer)
        );
    }


    @Override
    public Configuration configuration() {
        return new MostUsefulConfiguration()
                .useStoryLoader(new LoadFromClasspath(this.getClass()))
                .useStoryReporterBuilder(new StoryReporterBuilder()
                        .withFormats(Format.XML, Format.IDE_CONSOLE, Format.CONSOLE, Format.HTML, Format.TXT));
    }


    @Override
    protected List<String> storyPaths() {
        return new StoryFinder().findPaths(CodeLocations.codeLocationFromClass(this.getClass()), STORIES, "");
    }
}

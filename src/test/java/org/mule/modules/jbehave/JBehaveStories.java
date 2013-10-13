package org.mule.modules.jbehave;

/**
 * Created with IntelliJ IDEA.
 * User: sporcina
 * Date: 9/25/13
 * Time: 9:31 AM
 * To change this template use File | Settings | File Templates.
 */

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

// credit to: https://blog.codecentric.de/en/2012/06/jbehave-configuration-tutorial/

@RunWith(JUnitReportingRunner.class)
public class JBehaveStories extends JUnitStories {

    //private Configuration configuration;
    private static final String STORIES = "**/*.story";
    private static final Integer TEN_MINUTES_AS_SECONDS = 600;

    public JBehaveStories() {
        super();

        /* TODO: this works, but does not display the story information in an attractive manner, like this:

            (org/mule/modules/jbehave/stories/StorageManagement.story)
            Scenario: Create a DynamoDB repository
            Given I have DynamoDB credentials
            When I request a new repository
            Then the repository is created successfully (FAILED)

            This is what the current config produces, but it is not as easy to configure.
            Need to play with this more to find the right settings.
        */

        /*configuration = new Configuration() {};

        // configuration.doDryRun(false); "no dry run" is implicit by using
        // default StoryControls

        // configuration.useDefaultStoryReporter(new ConsoleOutput());
        // deprecated -- rather use StoryReportBuilder

        configuration.useFailureStrategy(new RethrowingFailure());
        configuration.useKeywords(new LocalizedKeywords(Locale.ENGLISH));
        configuration.usePathCalculator(new AbsolutePathCalculator());
        configuration.useParameterControls(new ParameterControls());
        configuration.useParameterConverters(new ParameterConverters());
        configuration.useParanamer(new NullParanamer());
        configuration.usePendingStepStrategy(new FailingUponPendingStep());
        configuration.useStepCollector(new MarkUnmatchedStepsAsPending());
        configuration.useStepdocReporter(new PrintStreamStepdocReporter());
        configuration.useStepFinder(new StepFinder());
        configuration.useStepMonitor(new PrintStreamStepMonitor());
        //orig: configuration.useStepMonitor(new SilentStepMonitor());
        configuration
                .useStepPatternParser(new RegexPrefixCapturingPatternParser());
        configuration.useStoryControls(new StoryControls());
        configuration.useStoryLoader(new LoadFromClasspath());
        configuration.useStoryParser(new RegexStoryParser(configuration
                .keywords()));
        configuration.useStoryPathResolver(new UnderscoredCamelCaseResolver());
        configuration.useStoryReporterBuilder(new StoryReporterBuilder().withFormats(Format.STATS));
        configuration.useViewGenerator(new FreemarkerViewGenerator());

        EmbedderControls embedderControls = configuredEmbedder()
                .embedderControls();
        embedderControls.doBatch(false);
        embedderControls.doGenerateViewAfterStories(true);
        embedderControls.doIgnoreFailureInStories(false);
        embedderControls.doIgnoreFailureInView(false);
        embedderControls.doSkip(false);
        embedderControls.doVerboseFailures(false);
        embedderControls.doVerboseFiltering(false);
        embedderControls.useStoryTimeoutInSecs(300);
        embedderControls.useThreads(1);*/

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
                new BeforeAndAfterScenerios(fakeCustomer)
        );
    }

    @Override
    public Configuration configuration() {
        //return configuration;
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

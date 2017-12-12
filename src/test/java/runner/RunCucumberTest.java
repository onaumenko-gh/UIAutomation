package runner;

        import PageObjects.GrasshopperApp;
        import cucumber.api.CucumberOptions;
        import cucumber.api.junit.Cucumber;
        import helper.AppiumFactory;
        import org.junit.AfterClass;
        import org.junit.BeforeClass;
        import org.junit.runner.RunWith;

        import static com.citrix.grasshopper.at.steps.BaseSteps.app;

@RunWith(Cucumber.class)
@CucumberOptions(
        format = {
                "json:target/reports.json",
                "html:target/reports",
                "pretty"
        },
        features = "src/test/java/cucumber",
        glue = {"com.citrix.grasshopper.at.steps"},
        tags = {"@dropbox"},
        strict = false
)
public class RunCucumberTest {
    @BeforeClass
    public static void beforeScenario() {
        AppiumFactory.startAppium();
        app = GrasshopperApp.getInstance();
    }

    @AfterClass
    public static void afterScenario() {
        app.endSession();
        AppiumFactory.stopAppium();

    }

}
package libs.libCore.steps;

import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;
import cucumber.api.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {"ru.yandex.qatools.allure.cucumberjvm.AllureReporter"},
        features = "src/test/java/features",
        glue = "libs")
public class TestRunner {}
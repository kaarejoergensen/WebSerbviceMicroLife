package cucumber;

import calculator.Calculator;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;

public class MyStepdefs {
    private String number;
    private int actualAnswer;

    @Given("^An empty string$")
    public void anEmptyString() {
        number = "";
    }

    @When("^Calculating sum$")
    public void calculatingSum() {
        actualAnswer = Calculator.add(number);
    }

    @Then("^I should return (\\d+)$")
    public void iShouldReturn(int expected) {
        Assert.assertThat(expected, CoreMatchers.is(actualAnswer));
    }
}

package cucumber;

import calculator.Calculator;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import cucumber.api.java.en.Then;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

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
        assertThat(expected, is(actualAnswer));
    }
}

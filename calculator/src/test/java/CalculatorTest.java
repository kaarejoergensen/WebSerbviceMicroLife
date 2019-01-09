import calculator.Calculator;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.concurrent.ThreadLocalRandom;

public class CalculatorTest {

    @Test
    public void NoNumbers() {
        Assert.assertThat(Calculator.add(""), CoreMatchers.is(0));
    }

    @Test
    public void OneNumber(){
        int number = ThreadLocalRandom.current().nextInt(0, 100000);
        Assert.assertThat(Calculator.add(String.valueOf(number)), CoreMatchers.is(number));
    }

    @Test
    public void TwoNumbers(){
        int number1 = ThreadLocalRandom.current().nextInt(0, 100000);
        int number2 = ThreadLocalRandom.current().nextInt(0, 100000);
        Assert.assertThat(Calculator.add(number1 + "," + number2), CoreMatchers.is(number1 + number2));
    }

    @Test
    public void ManyNumbers(){
        int number = ThreadLocalRandom.current().nextInt(0, 10000);
        int finalSum = 0;
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < number; i++) {
            stringBuilder.append(i).append(",");
            finalSum += i;
        }
        Assert.assertThat(Calculator.add(stringBuilder.toString()), CoreMatchers.is(finalSum));
    }

    @Test
    public void NewLine(){
        Assert.assertThat(Calculator.add("1\n2,3"), CoreMatchers.is(6));
    }

    @Test(expected = IllegalArgumentException.class)
    public void NotNewLineCommaAdjacent(){
        Assert.assertThat(Calculator.add("1,\n,3"), CoreMatchers.is(-1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void OnlyNumbers(){
        Assert.assertThat(Calculator.add("1,a,3"), CoreMatchers.is(-1));
    }

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void NegativeNumbers() throws IllegalArgumentException {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Negative numbers: -1,-3,-100");
        Assert.assertThat(Calculator.add("-1,2,-3,4,5,6,-100"), CoreMatchers.is(-1));
    }

    @Test
    public void SupportOfDifferentDelimiters(){
        Assert.assertThat(Calculator.add("//;\\n1;2"), CoreMatchers.is(3));
    }



}

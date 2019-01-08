import calculator.Calculator;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.concurrent.ThreadLocalRandom;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

public class CalculatorTest {

    @Test
    public void NoNumbers() {
        assertThat(Calculator.add(""), is(0));
    }

    @Test
    public void OneNumber(){
        int number = ThreadLocalRandom.current().nextInt(0, 100000);
        assertThat(Calculator.add(String.valueOf(number)), is(number));
    }

    @Test
    public void TwoNumbers(){
        int number1 = ThreadLocalRandom.current().nextInt(0, 100000);
        int number2 = ThreadLocalRandom.current().nextInt(0, 100000);
        assertThat(Calculator.add(number1 + "," + number2), is(number1 + number2));
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
        assertThat(Calculator.add(stringBuilder.toString()), is(finalSum));
    }

    @Test
    public void NewLine(){
        assertThat(Calculator.add("1\n2,3"),is(6));
    }

    @Test(expected = IllegalArgumentException.class)
    public void NotNewLineCommaAdjacent(){
        assertThat(Calculator.add("1,\n,3"),is(-1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void OnlyNumbers(){
        assertThat(Calculator.add("1,a,3"),is(-1));
    }

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void NegativeNumbers() throws IllegalArgumentException {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Negative numbers: -1,-3,-100");
        assertThat(Calculator.add("-1,2,-3,4,5,6,-100"), is(-1));
    }

    @Test
    public void SupportOfDifferentDelimiters(){
        assertThat(Calculator.add("//;\\n1;2"),is(3));
    }



}

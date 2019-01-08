import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class CalculatorTest {

    @Test
    public void MiniTest() {
        assertThat("a", is("a"));
        assertThat("b", is("b"));
    }

    @Test
    public void TestCalculator() {
        assertThat(Calculator.Add("1,2"), is(3));
    }
}

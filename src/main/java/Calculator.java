public class Calculator {
    public static int Add(String numbers) {
        if (numbers == null || numbers.equals("")) {
            return 0;
        }
        String[] numberArray = numbers.split(",");
        if (numberArray.length > 2) {
            throw new IllegalArgumentException("Too many numbers");
        }


        return 0;
    }
}

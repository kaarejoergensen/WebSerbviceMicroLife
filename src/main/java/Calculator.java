import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Calculator {
    public static int add(String numbers) {
        if (numbers == null || numbers.equals("")) {
            return 0;
        }
        char delimeter = ',';
        //Custom delimetsrs
        try{
            if(numbers.charAt(0) == '/' && numbers.charAt(0) == '/'){
                delimeter= numbers.charAt(2);
                numbers = numbers.substring(5,numbers.length());
            }
        }catch(Exception e){
            throw new IllegalArgumentException();
        }
        //Splitter etter mellomrom, newline, eller komme
        String[] numberArray = numbers.split( delimeter + "|\n");

        int[] numberos = new int[numberArray.length];
        //Sjekker om alt er number - legger til i numberlort
        for(int i = 0; i < numberArray.length ; i++){
            try {
                numberos[i] = Integer.parseInt(numberArray[i]);
            } catch(NumberFormatException e) {
                throw new IllegalArgumentException();
            }
        }


        //Its all good lets go
        int sum = 0;
        List<Integer> negativeNumbers = new ArrayList<>();
        for(Integer i : numberos) {
            if (i < 0)
                negativeNumbers.add(i);
            sum += i;
        }
        if (!negativeNumbers.isEmpty())
            throw new IllegalArgumentException("Negative numbers: " + negativeNumbers.stream().
                    map(Objects::toString).collect(Collectors.joining(",")));
        return sum;
    }


}

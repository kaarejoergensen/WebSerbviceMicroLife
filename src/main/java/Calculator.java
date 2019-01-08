public class Calculator {



    public static int add(String numbers) {
        if (numbers == null || numbers.equals("")) {
            return 0;
        }
        //Splitter etter mellomrom, newline, eller komme
        String[] numberArray = numbers.split(",|\n");
        int[] numberos = new int[numberArray.length];
        //Sjekker om alt er number - legger til i numberlort
        for(int i = 0; i < numberArray.length ; i++){
            try{
                numberos[i] = Integer.parseInt(numberArray[i]);
            }catch(NumberFormatException e){
                throw new IllegalArgumentException();
            }
        }


        //Its all good lets go
        int sum = 0;
        for(Integer i : numberos) sum += i;
        return sum;
    }


}

public class Main {
    public static void main(String[] args) {
        String cmd = args[0];
        int n1 = Integer.parseInt(args[1]);
        int n2 = Integer.parseInt(args[2]);


        switch(cmd){
            case "plus":
                System.out.println(n1 + n2);
        }
    }
}

import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        
        Scanner input = new Scanner(System.in);

        while (true) {
            System.out.print("$ ");

            String command = input.nextLine();

            System.out.println(command + ": command not found");
        }
    }
}

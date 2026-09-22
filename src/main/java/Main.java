import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        
        Scanner input = new Scanner(System.in);

        String command = "";

        while (!command.equals("exit")) {
            System.out.print("$ ");

            command = input.nextLine();

            command = command.stripLeading();
            command = command.stripTrailing();

            System.out.println(command + ": command not found");
        }
    }
}

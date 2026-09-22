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

            if (command.equals("exit")) {
                System.exit(0);
            } else if (command.startsWith("echo")) {
                String echoArgs = command.substring(4);

                System.out.println(echoArgs);
            }

            System.out.println(command + ": command not found");
        }
    }
}

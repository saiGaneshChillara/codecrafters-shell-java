import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
                String echoArgs = command.substring(5);

                System.out.println(echoArgs);
            } else if (command.startsWith("type")) {
                String infoCommand = command.substring(5);

                switchLable:
                switch (infoCommand) {
                    case "echo":
                    case "exit":
                    case "type":
                        System.out.println(infoCommand + " is a shell builtin");
                        break;
                    default:
                        String pathVariable = System.getenv("PATH");
                        if (pathVariable != null) {
                            String[] directories = pathVariable.split(File.pathSeparator);

                            for (String dir: directories) {
                                Path path = Paths.get(dir, infoCommand);

                                if (Files.exists(path) && Files.isExecutable(path)) {
                                    System.out.println(infoCommand + " is " + path);
                                    break switchLable;
                                }
                            }
                        }
                        System.out.println(infoCommand + ": not found");
                        break;
                }
            }
            else {
                System.out.println(command + ": command not found");
            }
        }
    }
}

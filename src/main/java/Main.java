import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        
        Scanner input = new Scanner(System.in);

        while (true) {
            System.out.print("$ ");

            String command = input.nextLine().strip();

            if (command.equals("exit")) {
                break;
            } else if (
                command.equals("echo") || command.startsWith("echo ")
            ) {
                handleEcho(command);
            } else if (
                command.equals("type") ||
                command.startsWith("type ")
            ) {
                handleType(command);
            } else if (findExecutable(command.split(" ")[0]) != null) {
                executeExternalProgram(command.split(" "));
            }
            else {
                System.out.println(command + ": command not found");
            }
        }

        input.close();
    }

    private static void handleEcho(String command) {
        String echoArgs = command.length() > 5 
                            ? command.substring(5) 
                            : "";

        System.out.println(echoArgs);
    }

    private static void handleType(String command) {
        String targetCommand = command.length() > 5 
                                ? command.substring(5)
                                : "";
        if (isBuiltin(targetCommand)) {
            System.out.println(targetCommand + " is a shell builtin");
            return;
        }

        Path executablePath = findExecutable(targetCommand);

        if (executablePath != null) {
            System.out.println(targetCommand + " is " + executablePath);
        } else {
            System.out.println(targetCommand + ": not found");
        }
        
    }

    private static boolean isBuiltin(String command) {
        return command.equals("echo")
                || command.equals("exit")
                || command.equals("type");
    }

    private static Path findExecutable(String command) {
        String pathVariable = System.getenv("PATH");

        if (pathVariable == null) {
            return null;
        }

        String[] directories = pathVariable.split(File.pathSeparator);

        for (String dir: directories) {
            Path path = Paths.get(dir, command);

            if (Files.isRegularFile(path) && Files.isExecutable(path)) {
                return path;
            }
        }

        return null;
    }

    private static void executeExternalProgram(String[] commandWithArgs) {
        ProcessBuilder pb = new ProcessBuilder(commandWithArgs);

        try {
            pb.inheritIO();
            Process process = pb.start();
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {

    private static Path currentWorkingDir = Path.of("").toAbsolutePath();
    public static void main(String[] args) throws Exception {
        
        Scanner input = new Scanner(System.in);

        while (true) {
            System.out.print("$ ");

            String command = input.nextLine().strip();

            if (command.equals("exit")) {
                break;
            } else if (command.equals("pwd")) {
                System.out.println(getPresentWorkingDir());
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
            } else if (
                command.equals("cd") || 
                command.startsWith("cd ")
            ) {
                handleChangeDirectory(command);
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
                || command.equals("type")
                || command.equals("pwd");
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

    private static Path getPresentWorkingDir() {
        return currentWorkingDir;
    }

    private static void handleChangeDirectory(String command) {
        String pathString = command.length() > 3 ? command.substring(3) : "";
        Path newPath = Path.of(pathString).isAbsolute() ? Path.of(pathString) : currentWorkingDir.resolve(pathString);

        if (Files.exists(newPath)) {
            currentWorkingDir = newPath;
        } else {
            System.out.println("cd: " + pathString + ": No such file or directory");
        }
    }
}

import java.io.File;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.print("$ ");
            String s = sc.nextLine();

            if (s.equals("exit")) {
                break;
            }
            else if (s.startsWith("echo ")) {
                System.out.println(s.substring(5));
            }
            else if (s.startsWith("type ")) {
                String cmd = s.substring(5);

                // Builtins
                if (cmd.equals("echo") || cmd.equals("exit") || cmd.equals("type")) {
                    System.out.println(cmd + " is a shell builtin");
                }
                else {
                    String path = System.getenv("PATH");
                    String[] dirs = path.split(File.pathSeparator);

                    boolean found = false;

                    for (String dir : dirs) {
                        File file = new File(dir, cmd);

                        if (file.exists() && file.canExecute()) {
                            System.out.println(cmd + " is " + file.getAbsolutePath());
                            found = true;
                            break;
                        }
                    }

                    if (!found) {
                        System.out.println(cmd + ": not found");
                    }
                }
            }
            else {
                System.out.println(s + ": command not found");
            }
        }
    }
}
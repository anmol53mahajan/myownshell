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
            else if (s.equals("pwd")) {
                System.out.println(System.getProperty("user.dir"));
            }
            else if (s.startsWith("cd ")) {
                String path = s.substring(3).trim();
                File targetDir;

                if (path.equals("~")) {
                    targetDir = new File(System.getenv("HOME"));
                } else {
                    targetDir = new File(path);

                    if (!targetDir.isAbsolute()) {
                        targetDir = new File(System.getProperty("user.dir"), path);
                    }
                }
                if (targetDir.exists() && targetDir.isDirectory()) {
                    System.setProperty("user.dir", targetDir.getCanonicalPath());
                } else {
                    System.out.println("cd: " + path + ": No such file or directory");
                }
            }
            else if (s.startsWith("type ")) {
                String cmd = s.substring(5);

                if (cmd.equals("echo") || cmd.equals("exit") || cmd.equals("type") || cmd.equals("pwd")) {
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
                String[] parts = s.split(" ");
                String cmd = parts[0];

                String path = System.getenv("PATH");
                String[] dirs = path.split(File.pathSeparator);

                File executable = null;

                for (String dir : dirs) {
                    File file = new File(dir, cmd);

                    if (file.exists() && file.canExecute()) {
                        executable = file;
                        break;
                    }
                }

                if (executable == null) {
                    System.out.println(cmd + ": command not found");
                } else {

                    ProcessBuilder pb = new ProcessBuilder(parts);
                    pb.inheritIO();

                    Process p = pb.start();
                    p.waitFor();
                }
            }
        }
    }
}
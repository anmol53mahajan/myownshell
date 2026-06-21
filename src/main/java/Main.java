import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.print("$ ");
            String s = sc.nextLine();

            String outputFile = null;

            if (s.contains(" 1> ")) {
                String[] temp = s.split(" 1> ", 2);
                s = temp[0].trim();
                outputFile = temp[1].trim();
            } else if (s.contains(" > ")) {
                String[] temp = s.split(" > ", 2);
                s = temp[0].trim();
                outputFile = temp[1].trim();
            }

            if (s.equals("exit")) {
                break;
            }
            else if (s.startsWith("echo ")) {
                String output = s.substring(5);

                if (outputFile != null) {
                    PrintWriter pw = new PrintWriter(outputFile);
                    pw.println(output);
                    pw.close();
                } else {
                    System.out.println(output);
                }
            }
            else if (s.equals("pwd")) {
                String output = System.getProperty("user.dir");

                if (outputFile != null) {
                    PrintWriter pw = new PrintWriter(outputFile);
                    pw.println(output);
                    pw.close();
                } else {
                    System.out.println(output);
                }
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
                String result;

                if (cmd.equals("echo") || cmd.equals("exit") || cmd.equals("type") || cmd.equals("pwd")) {
                    result = cmd + " is a shell builtin";
                } else {
                    String path = System.getenv("PATH");
                    String[] dirs = path.split(File.pathSeparator);

                    result = null;

                    for (String dir : dirs) {
                        File file = new File(dir, cmd);

                        if (file.exists() && file.canExecute()) {
                            result = cmd + " is " + file.getAbsolutePath();
                            break;
                        }
                    }

                    if (result == null) {
                        result = cmd + ": not found";
                    }
                }

                if (outputFile != null) {
                    PrintWriter pw = new PrintWriter(outputFile);
                    pw.println(result);
                    pw.close();
                } else {
                    System.out.println(result);
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

                    pb.directory(new File(System.getProperty("user.dir")));
                    pb.redirectInput(ProcessBuilder.Redirect.INHERIT);
                    pb.redirectError(ProcessBuilder.Redirect.INHERIT);

                    if (outputFile != null) {
                        pb.redirectOutput(new File(outputFile));
                    } else {
                        pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
                    }

                    Process p = pb.start();
                    p.waitFor();
                }
            }
        }
    }
}
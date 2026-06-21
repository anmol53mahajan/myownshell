import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
class Job {
    int jobNumber;
    Process process;
    String command;

    Job(int jobNumber, Process process, String command) {
        this.jobNumber = jobNumber;
        this.process = process;
        this.command = command;
    }
}
public class Main {
    static void reapJobs(ArrayList<Job> jobs) {

        ArrayList<Job> toRemove = new ArrayList<>();

        for (int i = 0; i < jobs.size(); i++) {

            Job job = jobs.get(i);

            if (!job.process.isAlive()) {

                char marker = ' ';

                if (i == jobs.size() - 1) {
                    marker = '+';
                } else if (i == jobs.size() - 2) {
                    marker = '-';
                }

                System.out.printf("[%d]%c  %-24s%s%n",
                        job.jobNumber,
                        marker,
                        "Done",
                        job.command.replace(" &", ""));

                toRemove.add(job);
            }
        }

        jobs.removeAll(toRemove);
    }
    static int getNextJobNumber(ArrayList<Job> jobs) {

        if (jobs.isEmpty()) {
            return 1;
        }

        int max = 0;

        for (Job job : jobs) {
            max = Math.max(max, job.jobNumber);
        }

        return max + 1;
    }
    public static void main(String[] args) throws Exception {
    
        Scanner sc = new Scanner(System.in);

        ArrayList<Job> jobs = new ArrayList<>();
        while (true) {
            reapJobs(jobs);
            System.out.print("$ ");
            String s = sc.nextLine();

            String outputFile = null;
            String errorFile = null;
            boolean appendOutput = false;
            boolean appendError = false;

            if (s.contains(" 2>> ")) {
                String[] temp = s.split(" 2>> ", 2);
                s = temp[0].trim();
                errorFile = temp[1].trim();
                appendError = true;
            }
            else if (s.contains(" 2> ")) {
                String[] temp = s.split(" 2> ", 2);
                s = temp[0].trim();
                errorFile = temp[1].trim();
            }
            else if (s.contains(" 1>> ")) {
                String[] temp = s.split(" 1>> ", 2);
                s = temp[0].trim();
                outputFile = temp[1].trim();
                appendOutput = true;
            }
            else if (s.contains(" >> ")) {
                String[] temp = s.split(" >> ", 2);
                s = temp[0].trim();
                outputFile = temp[1].trim();
                appendOutput = true;
            }
            else if (s.contains(" 1> ")) {
                String[] temp = s.split(" 1> ", 2);
                s = temp[0].trim();
                outputFile = temp[1].trim();
            }
            else if (s.contains(" > ")) {
                String[] temp = s.split(" > ", 2);
                s = temp[0].trim();
                outputFile = temp[1].trim();
            }

            if (s.equals("exit")) {
                break;
            }
            else if (s.contains(" | ")) {

                String[] commands = s.split("\\|", 2);

                String left = commands[0].trim();
                String right = commands[1].trim();

                String[] leftParts = left.split(" ");
                String[] rightParts = right.split(" ");

                String leftOutput = "";

                // LEFT SIDE
                if (leftParts[0].equals("echo")) {

                    leftOutput = left.substring(5) + System.lineSeparator();

                } else if (leftParts[0].equals("type")) {

                    String cmdName = leftParts[1];

                    if (cmdName.equals("echo") || cmdName.equals("exit")
                            || cmdName.equals("type") || cmdName.equals("pwd")
                            || cmdName.equals("cd") || cmdName.equals("jobs")) {
                        leftOutput = cmdName + " is a shell builtin"
                                + System.lineSeparator();
                    }

                } else {

                    ProcessBuilder pb = new ProcessBuilder(leftParts);
                    pb.directory(new File(System.getProperty("user.dir")));

                    Process p = pb.start();

                    java.io.ByteArrayOutputStream baos =
                            new java.io.ByteArrayOutputStream();

                    p.getInputStream().transferTo(baos);

                    p.waitFor();

                    leftOutput = baos.toString();
                }

                // RIGHT SIDE
                if (rightParts[0].equals("type")) {

                    String cmdName = rightParts[1];

                    if (cmdName.equals("echo") || cmdName.equals("exit")
                            || cmdName.equals("type") || cmdName.equals("pwd")
                            || cmdName.equals("cd") || cmdName.equals("jobs")) {

                        System.out.println(cmdName + " is a shell builtin");
                    }

                } else {

                    ProcessBuilder pb = new ProcessBuilder(rightParts);
                    pb.directory(new File(System.getProperty("user.dir")));

                    Process p = pb.start();

                    p.getOutputStream().write(leftOutput.getBytes());
                    p.getOutputStream().close();

                    p.getInputStream().transferTo(System.out);

                    p.waitFor();
                }
            }
            else if (s.startsWith("echo ")) {

                if (errorFile != null) {
                    if (appendError) {
                        new PrintWriter(new FileWriter(errorFile, true)).close();
                    } else {
                        new PrintWriter(errorFile).close();
                    }
                }

                String output = s.substring(5);

                if (outputFile != null) {
                    PrintWriter pw;

                    if (appendOutput) {
                        pw = new PrintWriter(new FileWriter(outputFile, true));
                    } else {
                        pw = new PrintWriter(outputFile);
                    }

                    pw.println(output);
                    pw.close();
                } else {
                    System.out.println(output);
                }
            }
            else if (s.equals("pwd")) {

                if (errorFile != null) {
                    if (appendError) {
                        new PrintWriter(new FileWriter(errorFile, true)).close();
                    } else {
                        new PrintWriter(errorFile).close();
                    }
                }

                String output = System.getProperty("user.dir");

                if (outputFile != null) {
                    PrintWriter pw;

                    if (appendOutput) {
                        pw = new PrintWriter(new FileWriter(outputFile, true));
                    } else {
                        pw = new PrintWriter(outputFile);
                    }

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

                    if (errorFile != null) {
                        if (appendError) {
                            new PrintWriter(new FileWriter(errorFile, true)).close();
                        } else {
                            new PrintWriter(errorFile).close();
                        }
                    }
                } else {
                    String errorMsg = "cd: " + path + ": No such file or directory";

                    if (errorFile != null) {
                        PrintWriter pw;

                        if (appendError) {
                            pw = new PrintWriter(new FileWriter(errorFile, true));
                        } else {
                            pw = new PrintWriter(errorFile);
                        }

                        pw.println(errorMsg);
                        pw.close();
                    } else {
                        System.out.println(errorMsg);
                    }
                }
            }
            else if (s.equals("jobs")) {

                if (errorFile != null) {
                    if (appendError) {
                        new PrintWriter(new FileWriter(errorFile, true)).close();
                    } else {
                        new PrintWriter(errorFile).close();
                    }
                }
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                }
                ArrayList<Job> toRemove = new ArrayList<>();

                for (int i = 0; i < jobs.size(); i++) {

                    Job job = jobs.get(i);

                    char marker = ' ';

                    if (i == jobs.size() - 1) {
                        marker = '+';
                    } else if (i == jobs.size() - 2) {
                        marker = '-';
                    }

                    if (job.process.isAlive()) {

                        System.out.printf("[%d]%c  %-24s%s%n",
                                job.jobNumber,
                                marker,
                                "Running",
                                job.command);

                    } else {

                        System.out.printf("[%d]%c  %-24s%s%n",
                                job.jobNumber,
                                marker,
                                "Done",
                                job.command.replace(" &", ""));

                        toRemove.add(job);
                    }
                }

                jobs.removeAll(toRemove);
            }
            else if (s.startsWith("type ")) {

                if (errorFile != null) {
                    if (appendError) {
                        new PrintWriter(new FileWriter(errorFile, true)).close();
                    } else {
                        new PrintWriter(errorFile).close();
                    }
                }

                String cmd = s.substring(5);
                String result;

                if (cmd.equals("echo") || cmd.equals("exit")
                        || cmd.equals("type") || cmd.equals("pwd")
                        || cmd.equals("cd") || cmd.equals("jobs")) {
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
                    PrintWriter pw;

                    if (appendOutput) {
                        pw = new PrintWriter(new FileWriter(outputFile, true));
                    } else {
                        pw = new PrintWriter(outputFile);
                    }

                    pw.println(result);
                    pw.close();
                } else {
                    System.out.println(result);
                }
            }
            else {
                String[] parts = s.split(" ");
                String cmd = parts[0];
                boolean background = false;

                if (parts.length > 0 && parts[parts.length - 1].equals("&")) {
                    background = true;

                    String[] temp = new String[parts.length - 1];
                    System.arraycopy(parts, 0, temp, 0, parts.length - 1);
                    parts = temp;

                    cmd = parts[0];
                }
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
                    String errorMsg = cmd + ": command not found";

                    if (errorFile != null) {
                        PrintWriter pw;

                        if (appendError) {
                            pw = new PrintWriter(new FileWriter(errorFile, true));
                        } else {
                            pw = new PrintWriter(errorFile);
                        }

                        pw.println(errorMsg);
                        pw.close();
                    } else {
                        System.out.println(errorMsg);
                    }
                } else {
                    ProcessBuilder pb = new ProcessBuilder(parts);

                    pb.directory(new File(System.getProperty("user.dir")));
                    pb.redirectInput(ProcessBuilder.Redirect.INHERIT);

                    if (outputFile != null) {
                        if (appendOutput) {
                            pb.redirectOutput(
                                ProcessBuilder.Redirect.appendTo(
                                    new File(outputFile)
                                )
                            );
                        } else {
                            pb.redirectOutput(new File(outputFile));
                        }
                    } else {
                        pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
                    }

                    if (errorFile != null) {
                        if (appendError) {
                            pb.redirectError(
                                ProcessBuilder.Redirect.appendTo(
                                    new File(errorFile)
                                )
                            );
                        } else {
                            pb.redirectError(new File(errorFile));
                        }
                    } else {
                        pb.redirectError(ProcessBuilder.Redirect.INHERIT);
                    }

                    Process p = pb.start();

                    if (background) {
                        
                        int jobNumber = getNextJobNumber(jobs);

                        jobs.add(new Job(
                                jobNumber,
                                p,
                                s
                        ));

                        System.out.println("[" + jobNumber + "] " + p.pid());

                    } else {
                        p.waitFor();
                    }
                }
            }
        }
    }
}
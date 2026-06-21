# OS Assignment: Build Your Own Shell Using Java

## Student Information
- **Name**: Anmol Mahajan
- **Roll Number**: 25BCS10037
- **Institution**: Scaler School of Technology

[![progress-banner](https://backend.codecrafters.io/progress/shell/65acc684-b9c4-47d3-bfcc-24e3e8b803f1)](https://app.codecrafters.io/users/anmol53mahajan?r=2qF)

---

## Project Overview

This is a Java implementation of the ["Build Your Own Shell" Challenge](https://app.codecrafters.io/courses/shell/overview) from CodeCrafters.

In this challenge, you build a POSIX-compliant shell capable of interpreting shell commands, running external programs, and implementing builtin commands like cd, pwd, echo, and more. Along the way, you learn about shell command parsing, REPLs, builtin commands, process management, redirection, pipelines, and more.

---

## Stages Completed

**Total Progress: 34/34 mandatory stages completed **

### Base Shell Stages ✓ (8/8)
- Shell prompt and REPL loop
- Handling invalid commands
- Implementing exit
- Implementing echo
- Implementing type
- PATH lookup
- Running external programs with arguments

### Navigation Extension ✓ (4/4)
- pwd command
- cd with absolute paths
- cd with relative paths
- cd with home directory (~)

### Quoting & Parsing Extension ✓ (6/6)
- Single quote handling
- Double quote handling
- Backslash escaping (outside quotes)
- Backslash escaping (within quotes)
- Quote interactions and edge cases
- Executing quoted executables

### Redirection Extension ✓ (4/4)
- Redirect stdout (> and 1>)
- Redirect stderr (2>)
- Append stdout (>> and 1>>)
- Append stderr (2>>)

### Background Jobs Extension ✓ (9/9)
- Register jobs builtin
- Start background jobs using &
- Print background job output
- List single job
- List multiple jobs
- Reap one completed job
- Reap multiple completed jobs
- Reap before next prompt
- Recycle job numbers

### Pipelines Extension ✓ (3/3)
- Dual-command pipelines
- Multi-command pipelines
- Pipelines with built-in commands

---

## Key Implementation Details

The implementation includes:
- **Command Parsing**: Hand-written tokenizer supporting quote handling and backslash escaping
- **Process Management**: Job tracking with ArrayList<Job> for background processes
- **Stream Redirection**: Support for stdout/stderr redirection (>, 1>, 2>, >>, 1>>, 2>>)
- **Pipelines**: Support for inter-process communication through pipes
- **Built-in Commands**: echo, pwd, cd, type, jobs, exit
- **Process Reaping**: Automatic cleanup of completed background jobs before each prompt

---

## Project Structure

```
codecrafters-shell-java/
├── src/main/java/Main.java       (Main shell implementation - 700+ lines)
├── pom.xml                        (Maven configuration)
├── your_program.sh                (Execution wrapper script)
├── codecrafters.yml               (CodeCrafters configuration)
└── README.md                      (This file)
```

---

This is a starting point for Java solutions to the
["Build Your Own Shell" Challenge](https://app.codecrafters.io/courses/shell/overview).

**Note**: If you're viewing this repo on GitHub, head over to
[codecrafters.io](https://codecrafters.io) to try the challenge.

# Passing the first stage

The entry point for your `shell` implementation is in `src/main/java/Main.java`.
Study and uncomment the relevant code, then run the command below to execute the
tests on our servers:

```sh
codecrafters submit
```

Time to move on to the next stage!

# Stage 2 & beyond

Note: This section is for stages 2 and beyond.

1. Ensure you have `mvn` installed locally
1. Run `./your_program.sh` to run your program, which is implemented in
   `src/main/java/Main.java`.
1. Run `codecrafters submit` to submit your solution to CodeCrafters. Test
   output will be streamed to your terminal.

package com.jet.lang;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Jet {

    static boolean hasError = false;

    /**
     * Entry point for the Jet language interpreter.
     * <p>
     * Handles command-line arguments to either execute a script file or start the
     * REPL.
     * </p>
     *
     * @param args Command-line arguments. If one argument is provided, it is
     *             treated as a script file path. If none, starts REPL.
     * @throws IOException if an I/O error occurs during file reading or REPL input.
     */
    public static void main(String[] args) throws IOException {

        if (args.length > 1) {
            System.out.println("Usage: jet [script]");
            System.exit(64);

        } else if (args.length == 1) {
            // If one argument is provided, treat it as a file path and execute the script
            // file.
            runFile(args[0]);
        } else {
            // If no arguments are provided, start the interactive REPL (Read-Eval-Print
            // Loop).
            runPrompt();
        }
    }

    /**
     * Reads the contents of a file and executes it as Jet source code.
     *
     * @param path The path to the script file to execute.
     * @throws IOException if an I/O error occurs while reading the file.
     */
    private static void runFile(String path) throws IOException {
        // Read the file at the given path, convert its contents to a String, and pass
        // it to the interpreter.
        // If an error occurs during execution, exit with status code 65.

        if (hasError)
            System.exit(65);

        byte[] bytes = Files.readAllBytes(Paths.get(path));
        run(new String(bytes, Charset.defaultCharset()));
    }

    /**
     * Processes and interprets Jet source code provided as a string.
     *
     * @param source The Jet source code to interpret.
     */
    private static void run(String source) {
        // Process the source code string (passes it to the lexer and parser for
        // interpretation).
        
    }

    /**
     * Starts an interactive Read-Eval-Print Loop (REPL) for Jet code.
     * <p>
     * Continuously reads user input, interprets it, and prints results.
     * </p>
     *
     * @throws IOException if an I/O error occurs during input reading.
     */
    private static void runPrompt() throws IOException {
        // Start a REPL (Read-Eval-Print Loop) for interactive user input.
        // Each line entered by the user is processed and interpreted.
        InputStreamReader inputReader = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(inputReader);

        while (true) {
            hasError = false;
            System.out.print("Jet > ");
            String line = reader.readLine();
            if (line == null)
                break;
            run(line); // Process each line entered by the user.
        }
    }

    /**
     * Reports an error at a specific line and location in the source code.
     *
     * @param line   The line number where the error occurred.
     * @param where  Additional context about where the error occurred (e.g., in which construct).
     * @param message The error message to display.
     */
    private static void report(int line, String where, String message) {
        System.out.println("[Line " + line + "] Error" + where + ": " + message);
        hasError = true;
    }

    /**
     * Reports an error at a specific line in the source code.
     *
     * @param line    The line number where the error occurred.
     * @param message The error message to display.
     */
    static void error(int line, String message) {
        report(line, "", message);
    }

}

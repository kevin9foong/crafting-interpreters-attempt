package jlox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * The main class for the Lox interpreter.
 * Since lox is scripting language, we are running the source code directly.
 */
public class Lox {
    static boolean isErrorPresent = false; // flag to check if there was an error while running the interpreter
    public static void main(String[] args) throws IOException {
        if (args.length > 1) {
            System.out.println("Usage: jlox [script]");
            System.exit(64); // 64 is the exit code for command line usage error based on unix sysexits.h
        } else if (args.length == 1) {
            runFile(args[0]);
        } else {
            runPrompt();
        }
    }

    /**
     * If the user passes a source code file
     * @param path
     */
    private static void runFile(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path)); // read file as bytes
        run(new String(bytes, Charset.defaultCharset())); // convert bytes to string

        if (isErrorPresent) System.exit(65); // 65 is the exit code for data format error based on unix sysexits.h
    }

    /**
     * If the user wants to run the interpreter in REPL mode
     * Fun fact: REPL stands for Read Eval Print Loop.
     * It is derived from Lisp where implementing one is simply: (print (eval (read)))
     * read -> eval -> print -> loop from inner to outer function
     */
    private static void runPrompt() throws IOException {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);

        // loop forever until user breaks
        for (;;) {
            System.out.print("> ");
            String line = reader.readLine();
            if (line == null) break; // user pressed ctrl + d
            run(line);
            isErrorPresent = false; // reset error flag since error in previous line shouldnt kill entire interpreter
        }
    }

    private static void run(String sourceCode) {
        Scanner scanner = new Scanner(sourceCode);
        List<Token> tokens = scanner.scanTokens();

        for (Token token : tokens) {
            System.out.println(token);
        }
    }

    // IMPROVEMENT: abstract out error handling to a separate ErrorReporter interface. Skipped since not focus of project.
    // erorr handling methods
    static void error(int line, String message) {
        reportError(line, "", message);
    }

    /**
     * Prints the stack trace.
     * Barebones implementation. Better approach is to show where the exact error occurred but requires string manipulation code.
     */
    private static void reportError(int line, String where, String message) {
        System.err.println("[line " + line + "] Error" + where + ": " + message);
        isErrorPresent = true;
    }
}

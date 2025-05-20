package com.jet.lang;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Jet {
    public static void main(String[] args) throws IOException {
        
       if(args.length > 1) {
        System.out.println("Usage: jet [script]");
        System.exit(64);

       } else if(args.length == 1){ // One args means only file path is provided
        // So we will call run file function
       } else{
        // If no argument is provided, we'll start REPL
       }
    }
    private static void runFile(String path) throws IOException{
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        run(new String(bytes, Charset.defaultCharset())); //typecasting the buffered file into String which we'll pass to our Lexer
    }
    private static void run(String source){
        // we will feed the source to the Lexer
    }
    private static void runPrompt() throws IOException{
        // Taking input from user following REPL (Read, Evaluate, Print, Loop)
        InputStreamReader inputReader = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(inputReader);

        while(true){
            System.out.print("Jett > ");
            String line = reader.readLine();
            if(line==null) break;
            run(line);
        }
    }
}

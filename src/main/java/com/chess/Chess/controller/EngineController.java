package com.chess.Chess.controller;

import java.io.*;
import java.util.Arrays;

public class EngineController {
    public static void getMoveFromStockfish(String difficulty) {
        ProcessBuilder builder = new ProcessBuilder("stockfish");
        Process process = null;
        try {
            process = builder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            reader.readLine();
            Writer writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
            writer.write("uci\n");
            writer.flush();
            String line;
            while (reader.ready() && (line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println("An error occurred");
            System.out.println(e);
        }
        process.destroyForcibly();
    }
}

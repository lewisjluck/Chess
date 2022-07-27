package com.chess.Chess.controller;

import java.io.*;
import java.util.Arrays;

public class EngineController {
    private Process engine;
    private BufferedReader engineOutput;
    private BufferedWriter engineInput;

    private void sendCommand(String command) throws IOException {
        engineInput.write(command + "\n");
        engineInput.flush();
    }

    private String readUntil(String endPhrase) throws IOException {
        String line;
        String lastLine = "";
        while ((line = engineOutput.readLine()) != null) {
            lastLine = line;
            if (line.startsWith(endPhrase)) {
                break;
            }
            if (!engineOutput.ready()) {
                throw new IOException("Expected output not received.");
            }
        }
        return lastLine;
    }

    public void initialiseEngine(String elo) {
        ProcessBuilder builder = new ProcessBuilder("stockfish");
        try {
            // setup engine config
            engine = builder.start();
            engineOutput = new BufferedReader(new InputStreamReader(engine.getInputStream()));
            engineInput = new BufferedWriter(new OutputStreamWriter(engine.getOutputStream()));

            // discard stockfish intro
            engineOutput.readLine();

            // initialise uci and newgame
            sendCommand("uci");
            readUntil("uciok");

            sendCommand("ucinewgame");
            sendCommand("setoption name UCI_LimitStrength value true");
            sendCommand("setoption name UCI_Elo value " + elo);

            sendCommand("isready");
            readUntil("readyok");
        } catch (IOException e) {
            System.out.println("An error occurred in initialising Stockfish: ");
            e.printStackTrace();
        }
    }

    public String getMove(String fen) {
        try {
            sendCommand("position fen " + fen);
            sendCommand("go movetime 250");
            Thread.sleep(300);
            return readUntil("bestmove").split(" ")[1];
        } catch (IOException e) {
            System.out.println("An error occurred in generating the engine move: ");
            e.printStackTrace();
        } catch (InterruptedException e) {
            System.out.println("Error with sleep wait.");
        }
        return "";
    }
}

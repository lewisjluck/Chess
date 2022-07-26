package com.chess.Chess.controller;

import com.chess.Chess.model.Colour;
import com.chess.Chess.model.Player;
import com.chess.Chess.model.Position;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@RestController
public class APIController {
    private EngineController engine = null;

    @GetMapping("/get_moves")
    public List<String> getMoves(@RequestParam String coords, @RequestParam String player) {
        Position position = Parser.getPositionFromString(coords);
        return Parser.formatJsonMoves(HomeController.board.getMoves(position, Colour.valueOf(player.toUpperCase(Locale.ROOT))));
    }

    @GetMapping("/move")
    public MoveResponse move(@RequestParam String player, @RequestParam String from, @RequestParam String to) {
        Position moveFrom = Parser.getPositionFromString(from);
        Position moveTo = Parser.getPositionFromString(to);
        return HomeController.board.move(moveFrom, moveTo, Colour.valueOf(player.toUpperCase(Locale.ROOT)));
    }

    @GetMapping("/get_engine_move")
    public MoveResponse getEngineMove(@RequestParam String player, @RequestParam String elo) {
        if (engine == null) {
            engine = new EngineController();
            engine.initialiseEngine(elo);
        }

        String fen = HomeController.board.getFEN(player);
        String bestMove = engine.getMove(fen);

        System.out.println(fen);
        System.out.println(bestMove);
        System.out.println(HomeController.board);

        Position from = Parser.getPositionFromNotation(bestMove.substring(0,2));
        Position to = Parser.getPositionFromNotation(bestMove.substring(2,4));

        MoveResponse response = HomeController.board.move(from, to, Colour.valueOf(player.toUpperCase(Locale.ROOT)));
        response.addTile(from, null);
        response.addTile(to, HomeController.board.getPieceFromPosition(to));

        return response;
    }
}

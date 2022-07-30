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
    public List<String> getMoves(@RequestParam String coords, @RequestParam String player, @RequestParam String gameID) {
        Position position = Parser.getPositionFromString(coords);
        Colour colour = Colour.valueOf(player.toUpperCase(Locale.ROOT));
        return Parser.formatJsonMoves(GameController.getGame(Integer.parseInt(gameID)).getMoves(position, colour));
    }

    @GetMapping("/move")
    public MoveResponse move(@RequestParam String player, @RequestParam String from, @RequestParam String to, @RequestParam String gameID) {
        Position moveFrom = Parser.getPositionFromString(from);
        Position moveTo = Parser.getPositionFromString(to);
        Colour colour = Colour.valueOf(player.toUpperCase(Locale.ROOT));
        return GameController.getGame(Integer.parseInt(gameID)).move(moveFrom, moveTo, colour);
    }

    @GetMapping("/get_engine_move")
    public MoveResponse getEngineMove(@RequestParam String player, @RequestParam String elo, @RequestParam String gameID) {
        if (engine == null) {
            engine = new EngineController();
            engine.initialiseEngine(elo);
        }

        String fen = GameController.getGame(Integer.parseInt(gameID)).getFEN(player);
        String bestMove = engine.getMove(fen);

        Position from = Parser.getPositionFromNotation(bestMove.substring(0,2));
        Position to = Parser.getPositionFromNotation(bestMove.substring(2,4));

        MoveResponse response = HomeController.board.move(from, to, Colour.valueOf(player.toUpperCase(Locale.ROOT)));
        response.addTile(from, null);
        response.addTile(to, HomeController.board.getPieceFromPosition(to));

        return response;
    }

    @GetMapping("/new_game")
    public Integer newGame() {
        return GameController.newGame();
    }
}

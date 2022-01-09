package com.chess.Chess.controller;

import com.chess.Chess.model.Colour;
import com.chess.Chess.model.Player;
import com.chess.Chess.model.Position;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@RestController
public class APIController {
    @GetMapping("/get_moves")
    public List<Position> getMoves(@RequestParam String coords, @RequestParam String player) {
        Position position = Position.getPositionFromString(coords);
        List<Position> possibleMoves = HomeController.board.getMoves(position, new Player("Test", Colour.valueOf(player.toUpperCase(Locale.ROOT))));
        System.out.println(possibleMoves);
        return possibleMoves;
    }

    @GetMapping("/move")
    public void move(@RequestParam String player, @RequestParam String from, @RequestParam String to) {
        Position moveFrom = Position.getPositionFromString(from);
        Position moveTo = Position.getPositionFromString(to);
        Player movePlayer = new Player("Test", Colour.valueOf(player.toUpperCase(Locale.ROOT)));
        HomeController.board.move(moveFrom, moveTo, movePlayer);
    }
}

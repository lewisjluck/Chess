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
    @GetMapping("/get_moves")
    public List<Position> getMoves(@RequestParam String coords, @RequestParam String player) {
        Position position = Position.getPositionFromString(coords);
        return HomeController.board.getMoves(position, Colour.valueOf(player.toUpperCase(Locale.ROOT)));
    }

    @GetMapping("/move")
    public HashMap<String, List<String>> move(@RequestParam String player, @RequestParam String from, @RequestParam String to) {
        Position moveFrom = Position.getPositionFromString(from);
        Position moveTo = Position.getPositionFromString(to);
        return HomeController.board.move(moveFrom, moveTo, Colour.valueOf(player.toUpperCase(Locale.ROOT)));
    }
}

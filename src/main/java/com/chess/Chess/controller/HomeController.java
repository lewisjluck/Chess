package com.chess.Chess.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.chess.Chess.model.Board;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class HomeController {
    public static Board board = new Board();

    public static GameController gameController;

    @GetMapping("/game")
    public String game(Model model) {
        model.addAttribute("board", board.getDisplay());
        return "game";
    }
}

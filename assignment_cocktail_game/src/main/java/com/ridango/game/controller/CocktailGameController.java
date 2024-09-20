package com.ridango.game.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ridango.game.logic.CocktailGame;
import com.ridango.game.model.Cocktail;

@RestController
@RequestMapping("/game")
public class CocktailGameController {

    private final CocktailGame cocktailGame;

    @Autowired
    public CocktailGameController(CocktailGame cocktailGame) {
        this.cocktailGame = cocktailGame;
    }

    // Start a new game
    @GetMapping("/start")
    public String startGame() {
        cocktailGame.startGame(); // Start the game logic
        return "Game started!";
    }

    // Fetch the current cocktail (or new state)
    @GetMapping("/cocktail")
    public Cocktail getCocktail() {
        return cocktailGame.getNewUniqueCocktail(); // Fetch the current or new cocktail
    }

    // Make a guess for the cocktail and return feedback
    @PostMapping("/guess")
    public String makeGuess(@RequestParam String guess) {
        return cocktailGame.makeGuess(guess); // Process the guess through the CocktailGame logic
    }

    // Fetch the masked name of the current cocktail
    @GetMapping("/masked-name")
    public String getMaskedName() {
        return cocktailGame.getHiddenName(); // Return the masked cocktail name (with underscores)
    }

    // Fetch additional information about the current cocktail
    @GetMapping("/cocktail-info")
    public String getCocktailInfo() {
        return cocktailGame.getCurrentCocktailInfo(); // Return additional information about the current cocktail
    }
}
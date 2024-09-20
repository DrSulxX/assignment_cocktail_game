package com.ridango.game.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.ridango.game.model.Cocktail;
import com.ridango.game.service.CocktailService;
import com.ridango.game.repository.CocktailRepository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

@Component
public class CocktailGame {

    private final CocktailService cocktailService;
    private Set<String> usedCocktailIds = new HashSet<>();
    private Cocktail currentCocktail; // Current cocktail being guessed
    private String hiddenName; // Masked cocktail name for guessing
    private int attemptsLeft; // Number of attempts left for guessing

    private final CocktailRepository cocktailRepository;

    @Autowired
    public CocktailGame(CocktailService cocktailService, CocktailRepository cocktailRepository) {
        this.cocktailService = cocktailService;
        this.cocktailRepository = cocktailRepository;
        this.attemptsLeft = 5; // Initialize attempts
    }

    // Method to start a new game or reset game state
    public void startGame() {
        resetGame(); // Start with a clean slate
        getNewUniqueCocktail(); // Fetch a new cocktail for a fresh game start
    }

    // Fetch a new unique cocktail or return the current one if already set
    public Cocktail getNewUniqueCocktail() {
        Optional<Cocktail> existingCocktail = cocktailRepository.findFirstByOrderByIdAsc();
        if (existingCocktail.isPresent()) {
            currentCocktail = existingCocktail.get(); // Use the existing cocktail
        } else {
            currentCocktail = cocktailService.getRandomCocktail(); // Fetch new one
        }

        if (currentCocktail != null) {
            usedCocktailIds.add(currentCocktail.getIdDrink());
            this.hiddenName = maskName(currentCocktail.getStrDrink());
            this.attemptsLeft = 5; // Reset attempts for a new cocktail
        }
        return currentCocktail;
    }

    // Make a guess and check if it's correct
    public String makeGuess(String guess) {
        String cleanedGuess = cleanString(guess);
        String cleanedCocktailName = cleanString(currentCocktail.getStrDrink());

        if (cleanedGuess.equals(cleanedCocktailName)) {
            return "Correct! The cocktail is: " + currentCocktail.getStrDrink() + ". Would you like to continue? (yes/no)";
        } else {
            attemptsLeft--;
            preventNegativeAttempts(); // Ensure attempts don't go below zero

            if (attemptsLeft == 0) { 
                // This block should execute when attemptsLeft reaches 0
                String result = "Game over! The correct answer was: " + currentCocktail.getStrDrink();
                result += "\nWould you like to play again? (yes/no)";
                return result;
            } else {
                hiddenName = revealRandomLetter(currentCocktail.getStrDrink(), hiddenName);
                return "Wrong! Attempts left: " + attemptsLeft + ". Cocktail Name: " + hiddenName;
            }
        }
    }

    // Prevent negative attempts
    private void preventNegativeAttempts() {
        if (attemptsLeft < 0) {
            attemptsLeft = 0;
        }
    }

    // Reset the game for the next round or for game end
    private void resetGame() {
        clearDatabase(); // Clear the database each time the game is reset
        this.usedCocktailIds.clear();
        this.hiddenName = "";
        this.attemptsLeft = 5;
        this.currentCocktail = null;
    }

    // Method to clear the database (delete only the current cocktail)
    private void clearDatabase() {
        cocktailRepository.deleteAll(); // Clear the entire `TEMP` table after game over or reset
    }

    // Clean string by removing spaces and converting to lowercase
    private String cleanString(String input) {
        return input.trim().toLowerCase();
    }

    // Mask the cocktail name by replacing letters with underscores
    private String maskName(String name) {
        return name.replaceAll("[a-zA-Z]", "_");
    }

    // Randomly reveal one letter in the masked name after a wrong guess
    private String revealRandomLetter(String original, String masked) {
        char[] maskedChars = masked.toCharArray();
        char[] originalChars = original.toCharArray();
        Random random = new Random();

        // Ensure we only reveal an unrevealed letter
        boolean letterRevealed = false;
        while (!letterRevealed) {
            int randomIndex = random.nextInt(originalChars.length); // Ensure index is within bounds
            if (maskedChars[randomIndex] == '_') { // Only reveal if not already revealed
                maskedChars[randomIndex] = originalChars[randomIndex]; // Reveal one unrevealed letter
                letterRevealed = true;
            }
        }
        return new String(maskedChars);
    }

    // Get the masked cocktail name
    public String getHiddenName() {
        return hiddenName;
    }

    // Get additional information about the current cocktail
    public String getCurrentCocktailInfo() {
        if (currentCocktail == null) {
            return "No cocktail available.";
        }
        return "Category: " + currentCocktail.getStrCategory() + "\nGlass: " + currentCocktail.getStrGlass() +
                "\nImage: " + currentCocktail.getStrDrinkThumb();
    }
}
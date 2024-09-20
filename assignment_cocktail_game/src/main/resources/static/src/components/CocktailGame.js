import React, { useState } from 'react';
import CocktailGameService from '../services/CocktailGameService'; // Import the service

function CocktailGame() {
  const [cocktail, setCocktail] = useState(null);
  const [hiddenName, setHiddenName] = useState('');
  const [guess, setGuess] = useState('');
  const [message, setMessage] = useState('');
  const [score, setScore] = useState(0); // Score is carried over between games
  const [attemptsLeft, setAttemptsLeft] = useState(5); // Initialize attempts
  const [gameOver, setGameOver] = useState(false);
  const [continuePrompt, setContinuePrompt] = useState(false);
  const [gameStarted, setGameStarted] = useState(false); // Flag to control first game start
  const [submitDisabled, setSubmitDisabled] = useState(false); // Control submit button state
  const [showGameOverButtons, setShowGameOverButtons] = useState(false); // Control to show Game Over buttons

  // Start the game and fetch the first cocktail
  const startGame = (resetScore = true) => {
    CocktailGameService.startGame()
      .then(() => {
        fetchNewCocktail(); // Fetch a new cocktail on game start
        resetGame(resetScore); // Reset game state, decide whether to reset score
        setGameStarted(true); // Mark the game as started
        setSubmitDisabled(false); // Enable the submit button for the new game
        setShowGameOverButtons(false); // Reset the game over buttons visibility
      })
      .catch(error => {
        console.error("Error starting game:", error);
      });
  };

  // Reset game state
  const resetGame = (resetScore = true) => {
    setGameOver(false);
    setContinuePrompt(false);
    setAttemptsLeft(5);
    setGuess(''); // Clear previous guess
    setMessage(''); // Clear any messages
    if (resetScore) {
      setScore(0); // Reset score only when starting a completely new game, not when continuing
    }
  };

  // Fetch a new cocktail
  const fetchNewCocktail = () => {
    CocktailGameService.getCocktail()
      .then(data => {
        setCocktail(data); // Set the new cocktail
        fetchHiddenName(); // Fetch the masked name
      })
      .catch(error => {
        console.error("Error fetching cocktail:", error);
      });
  };

  // Submit the player's guess and handle the response
  const handleGuess = () => {
    CocktailGameService.submitGuess(guess)
      .then(data => {
        if (data.includes('Correct')) {
          setMessage(`Correct! The cocktail is: ${cocktail.strDrink}.`);
          setContinuePrompt(true); // Trigger the continuation prompt
          incrementScore(attemptsLeft); // Increment score based on attempts left
          setSubmitDisabled(true); // Disable the submit button after the correct guess
        } else {
          setMessage(data); // Only set the message for incorrect guesses
          setAttemptsLeft(prevAttempts => {
            const updatedAttempts = prevAttempts - 1;

            if (updatedAttempts === 0) {
              setMessage(`Game over! The correct cocktail was: ${cocktail.strDrink}`);
              setGameOver(true); // Game over when attempts run out
              setSubmitDisabled(true); // Disable the submit button after game over
              setShowGameOverButtons(true); // Show the game over buttons
            } else {
              fetchHiddenName(); // Reveal letters if the guess is incorrect
            }

            return updatedAttempts;
          });
        }
      })
      .catch(error => {
        console.error("Error making guess:", error);
      });
  };

  // Increment the score based on attempts left
  const incrementScore = (attemptsLeft) => {
    setScore(prevScore => prevScore + attemptsLeft); // Add attempts left to the score
  };

  // Fetch the hidden name
  const fetchHiddenName = () => {
    CocktailGameService.getMaskedName()
      .then(data => setHiddenName(data))
      .catch(error => console.error("Error fetching hidden name", error));
  };

  // Handle continue: fetch a new cocktail when continuing
  const handleContinue = (continueGame) => {
    if (continueGame) {
      startGame(false); // Start a new game, but don't reset the score
    } else {
      setGameOver(true); // Mark the game as over
      setMessage(`Game Over! Your final score is: ${score}`); // Display the final score
      setShowGameOverButtons(true); // Ensure the game over buttons are shown
    }
  };

  // Handle returning to the main screen after choosing not to continue
  const handleReturnToMain = () => {
    setGameStarted(false); // Reset the game state
    setShowGameOverButtons(false); // Hide game over buttons for the next game
  };

  const getIngredients = () => {
    if (!cocktail) return [];
    return [
      cocktail.strIngredient1,
      cocktail.strIngredient2,
      cocktail.strIngredient3,
    ].filter(ingredient => ingredient && ingredient.trim() !== '');
  };

  return (
    <div className="CocktailGame">
      <h1>Cocktail Game</h1>

      {/* Check if the game has started before rendering the game elements */}
      {!gameStarted ? (
        <div>
          <button onClick={() => startGame()}>Start Game</button>
        </div>
      ) : (
        <div>
          {cocktail && (
            <div>
              <p>Cocktail Instructions: {cocktail.strInstructions}</p>
              <p>Cocktail Name: {hiddenName}</p>
              <p>Attempts Left: {attemptsLeft}</p>
              <input
                type="text"
                value={guess}
                onChange={(e) => setGuess(e.target.value)}
                placeholder="Enter your guess"
              />
              <button
                onClick={handleGuess}
                disabled={submitDisabled} // Disable the button if the game is over or after winning
              >
                Submit Guess
              </button>
              <p>{message}</p>
              <p>Score: {score}</p>

              {(gameOver || continuePrompt) && (
                <div>
                  <h3>Additional Info about the Cocktail:</h3>
                  <p><strong>Category:</strong> {cocktail.strCategory}</p>
                  <p><strong>Glass:</strong> {cocktail.strGlass}</p>
                  <p><strong>Ingredients:</strong></p>
                  <ul>
                    {getIngredients().map((ingredient, index) => (
                      <li key={index}>{ingredient}</li>
                    ))}
                  </ul>
                  <p><strong>Picture:</strong> <img src={cocktail.strDrinkThumb} alt={cocktail.strDrink} width="100" /></p>
                </div>
              )}

              {continuePrompt && !gameOver && (
                <div>
                  <p>Would you like to continue? (yes/no)</p>
                  <button onClick={() => handleContinue(true)}>Yes</button>
                  <button onClick={() => handleContinue(false)}>No</button>
                </div>
              )}

              {/* Show "Start New Game" and "Return" buttons when game is over */}
              {showGameOverButtons && (
                <div>
                  <p>Game Over! Your final score is: {score}</p>
                  <button onClick={() => startGame()}>Start a New Game</button>
                  <button onClick={handleReturnToMain}>Return to Main Screen</button>
                </div>
              )}
            </div>
          )}
        </div>
      )}
    </div>
  );
}

export default CocktailGame;
import axios from 'axios';

class CocktailGameService {

  constructor() {
    this.isGameRunning = false; // Add a flag to prevent multiple start game calls
  }

  // Start a new game
  startGame() {
    if (this.isGameRunning) return Promise.resolve("Game is already running");

    this.isGameRunning = true; // Set the flag to true to indicate the game is in progress
    return axios.get('/game/start') // Use GET for starting the game
      .then(response => {
        this.isGameRunning = false; // Reset the flag after the game is started
        return response.data;
      })
      .catch(error => {
        this.isGameRunning = false; // Reset the flag if an error occurs
        console.error("Error starting game:", error);
        throw error;
      });
  }

  // Reset the flag when the game is over or the user decides to start a new game
  resetGameState() {
    this.isGameRunning = false; // Reset the game state to allow a new game
  }

  // Fetch a new unique cocktail or the current state of the game
  getCocktail() {
    return axios.get('/game/cocktail')
      .then(response => response.data)
      .catch(error => {
        console.error("Error fetching cocktail:", error);
        throw error;
      });
  }

  // Submit a guess to the backend
  submitGuess(guess) {
    return axios.post('/game/guess', null, { params: { guess } }) // Use POST to submit the guess
      .then(response => response.data)
      .catch(error => {
        console.error("Error submitting guess:", error);
        throw error;
      });
  }

  // Fetch the masked name of the current cocktail
  getMaskedName() {
    return axios.get('/game/masked-name')
      .then(response => response.data)
      .catch(error => {
        console.error("Error fetching masked name:", error);
        throw error;
      });
  }

  // Fetch additional information about the current cocktail
  getCocktailInfo() {
    return axios.get('/game/cocktail-info')
      .then(response => response.data)
      .catch(error => {
        console.error("Error fetching cocktail info:", error);
        throw error;
      });
  }
}

export default new CocktailGameService();
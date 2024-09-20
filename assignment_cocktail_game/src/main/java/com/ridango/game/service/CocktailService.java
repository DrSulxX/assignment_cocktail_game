package com.ridango.game.service;

import com.ridango.game.model.Cocktail;
import com.ridango.game.repository.CocktailRepository;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

@Service
public class CocktailService {

    private final String API_URL = "https://www.thecocktaildb.com/api/json/v1/1/random.php";
    private final Gson gson = new Gson();
    private final CocktailRepository cocktailRepository;

    @Autowired
    public CocktailService(CocktailRepository cocktailRepository) {
        this.cocktailRepository = cocktailRepository;
    }

    /**
     * Fetches a random cocktail from the API, saves it temporarily, and returns it.
     * 
     * @return Cocktail object containing the cocktail information.
     */
    public Cocktail getRandomCocktail() {
        try {
            // Fetch cocktail data from the API
            String jsonResponse = fetchCocktailData(API_URL);
            CocktailResponse response = gson.fromJson(jsonResponse, CocktailResponse.class);

            // Check if there is at least one cocktail in the response
            if (response.getDrinks() == null || response.getDrinks().isEmpty()) {
                throw new IllegalStateException("No cocktails found in the API response.");
            }

            // Get the first cocktail from the response (ensure only one is processed)
            Cocktail apiCocktail = response.getDrinks().get(0);

            // Create a new Cocktail object for temporary storage
            Cocktail cocktail = new Cocktail();
            cocktail.setIdDrink(apiCocktail.getIdDrink());
            cocktail.setStrDrink(apiCocktail.getStrDrink());
            cocktail.setStrInstructions(apiCocktail.getStrInstructions());
            cocktail.setStrCategory(apiCocktail.getStrCategory());
            cocktail.setStrGlass(apiCocktail.getStrGlass());
            cocktail.setStrDrinkThumb(apiCocktail.getStrDrinkThumb());

            // Set ingredients (Ensure only relevant ingredients are processed)
            cocktail.setStrIngredient1(apiCocktail.getStrIngredient1());
            cocktail.setStrIngredient2(apiCocktail.getStrIngredient2());
            cocktail.setStrIngredient3(apiCocktail.getStrIngredient3());

            // Save the cocktail to the temporary database
            cocktailRepository.save(cocktail);

            return cocktail;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Method to fetch data from the API
    protected String fetchCocktailData(String apiUrl) throws Exception {
        URI uri = new URI(apiUrl);
        URL url = uri.toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder content = new StringBuilder();
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        connection.disconnect();

        return content.toString();
    }

    // Method to clear the TEMP table
    public void clearTempTable() {
        cocktailRepository.deleteAll(); // Deletes all records from the TEMP table
    }

    // Inner class to represent the API response structure
    private class CocktailResponse {
        private java.util.List<Cocktail> drinks;

        public java.util.List<Cocktail> getDrinks() {
            return drinks;
        }
    }
}
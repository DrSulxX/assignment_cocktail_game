package com.ridango.game.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ridango.game.model.Cocktail;
import java.util.Optional;

@Repository
public interface CocktailRepository extends JpaRepository<Cocktail, Long> {

    // Fetch the first cocktail in the table, ordered by ID
    Optional<Cocktail> findFirstByOrderByIdAsc();
}
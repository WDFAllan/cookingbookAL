package com.example.repository;

import com.example.model.Recette;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecetteRepository extends JpaRepository<Recette, Integer> {

    @Query("SELECT r FROM Recette r " +
            "JOIN r.tags t " +
            "WHERE t IN :tags " +
            "GROUP BY r " +
            "HAVING COUNT(t) = :tagCount")
    List<Recette> findAllByTags(List<String> tags, Long tagCount);

    @Query("SELECT DISTINCT r.tags FROM Recette r")
    List<String> getAllTags();

}

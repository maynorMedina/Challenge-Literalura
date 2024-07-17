package com.aluraCursos.LiterAlura.repository;

import com.aluraCursos.LiterAlura.entities.Libros;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface ILibrosRepository extends JpaRepository<Libros, Long> {
    @Query("SELECT COUNT(l) FROM Libros l WHERE l.idioma = :idioma")
    long countByIdioma(@Param("idioma") String idioma);

    @Query("SELECT l FROM Libros l WHERE l.idioma = :idioma")
    List<Libros> buscarPorIdioma(@Param("idioma") String idioma);
}

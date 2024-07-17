package com.aluraCursos.LiterAlura.repository;

import com.aluraCursos.LiterAlura.entities.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IAutorRepository extends JpaRepository<Autor, Long> {
    @Query("SELECT a FROM Autor a WHERE (a.fechaDeNacimiento <= :anio AND (a.fechaDeFallecimiento IS NULL OR a.fechaDeFallecimiento >= :anio))")
    List<Autor> encontrarAutoresVivosEnAnio(@Param("anio") int anio);
}

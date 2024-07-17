package com.aluraCursos.LiterAlura.entities;

import com.aluraCursos.LiterAlura.model.DatosLibros;
import com.aluraCursos.LiterAlura.model.DatosAutor;
import jakarta.persistence.*;


@Entity
@Table(name = "libros")
public class Libros {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @Column(unique = true)
    private String titulo;
    private String autor;
    private String idioma;
    private Double numeroDeDescargas;

    public Libros(){}

    public Libros(DatosLibros datosLibros) {
        this.titulo = datosLibros.titulo();
        this.autor = getAutor(datosLibros).getNombre();
        this.idioma =getIdioma(datosLibros);
        this.numeroDeDescargas = datosLibros.numeroDeDescargas();
    }

    @Override
    public String toString(){
        return "----------LIBRO--------------\n" +
                "titulo: " + titulo + "\n" +
                "idioma: " + idioma + "\n" +
                "autor: " + autor + "\n" +
                "numeroDescargas: " + numeroDeDescargas + "\n" +
                "---------------------------------\n";
    }

    private String getIdioma(DatosLibros datosLibros) {
        return datosLibros.idiomas().get(0);
    }

    private Autor getAutor(DatosLibros datosLibros) {
        DatosAutor datosAutores = datosLibros.autor().get(0);
        return new Autor(datosAutores);
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
}

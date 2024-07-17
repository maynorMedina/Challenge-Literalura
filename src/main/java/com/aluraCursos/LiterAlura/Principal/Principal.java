package com.aluraCursos.LiterAlura.Principal;

import com.aluraCursos.LiterAlura.entities.Autor;
import com.aluraCursos.LiterAlura.entities.Libros;
import com.aluraCursos.LiterAlura.model.Datos;
import com.aluraCursos.LiterAlura.model.DatosLibros;
import com.aluraCursos.LiterAlura.repository.IAutorRepository;
import com.aluraCursos.LiterAlura.repository.ILibrosRepository;
import com.aluraCursos.LiterAlura.service.ConsumoApi;
import com.aluraCursos.LiterAlura.service.ConvierteDatos;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {

    private static final String URL_BASE = "https://gutendex.com/books/";
    private ConsumoApi consumoApi = new ConsumoApi();
    private ConvierteDatos conversor = new ConvierteDatos();

    private ILibrosRepository librosRepositorio;
    private IAutorRepository autorRepositorio;

    public Principal(ILibrosRepository librosRepository, IAutorRepository autorRepository) {
        this.librosRepositorio = librosRepository;
        this.autorRepositorio = autorRepository;
    }

    public void buscarLibroPorTitulo() {
        String tituloLibro = solicitarEntrada("Ingrese el nombre del libro que desea buscar:");
        String json = consumoApi.obtenerDatos(URL_BASE + "?search=" + tituloLibro.replace(" ", "+"));
        var datosBusqueda = conversor.obtenerDatos(json, Datos.class);

        Optional<DatosLibros> libroBuscado = datosBusqueda.resultados().stream()
                .filter(l -> l.titulo().toUpperCase().contains(tituloLibro.toUpperCase()))
                .findFirst();

        if (libroBuscado.isPresent()) {
            mostrarLibro(libroBuscado.get());
            guardarLibroYAutor(libroBuscado.get());
        } else {
            System.out.println("Libro no encontrado.");
        }
    }

    private void mostrarLibro(DatosLibros libro) {
        System.out.println("Libro encontrado:");
        System.out.println("Título: " + libro.titulo());

        // Asumiendo que 'libro.autor()' devuelve una lista de 'DatosAutor'
        String autores = libro.autor().stream()
                .map(datosAutor -> datosAutor.nombre()) // Usa el método 'nombre()' de DatosAutor
                .collect(Collectors.joining(", ")); // Une los nombres

        System.out.println("Autores: " + autores);
        System.out.println("Idioma: " + String.join(", ", libro.idiomas()));
        System.out.println("Número de descargas: " + libro.numeroDeDescargas());
        System.out.println("--------------------------------");
    }


    private void guardarLibroYAutor(DatosLibros libroBuscado) {
        Libros libro = new Libros(libroBuscado);

        // Mapeo de DatosAutor a Autor
        List<Autor> autores = libroBuscado.autor().stream()
                .map(datosAutor -> new Autor(datosAutor)) // Cambia aquí
                .toList();

        // Guardar libro y autores en la base de datos
        librosRepositorio.save(libro);
        autores.forEach(autorRepositorio::save);
        System.out.println("Libro y autores guardados exitosamente.");
    }


    public void mostrarLibrosPorIdioma() {
        String idiomaSeleccionado = solicitarEntrada("Ingrese el idioma para buscar los libros:\nes - español\nen - inglés\nfr - francés\npt - portugués");
        mostrarResultadosPorIdioma(idiomaSeleccionado);
    }

    private void mostrarResultadosPorIdioma(String idioma) {
        long cantidad = librosRepositorio.countByIdioma(idioma);
        List<Libros> libros = librosRepositorio.buscarPorIdioma(idioma);

        System.out.println("\nCantidad de libros en '" + idioma + "': " + cantidad + "\n");
        if (libros.isEmpty()) {
            System.out.println("No se encontraron libros en este idioma.");
        } else {
            libros.forEach(System.out::println);
        }
    }

    public void recuperarAutoresVivos() {
        int anio = Integer.parseInt(solicitarEntrada("Ingrese el año para buscar autores que estaban vivos:"));
        List<Autor> autores = autorRepositorio.encontrarAutoresVivosEnAnio(anio);

        if (autores.isEmpty()) {
            System.out.println("No se encontraron autores vivos en el año " + anio + ".");
        } else {
            System.out.println("Autores vivos en el año " + anio + ":");
            autores.forEach(System.out::println);
        }
    }

    public void mostrarLibrosBuscados() {
        List<Libros> libros = librosRepositorio.findAll();
        libros.stream()
                .sorted(Comparator.comparing(Libros::getTitulo))
                .forEach(System.out::println);
    }

    public void mostrarAutoresBuscados() {
        List<Autor> autores = autorRepositorio.findAll();
        autores.stream()
                .sorted(Comparator.comparing(Autor::getNombre))
                .forEach(System.out::println);
    }

    public void muestraDatos() {
        int opcion;
        do {
            mostrarMenu();
            opcion = Integer.parseInt(solicitarEntrada("Elija la opción a través de su número:"));
            ejecutarOpcion(opcion);
        } while (opcion != 0);
        System.out.println("Programa finalizado.");
    }

    private void ejecutarOpcion(int opcion) {
        switch (opcion) {
            case 1 -> buscarLibroPorTitulo();
            case 2 -> mostrarLibrosBuscados();
            case 3 -> mostrarAutoresBuscados();
            case 4 -> recuperarAutoresVivos();
            case 5 -> mostrarLibrosPorIdioma();
            default -> System.out.println("Opción no válida.");
        }
    }

    private static void mostrarMenu() {
        System.out.println("""
                ---------------------------
                1- Buscar libro por título.
                2- Listar libros registrados.
                3- Listar autores registrados.
                4- Listar autores vivos en un determinado año.
                5- Listar libros por idioma.
                0- salir
                -----------------------
                """);
    }

    private String solicitarEntrada(String mensaje) {
        System.out.println(mensaje);
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }
}

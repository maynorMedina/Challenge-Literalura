package com.aluraCursos.LiterAlura;

import com.aluraCursos.LiterAlura.Principal.Principal;
import com.aluraCursos.LiterAlura.repository.IAutorRepository;
import com.aluraCursos.LiterAlura.repository.ILibrosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LiterAluraApplication implements CommandLineRunner {

	@Autowired
	private ILibrosRepository librosRepository;
	@Autowired
	private IAutorRepository autorRepository;
	public static void main(String[] args) {
		SpringApplication.run(LiterAluraApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Principal principal = new Principal(librosRepository, autorRepository);
		principal.muestraDatos();
	}
}

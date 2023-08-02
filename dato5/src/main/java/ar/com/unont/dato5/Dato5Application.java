package ar.com.unont.dato5;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class Dato5Application implements CommandLineRunner {

	@Autowired
	private Dato5Setup datosetup;

	public static void main(String[] args) {
		SpringApplication.run(Dato5Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		log.info("lanzando desde Main");
		datosetup.lanzar();
	}

}
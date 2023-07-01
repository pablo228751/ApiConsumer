package ar.com.unont.dato5;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import ar.com.unont.dato5.service.IBarreraService;

@SpringBootApplication
public class Dato5Application implements CommandLineRunner{
	private static Logger LOG = LoggerFactory.getLogger(Dato5Application.class);
	@Autowired
	private IBarreraService barreraService;


	public static void main(String[] args) {
		SpringApplication.run(Dato5Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		LOG.info("Iniciando SISTEMA...");
		
		barreraService.mostrar();
	}

}

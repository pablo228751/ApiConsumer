package ar.com.unont.dato5;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import ar.com.unont.dato5.service.AnunciarTurnoService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class Dato5Application implements CommandLineRunner {

    @Autowired
    private DatoEventosSetup datoEventos;
    @Autowired
    private DatoTipoClienteSetup datoTipoCliente;
    @Autowired
    private AnunciarTurnoService anunciarTurno;

    

    public static void main(String[] args) {
        SpringApplication.run(Dato5Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
		
        log.info("Lanzando DatoEventos en un hilo 1 ");
        Thread hiloDatoEventos = new Thread(() -> datoEventos.lanzar());
        hiloDatoEventos.start();
		 

        log.info("Lanzando DatoTipoCliente en un hilo 2 ");
        Thread hiloDatoTipoCliente = new Thread(() -> datoTipoCliente.lanzar());
        hiloDatoTipoCliente.start();
        
        log.info("Lanzando AnunciarTurno en un hilo 3 ");
        Thread hiloAnunciarTurno = new Thread(() -> anunciarTurno.lanzar());
        hiloAnunciarTurno.start();
        
    }
}

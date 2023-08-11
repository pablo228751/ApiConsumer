package ar.com.unont.dato5;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ar.com.unont.dato5.entity.Barrera;
import ar.com.unont.dato5.entity.Eventos;
import ar.com.unont.dato5.entity.RegisteredUserResponse;
import ar.com.unont.dato5.entity.Turnero;
import ar.com.unont.dato5.entity.Turno;
import ar.com.unont.dato5.service.IBarreraService;
import ar.com.unont.dato5.service.ITurneroService;
import ar.com.unont.dato5.service.ConsumirApi;
import ar.com.unont.dato5.service.DatabaseService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import java.sql.SQLException;
import java.time.LocalDate;

import ar.com.unont.dato5.service.IEventosService;

@Slf4j
@Data
@Service
public class DatoEventosSetup {

    public static String token;
    // public static int dia = 1;
    public static boolean expiro = true;
    private String fec;
    private List<Turno> turnosDiarios;
    private List<Barrera> barreras;
    private int pagina = 1;

    @Autowired
    private IBarreraService barreraService;
    @Autowired
    private ConsumirApi consumirApi;
    @Autowired
    private final ITurneroService turneroService;
    @Autowired
    private final IEventosService eventosService;
    @Autowired
    private final DatabaseService databaseService;

    public DatoEventosSetup(ITurneroService turneroService, IEventosService eventosService, DatabaseService databaseService) {
        this.turneroService = turneroService;
        this.eventosService = eventosService;
        this.databaseService = databaseService;
    }

    public void lanzar() {
        log.info("Iniciando SISTEMA...");
        barreras = barreraService.mostrar();
        /*
        for (Barrera barrera : barreras){
            System.out.println("barrera-> "+barrera.getIp()+ "Activa-> "+barrera.getActiva());
        }
         */
        //System.exit(0);
        // TurnosMemo recupera una lista de Turnos en la base Mysql
        turnosMemo();
        
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while (true) {

            if (expiro) {
                RegisteredUserResponse responseBody = consumirApi.generarToken();
                log.info(responseBody.toString());
            } else {
                log.info("La Credencial es valida, realizando Consulta");
                Map<String, String> parametros = new HashMap<>();

                LocalDate currentDate = LocalDate.now();
                fec = currentDate.toString();

                // Fecha para pruebas...
                //fec = "2023-08-09";
                /*
                 * if (dia < 10){
                 * fec= "2023-08-0" + String.valueOf(dia);
                 * 
                 * }else{
                 * fec= "2023-08-" + String.valueOf(dia);
                 * }
                 */

                log.info("FEC: " + fec);
                parametros.put("fecha", fec);
                parametros.put("centroAtencion", "1770");
                parametros.put("pagina", String.valueOf((int) pagina));
                // dia++;

                Turnero turnero = consumirApi.consultaTurnos(parametros);
                if (turnero != null) {

                    System.out.println("**************************************************************");
                    System.out.println("ID: " + turnero.getId());
                    System.out.println("Paginas: " + turnero.getPagina());
                    System.out.println("TotalPag: " + turnero.getTotalPaginas());
                    System.out.println("Registros: " + turnero.getRegistros());
                    System.out.println("Resultados: " + turnero.getResultados());
                    System.out.println("**************************************************************");

                    if (turnero.getRegistros() > 0) {
                        // Guardar el objeto Turnero en la base de datos
                        if (persistTurnero(turnero)) {
                            System.out.println("");

                            // Guardar en eventos en todas las barreras (molinetes)
                            persistEventos(barreras, turnero.getResultados());

                        }
                        pagina++;
                    } else {
                        pagina = 1;
                        log.info("SIN TURNOS, pagina= " + pagina);
                    }
                }else{
                    System.out.println("Errorr TURNERO NULO");
                }
            }

            // Sleep para pruebas
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean persistTurnero(Turnero turnero) {
        boolean reg = false;

        Iterator<Turno> iterator = turnero.getResultados().iterator();
        while (iterator.hasNext()) {
            Eventos evento = new Eventos();
            Turno turno = iterator.next();
            if (turnosDiarios != null && !turnosDiarios.isEmpty()) {
                boolean turnoExiste = false;
                for (Turno turnoDia : turnosDiarios) {
                    if (turno.getTurnoId().equals(turnoDia.getTurnoId())) {
                        turnoExiste = true;
                        break;
                    }
                }

                if (!turnoExiste) {
                    turneroService.insertTurnero(turnero);
                    evento.setEventoId(turno.getTurnoId());
                    evento.setDni(turno.getDni());
                    evento.setFechaEntrada(turno.getFechaActualizacion());
                    evento.setPago("SI");
                    eventosService.insertarEvento(evento);
                    turnosDiarios.add(turno);
                    reg = true;
                } else {
                    // Si el turno existe, se quita de turnero.getResultados()
                    iterator.remove();
                    turnero.setRegistros(turnero.getRegistros() - 1);
                }
            } else {
                turneroService.insertTurnero(turnero);
                evento.setEventoId(turno.getTurnoId());
                evento.setDni(turno.getDni());
                evento.setFechaEntrada(turno.getFechaActualizacion());
                evento.setPago("SI");
                eventosService.insertarEvento(evento);
                turnosDiarios.add(turno);
                reg = true;
            }
        }
        return reg;
    }

    private void turnosMemo() {
        turnosDiarios = turneroService.selectTurnos();
        for (Turno turno : turnosDiarios) {
            log.info(turno.toString());
        }
    }

    private void persistEventos(List<Barrera> barreras, List<Turno> turnero) {
        for (Barrera barrera : barreras) {
            int errores = 0; // cantidad de errores en un molinete
            for (Turno turno : turnero) {
                log.info("# BARRERA " + barrera.getIp() + " GUARDAR ->" + turno.getTurnoId());
                Long eventoId = turno.getTurnoId();
                String dni = turno.getDni();
                String pago = "SI";
                String query = "INSERT INTO eventos (evento_id, dni, fecha_entrada, pago) VALUES ('" + eventoId + "', '" + dni + "', '" + turno.getFechaActualizacion() +"', '" + pago + "') ON DUPLICATE KEY UPDATE dni = '" + dni + "', fecha_entrada = '" + turno.getFechaActualizacion() + "', pago = '" + pago + "'";
                String ip = barrera.getIp().trim();
                int puerto = Integer.valueOf(barrera.getPuerto().trim());
                String usr = barrera.getUsuario().trim();
                String pass = barrera.getClave().trim();
                String db = barrera.getBase().trim();

                try {
                    boolean isConnected = databaseService.openConnection(ip, puerto, usr, pass, db);
                    if (isConnected) {
                        int rowsAffected = databaseService.executeUpdate(query);
                        if (rowsAffected > 0) {
                            log.info("Se guardo correctamente el evento. Barrera: " + barrera.getIp());
                        } else {
                            log.error("ERROR AL GRABAR EN la barrera: " + barrera.getIp());
                            if (errores > 1) {
                                break;
                            } else {
                                errores++;
                            }
                        }
                    }
                } catch (SQLException e) {
                    log.error("ERROR! al conectar con la barrera: " + barrera.getIp());
                    break;
                } finally {
                    databaseService.closeConnection();
                }
            }
        }
    }

}

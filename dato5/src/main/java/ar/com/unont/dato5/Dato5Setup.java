package ar.com.unont.dato5;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.stereotype.Service;
import ar.com.unont.dato5.entity.Barrera;
import ar.com.unont.dato5.entity.RegisteredUserResponse;
import ar.com.unont.dato5.entity.Turnero;
import ar.com.unont.dato5.entity.Turno;
import ar.com.unont.dato5.service.IBarreraService;
import ar.com.unont.dato5.service.ITurneroService;
import ar.com.unont.dato5.service.ConsumirApi;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.sql.DataSource;
import ar.com.unont.dato5.entity.Eventos;
import ar.com.unont.dato5.service.IEventosService;

@Slf4j
@Data
@Service
public class Dato5Setup {

    public static String token;
    public static int dia = 1;
    public static boolean expiro = true;
    private String fec;
    private List<Turno> turnosDiarios;
    private List<Barrera> barreras;
    private Map<Long, DataSource> dataSourcesByBarreraId = new HashMap<>();

    @Autowired
    private IBarreraService barreraService;
    @Autowired
    private ConsumirApi consumirApi;
    @Autowired
    private final ITurneroService turneroService;
    @Autowired
    private final IEventosService eventosService;

    public Dato5Setup(ITurneroService turneroService, IEventosService eventosService) {
        this.turneroService = turneroService;
        this.eventosService = eventosService;
    }

    public void lanzar() {
        log.info("Iniciando SISTEMA...");
        barreras = barreraService.mostrar();
        // TurnosMemo recupera una lista de Turnos en la base Mysql
        turnosMemo();
        // System.exit(0);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        while (true) {

            if (expiro) {
                RegisteredUserResponse responseBody = consumirApi.generarToken();
                log.info(responseBody.toString());
            } else {
                log.info("La Credencial es valida, realizando Consulta");
                Map<String, String> parametros = new HashMap<>();

                // LocalDate currentDate = LocalDate.now();
                // fec = currentDate.toString();

                // Fecha para pruebas...
                fec = "2023-08-09";
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
                dia++;

                Turnero turnero = consumirApi.consultaTurnos(parametros);

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
                        /*
                        // Guardar en eventos en todas las barreras (molinetes)
                        for (Barrera barrera : barreras) {
                            DataSource dataSource = createDataSourceForBarrera(barrera);
                            dataSourcesByBarreraId.put(barrera.getBarrera_id(), dataSource);
                        }

                        for (Barrera barrera : barreras) {
                            for (Turno turno : turnero.getResultados()) {
                                persistEventos(barrera, turno);
                            }
                        }
                        */

                    }

                } else {
                    log.info("SIN TURNOS");
                }
            }

            // Sleep para pruebas
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private boolean persistTurnero(Turnero turnero) {
        boolean reg=false;
        Iterator<Turno> iterator = turnero.getResultados().iterator();
        while (iterator.hasNext()) {
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
                    turnosDiarios.add(turno);
                    reg=true;
                } else {
                    // Si el turno existe, se quita de turnero.getResultados()
                    iterator.remove();
                    turnero.setRegistros(turnero.getRegistros() - 1);
                }
            } else {
                turneroService.insertTurnero(turnero);
                turnosDiarios.add(turno);
                reg=true;
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

    private void persistEventos(Barrera barrera, Turno turno) {
        DataSource dataSource = dataSourcesByBarreraId.get(barrera.getBarrera_id());

        try (Connection connection = dataSource.getConnection()) {
            Eventos evento = new Eventos();
            evento.setEventoId(turno.getTurnoId());
            evento.setBarreraEntrada(barrera.getNrobarrera());
            evento.setTarjeta("");
            evento.setDni(turno.getDni());
            evento.setE_s('E');
            evento.setPago("SI");
            evento.setFechaEntrada(null);
            evento.setHoraEntrada(null);

            // Insertar el evento en la base de datos utilizando el servicio de eventos
            eventosService.insertarEvento(evento);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void insertarEvento(Connection connection, Eventos evento) throws SQLException {
        String sql = "INSERT INTO eventos (evento_id, barrera_entrada, tarjeta, dni, e_s, fecha_entrada, hora_entrada, pago, fecha_salida, barrera_salida) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, evento.getEventoId());
            preparedStatement.setInt(2, evento.getBarreraEntrada());
            preparedStatement.setString(3, evento.getTarjeta());
            preparedStatement.setString(4, evento.getDni());
            preparedStatement.setString(5, String.valueOf(evento.getE_s()));
            preparedStatement.setDate(6, null); 
            preparedStatement.setTime(7, null); 
            preparedStatement.setString(8, evento.getPago());
            preparedStatement.setDate(9, null);
            preparedStatement.setInt(10, evento.getBarreraSalida());

            preparedStatement.executeUpdate();
        }
    }

    private DataSource createDataSourceForBarrera(Barrera barrera) {
        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName("com.mysql.cj.jdbc.Driver"); // conf el driver correcto para  la DB
        dataSourceBuilder.url("jdbc:mysql://" + barrera.getIp() + ":" + barrera.getPuerto() + "/" + barrera.getBase());
        dataSourceBuilder.username(barrera.getUsuario());
        dataSourceBuilder.password(barrera.getClave());
        return dataSourceBuilder.build();
    }
}

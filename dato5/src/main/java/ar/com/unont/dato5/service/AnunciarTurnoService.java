package ar.com.unont.dato5.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ar.com.unont.dato5.entity.Barrera;
import ar.com.unont.dato5.entity.Eventos;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AnunciarTurnoService {
    private List<Barrera> barreras;
    private List<Eventos> eventosNoAnunciados;

    @Autowired
    private IBarreraService barreraService;
    @Autowired
    private ConsumirApi consumirApi;
    @Autowired
    private final IEventosService eventosService;
    @Autowired
    private final DatabaseService databaseService;

    public AnunciarTurnoService(IEventosService eventosService, DatabaseService databaseService) {
        this.eventosService = eventosService;
        this.databaseService = databaseService;
    }

    public void lanzar() {
        while (true) {
            barreras = barreraService.mostrar();
            eventosNoAnunciados = eventosService.eventoSinHoraEntrada();
            if (!eventosNoAnunciados.isEmpty()) {
                System.out.println("Se encontraron registros:");
                for (Eventos even : eventosNoAnunciados) {
                    System.out.println(
                            "Evento_id -> ---->" + even.getEventoId() + "<---- Fecha -> " + even.getFechaEntrada().toString());
                }
                buscarEventosAnunciados(eventosNoAnunciados, barreras);
            }

            espera();
        }

    }

    public boolean buscarEventosAnunciados(List<Eventos> eventosNoAnunciados, List<Barrera> barreras) {
        boolean rta = false;
        List<Eventos> resultados = new ArrayList<>();

        for (Barrera barrera : barreras) {
            String ip = barrera.getIp().trim();
            int puerto = Integer.parseInt(barrera.getPuerto().trim());
            String usr = barrera.getUsuario().trim();
            String pass = barrera.getClave().trim();
            String db = barrera.getBase().trim();

            try {
                boolean isConnected = databaseService.openConnection(ip, puerto, usr, pass, db);
                if (isConnected) {
                    List<Long> eventoIds = eventosNoAnunciados.stream()
                            .map(Eventos::getEventoId)
                            .collect(Collectors.toList());

                    String ids = String.join(",", eventoIds.stream()
                            .map(String::valueOf)
                            .collect(Collectors.toList()));

                    String query = "SELECT evento_id, barrera_entrada, tarjeta, dni, e_s, fecha_entrada, hora_entrada, pago, fecha_salida, hora_salida, barrera_salida "
                            +
                            "FROM eventos " +
                            "WHERE evento_id IN (" + ids + ") " +
                            "AND hora_entrada IS NOT NULL AND hora_entrada <> ''";

                    ResultSet resultSet = databaseService.executeQuery(query);

                    if (resultSet != null) {
                        while (resultSet.next()) {
                            Eventos evento = new Eventos();
                            evento.setEventoId(resultSet.getLong("evento_id"));
                            evento.setBarreraEntrada(resultSet.getInt("barrera_entrada"));
                            evento.setTarjeta(resultSet.getString("tarjeta"));
                            evento.setDni(resultSet.getString("dni"));
                            evento.setE_s(
                                    resultSet.getString("e_s") != null ? resultSet.getString("e_s").charAt(0) : ' ');

                            if (resultSet.getDate("fecha_entrada") != null) {
                                evento.setFechaEntrada(resultSet.getDate("fecha_entrada").toLocalDate());
                            } else {
                                evento.setFechaEntrada(null);
                            }

                            if (resultSet.getTime("hora_entrada") != null) {
                                evento.setHoraEntrada(resultSet.getTime("hora_entrada").toLocalTime());
                            } else {
                                evento.setHoraEntrada(null);
                            }

                            evento.setPago(resultSet.getString("pago"));

                            if (resultSet.getDate("fecha_salida") != null) {
                                evento.setFechaSalida(resultSet.getDate("fecha_salida").toLocalDate());
                            } else {
                                evento.setFechaSalida(null);
                            }

                            if (resultSet.getTime("hora_salida") != null) {
                                evento.setHoraSalida(resultSet.getTime("hora_salida").toLocalTime());
                            } else {
                                evento.setHoraSalida(null);
                            }

                            evento.setBarreraSalida(resultSet.getInt("barrera_salida"));

                            resultados.add(evento);
                        }
                    }
                }
            } catch (SQLException e) {
                log.error("ERROR al seleccionar los legajos de la barrera: " + ip, e);
                return false;
            } finally {
                databaseService.closeConnection();
            }
        }
        if(!resultados.isEmpty()){

            for (Eventos evento : resultados) {
                System.out.println("Evento_id: " + evento.getEventoId());
                System.out.println("Barrera_entrada: " + evento.getBarreraEntrada());
                System.out.println("Tarjeta: " + evento.getTarjeta());
                System.out.println("DNI: " + evento.getDni());
                System.out.println("E/S: " + evento.getE_s());
                System.out.println("Fecha_entrada: " + evento.getFechaEntrada());
                System.out.println("Hora_entrada: " + evento.getHoraEntrada());
                System.out.println("Pago: " + evento.getPago());
                System.out.println("Fecha_salida: " + evento.getFechaSalida());
                System.out.println("Hora_salida: " + evento.getHoraSalida());
                System.out.println("Barrera_salida: " + evento.getBarreraSalida());
                System.out.println();
            }
            anunciarTurnoApi(resultados);
        }


        return rta;
    }

    private boolean anunciarTurnoApi(List<Eventos> resultados){
        for(Eventos even : resultados){
            consumirApi.actualizarEstadoTurno(even.getEventoId().toString());
        }
        
        return true;
    }

    private void espera() {
        // Sleep para pruebas
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

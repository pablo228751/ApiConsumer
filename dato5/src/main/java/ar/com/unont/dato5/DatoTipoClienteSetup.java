package ar.com.unont.dato5;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ar.com.unont.dato5.entity.Barrera;
import ar.com.unont.dato5.entity.TipoCliente;
import ar.com.unont.dato5.service.DatabaseService;
import ar.com.unont.dato5.service.IBarreraService;
import ar.com.unont.dato5.service.ITipoClienteService;
import lombok.extern.slf4j.Slf4j;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class DatoTipoClienteSetup {

    private List<TipoCliente> tipoClientes;

    private List<Barrera> barreras;

    private final ITipoClienteService tipoClienteService;

    @Autowired
    private IBarreraService barreraService;
    @Autowired
    private final DatabaseService databaseService;

    public DatoTipoClienteSetup(ITipoClienteService tipoClienteService, DatabaseService databaseService) {
        this.tipoClienteService = tipoClienteService;
        this.databaseService = databaseService;
    }

    public void lanzar() {
        boolean aux = true;

        while (true) {
            tipoClientes = tipoClienteService.seleccionarTodo();
            barreras = barreraService.mostrar();

            /* SOLO PARA SINCRO COMPLETA DE TIPO_CLIENTE 
            if (aux) {
                if (sincroTipoClienteMolinetes()) {
                    log.info("SINCRO EXITOSA!!");
                    aux = false;
                } else {
                    log.error("ERROR AL SINCRONIZAR");
                }
            }  
            */          

            if (tipoClientes.isEmpty()) {
                log.warn("No se encontraron registros de TipoCliente.");
                tipoClientes = tipoClienteService.seleccionarTodo();
                espera();
                continue;
            }

            for (TipoCliente tipoCliente : tipoClientes) {
                int estado = tipoCliente.getEstado();
                if (estado == 0) {
                    insertEnMolinetes(tipoCliente);
                } else if (estado == 3) {
                    deleteEnMolinetes(tipoCliente);
                }
            }

            espera();
        }

    }

    private void insertEnMolinetes(TipoCliente tipoCliente) {
        for (Barrera barrera : barreras) {
            String categoria = tipoCliente.getCategoria();
            String tarjeta = tipoCliente.getTarjeta();
            String legajo = tipoCliente.getLegajo();
            String nombre = tipoCliente.getNombre();
            Character e_s = tipoCliente.getE_s();

            String query = "INSERT INTO tipo_cliente (categoria, tarjeta, legajo, nombre, e_s) VALUES ('"
                    + categoria + "', '" + tarjeta + "', '" + legajo + "', '" + nombre + "', '" + e_s
                    + "') ON DUPLICATE KEY UPDATE categoria = '"
                    + categoria + "', tarjeta = '" + tarjeta + "', nombre = '" + nombre + "', e_s = '" + e_s + "'";
            if (ejecutarConsulta(query, barrera)) {
                tipoClienteService.actualizarEstadoTipoCliente(tipoCliente.getId(), 5);
            }
        }
    }

    private void deleteEnMolinetes(TipoCliente tipoCliente) {
        for (Barrera barrera : barreras) {
            String legajo = tipoCliente.getLegajo().trim();
            String deleteQuery = "DELETE FROM tipo_cliente WHERE legajo = '" + legajo + "'";
            if (ejecutarConsulta(deleteQuery, barrera)) {
                tipoClienteService.actualizarEstadoTipoCliente(tipoCliente.getId(), 4);
            }
        }
    }

    private boolean sincroTipoClienteMolinetes() {
        List<Integer> estados = Arrays.asList(4);
        List<TipoCliente> tipoClientesEstado = tipoClienteService.seleccionarPorEstados(estados);
        boolean procesado = false;

        for (Barrera barrera : barreras) {
            // ELIMINAR REGISTROS MASIVAMENTE
            if (!tipoClientesEstado.isEmpty()) {
                List<String> registros = new ArrayList<>();
                for (TipoCliente tipoCliente : tipoClientesEstado) {
                    registros.add("'" + tipoCliente.getLegajo().trim() + "'");
                    // Si la lista alcanza 200 registros o si ya no hay más registros por procesar,
                    // ejecutar el delete
                    if (registros.size() >= 200) {
                        String legajos = String.join(",", registros);
                        String query = "DELETE FROM tipo_cliente WHERE legajo IN (" + legajos + ")";
                        boolean eliminacionExitosa = ejecutarConsulta(query, barrera);
                        if (eliminacionExitosa) {
                            procesado = true;
                        }
                        registros.clear(); // Vaciar la lista para el siguiente lote
                    }
                }
                if (!registros.isEmpty()) {
                    String legajos = String.join(",", registros);
                    String query = "DELETE FROM tipo_cliente WHERE legajo IN (" + legajos + ")";
                    boolean eliminacionExitosa = ejecutarConsulta(query, barrera);
                    if (eliminacionExitosa) {
                        procesado = true;
                    }
                    registros.clear(); // Vaciar la lista
                }
            }

            // AGREGAR REGISTROS
            List<TipoCliente> tipoClientesInsert;
            List<Integer> estados2 = Arrays.asList(0, 5);
            tipoClientesInsert = tipoClienteService.seleccionarPorEstados(estados2);
            List<String> regBarrera = seleccionarLegajoMolinetes(barrera);
            if (regBarrera != null && !regBarrera.isEmpty()) {
                List<TipoCliente> tipoClienteFiltrar = new ArrayList<>();
                for (TipoCliente tc : tipoClientesInsert) {
                    boolean legajoEncontrado = false;
                    for (String rb : regBarrera) {
                        if (rb.trim().equals(tc.getLegajo().trim())) {
                            legajoEncontrado = true;
                            break;
                        }
                    }
                    if (!legajoEncontrado) {
                        tipoClienteFiltrar.add(tc); // Agregar a tipoClienteFiltra si el legajo no fue encontrado
                    }
                }
                if (!tipoClienteFiltrar.isEmpty()) {
                    if (insertTC(tipoClienteFiltrar, barrera)) {
                        procesado = true;
                    }
                }
            }
        }

        return procesado;
    }

    private boolean ejecutarConsulta(String query, Barrera barrera) {
        boolean eliminado = false;
        String ip = barrera.getIp().trim();
        int puerto = Integer.parseInt(barrera.getPuerto().trim());
        String usr = barrera.getUsuario().trim();
        String pass = barrera.getClave().trim();
        String db = barrera.getBase().trim();
        try {
            boolean isConnected = databaseService.openConnection(ip, puerto, usr, pass, db);
            if (isConnected) {
                int rowsAffected = databaseService.executeUpdate(query);
                if (rowsAffected > 0) {
                    eliminado = true;
                } else {
                    log.error("ERROR en Molinete:  " + ip);
                }
            }
        } catch (SQLException e) {
            return eliminado;
        } finally {
            databaseService.closeConnection();
        }
        return eliminado;
    }

    private boolean insertTC(List<TipoCliente> tipoC, Barrera barrera) {
        boolean est = false;
        String ip = barrera.getIp().trim();
        int puerto = Integer.parseInt(barrera.getPuerto().trim());
        String usr = barrera.getUsuario().trim();
        String pass = barrera.getClave().trim();
        String db = barrera.getBase().trim();

        try {
            for (TipoCliente tc : tipoC) {
                String categoria = tc.getCategoria();
                String tarjeta = tc.getTarjeta();
                String legajo = tc.getLegajo();
                String nombre = tc.getNombre();
                Character e_s = tc.getE_s();

                String query = "INSERT INTO tipo_cliente (categoria, tarjeta, legajo, nombre, e_s) VALUES ('"
                        + categoria + "', '" + tarjeta + "', '" + legajo + "', '" + nombre + "', '" + e_s
                        + "') ON DUPLICATE KEY UPDATE categoria = '"
                        + categoria + "', tarjeta = '" + tarjeta + "', nombre = '" + nombre + "', e_s = '" + e_s + "'";

                boolean isConnected = databaseService.openConnection(ip, puerto, usr, pass, db);
                if (isConnected) {
                    int rowsAffected = databaseService.executeUpdate(query);
                    if (rowsAffected > 0) {
                        est = true;
                    }
                }

            }
        } catch (SQLException e) {
            return est;
        } finally {
            databaseService.closeConnection();
        }
        return est;
    }

    public List<String> seleccionarLegajoMolinetes(Barrera barrera) {
        List<String> legajos = new ArrayList<>();
        String ip = barrera.getIp().trim();
        int puerto = Integer.parseInt(barrera.getPuerto().trim());
        String usr = barrera.getUsuario().trim();
        String pass = barrera.getClave().trim();
        String db = barrera.getBase().trim();

        try {
            boolean isConnected = databaseService.openConnection(ip, puerto, usr, pass, db);
            if (isConnected) {
                ResultSet resultSet = databaseService.executeQuery("SELECT legajo FROM tipo_cliente");
                if (resultSet != null) {

                    while (resultSet.next()) {
                        String legajo = resultSet.getString("legajo");
                        if (legajo != null && !legajo.trim().isEmpty()) {
                            legajos.add(legajo.trim());
                        }
                    }
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            log.error("ERROR al seleccionar los legajos de la barrera: " + ip, e);
            return legajos;
        } finally {
            databaseService.closeConnection();
        }

        return legajos;
    }

    private void espera() {
        // Sleep para pruebas
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

package ar.com.unont.dato5;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ar.com.unont.dato5.entity.RegisteredUserResponse;
import ar.com.unont.dato5.entity.Turnero;
import ar.com.unont.dato5.entity.Turno;
import ar.com.unont.dato5.service.IBarreraService;
import ar.com.unont.dato5.service.ITurneroService;
import ar.com.unont.dato5.service.ConsumirApi;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@Service
public class Dato5Setup {

    public static String token;
    public static int dia = 1;
    public static boolean expiro = true;
    private String fec;
    private List<Turno> turnosDiarios;

    @Autowired
    private IBarreraService barreraService;
    @Autowired
    private ConsumirApi consumirApi;
    @Autowired
    private final ITurneroService turneroService;

    public Dato5Setup(ITurneroService turneroService) {
        this.turneroService = turneroService;

    }

    public void lanzar() {
        if (expiro) {
            log.info("Iniciando SISTEMA...");
            //TurnosMemo recupera una lista de Turnos en la base Mysql
            //turnosMemo();

            barreraService.mostrar();
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
                boolean turnoIdNoExiste = turnoIdExiste(turnero.getResultados());
                if (turnoIdNoExiste) {
                    // Guardar el objeto Turnero en la base de datos
                    turneroService.insertTurnero(turnero);
                }

            } else {
                log.info("SIN TURNOS");
            }

            // Cortar momentáneamente el flujo del programa
            // system.exit(0);
        }
    }

    private boolean turnoIdExiste(List<Turno> turnos) {
        // for (Turno turno : turnos) {
        // return true;
        // }
        return true;
    }

    private void turnosMemo() {
        turnosDiarios = turneroService.selectTurnos();
        for (Turno turno : turnosDiarios) {
            log.info("TurnoID: " + turno.getTurnoId());
            log.info("FechaActualizacion: " + turno.getFechaActualizacion());
            log.info("Nombre: " + turno.getNombreCompleto());
            log.info("FechaTurno: " + turno.getFechaTurno());
        }
        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}

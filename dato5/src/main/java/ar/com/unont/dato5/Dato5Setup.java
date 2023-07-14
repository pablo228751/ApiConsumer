package ar.com.unont.dato5;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ar.com.unont.dato5.entity.RegisteredUserResponse;
import ar.com.unont.dato5.entity.Turnero;
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
    public static boolean expiro = true;

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
            barreraService.mostrar();
            RegisteredUserResponse responseBody = consumirApi.generarToken();
            log.info(responseBody.toString());
        } else {
            log.info("La Credencial es valida, realizando Consulta");
            Map<String, String> parametros = new HashMap<>();
            parametros.put("fecha", "2023-07-14");
            parametros.put("centroAtencion", "1770");

            Turnero turnero = consumirApi.consultaTurnos(parametros);

            System.out.println("**************************************************************");
            System.out.println("ID: " + turnero.getId());
            System.out.println("Paginas: " + turnero.getPagina());
            System.out.println("TotalPag: " + turnero.getTotalPaginas());
            System.out.println("Registros: " + turnero.getRegistros());
            System.out.println("Resultados: " + turnero.getResultados());
            System.out.println("**************************************************************");

            // Guardar el objeto Turnero en la base de datos
            turneroService.insertTurnero(turnero);

            // Cortar momentáneamente el flujo del programa
            System.exit(0);
        }
    }
}

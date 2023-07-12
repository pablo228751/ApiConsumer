package ar.com.unont.dato5;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.com.unont.dato5.entity.RegisteredUserResponse;
import ar.com.unont.dato5.entity.Turnero;
import ar.com.unont.dato5.service.IBarreraService;
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

    public void lanzar() {
        if (expiro) {

            log.info("Iniciando SISTEMA...");
            barreraService.mostrar();
            // log.info(consumirApi.generarToken().toString());
            RegisteredUserResponse responseBody = consumirApi.generarToken();
            log.info(responseBody.toString());
            // log.info("VALOR DE TOKEN: " + token + " \n SISTEMA FINALIZADO.");
        } else {
            log.info("La Credencial es valida, realizando Consulta");
            Map<String, String> parametros = new HashMap<>();
            parametros.put("fecha", "2023-06-07");
            parametros.put("centroAtencion", "1770");
            //parametros.put("dni", "123456789");
            //parametros.put("fechaUltimaConsulta", "2023-06-01T00:00:00");
            //parametros.put("pagina", "1");
            //parametros.put("registros", "50");

            log.info(consumirApi.consultaTurnos(parametros).toString());

        }
    }

}

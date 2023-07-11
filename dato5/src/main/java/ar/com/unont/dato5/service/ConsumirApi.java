package ar.com.unont.dato5.service;

import java.util.Map;

import ar.com.unont.dato5.entity.RegisteredUserResponse;
import ar.com.unont.dato5.entity.Turnero;

public interface ConsumirApi {
    RegisteredUserResponse generarToken();

    Turnero consultaTurnos(Map<String, String> parametros);    

}

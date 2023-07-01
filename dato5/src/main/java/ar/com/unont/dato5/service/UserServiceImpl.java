package ar.com.unont.dato5.service;

import javax.management.RuntimeErrorException;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import ar.com.unont.dato5.entity.AbstractClient;
import ar.com.unont.dato5.entity.LoginRequest;
import ar.com.unont.dato5.entity.LoginResponse;
import ar.com.unont.dato5.entity.RegisterUserRequest;
import ar.com.unont.dato5.entity.RegisteredUserResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserServiceImpl extends AbstractClient implements UserService {

    public UserServiceImpl(RestTemplate restTemplate) {
        super(restTemplate);
    }

    @Override
    public RegisteredUserResponse generarToken(RegisterUserRequest user) {
        String uri = baseUrl + "/";
        log.info("Generando token para: " + user);
        ResponseEntity<RegisteredUserResponse> response = restTemplate.postForEntity(uri, user,
                RegisteredUserResponse.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            //log.info("Se registró con éxito: {}", response.getBody().getStatus());
            return response.getBody();
        }
        log.error("Error al registrarse");
        throw new RuntimeErrorException(null, "Error");
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        String uri = baseUrl + "/login";
        log.info("Logueando {}", loginRequest);
        ResponseEntity<LoginResponse> response = restTemplate.postForEntity(uri, loginRequest, LoginResponse.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            //log.info("Successfully user creation: {}", response.getBody().getStatus());
            return response.getBody();
        }
        log.error("Error in user login - httpStatus was: {}", response.getStatusCode());
        throw new RuntimeException("Error");

    }

}

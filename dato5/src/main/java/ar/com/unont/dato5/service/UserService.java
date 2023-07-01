package ar.com.unont.dato5.service;

import ar.com.unont.dato5.entity.LoginRequest;
import ar.com.unont.dato5.entity.LoginResponse;
import ar.com.unont.dato5.entity.RegisterUserRequest;
import ar.com.unont.dato5.entity.RegisteredUserResponse;

public interface UserService {
    RegisteredUserResponse generarToken(RegisterUserRequest user);

    LoginResponse login(LoginRequest loginrequest);    
}

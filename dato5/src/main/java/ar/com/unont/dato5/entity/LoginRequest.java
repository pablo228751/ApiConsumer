package ar.com.unont.dato5.entity;

import lombok.Data;

@Data
public class LoginRequest {

    private String email;
    private String password;
}
package ar.com.unont.dato5.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LoginResponse {

    private String status;
    @JsonProperty("data")
    private RegisteredDataUser registeredDataUser;
}
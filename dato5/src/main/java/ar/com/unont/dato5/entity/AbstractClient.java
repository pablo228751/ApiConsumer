package ar.com.unont.dato5.entity;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public abstract class AbstractClient {

    @Value("${miapp.apiToken}")
    protected String baseUrl;

    protected final RestTemplate restTemplate;

    protected AbstractClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    protected HttpHeaders buildAuthToken(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/x-www-form-urlencoded");
        headers.set("Cookie", "XSRF-TOKEN=0cb5fc2d-aecb-4072-a37b-ced3cd85cf71" + token);
        return headers;
    }

    protected MultiValueMap<String, String> buildRequestBody() {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "client_credentials");
        body.add("scope", "turnero/control_acceso_read");
        body.add("client_id", "3848nekvugefsce0irdqsj23ud");
        body.add("client_secret", "n3moa7estdcdlqk4abqm4t9ns6fmkl8g2ntmaose4ibdgeur8ut");
        return body;
    }
}


package ar.com.unont.dato5.entity;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

public abstract class AbstractClient {

    @Value("${miapp.api}")
    protected String baseUrl;

    protected final RestTemplate restTemplate;

    protected AbstractClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    protected HttpHeaders buildAuthToken(String token){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type","application/x-www-form-urlencoded");
        headers.set("Cookie", "XSRF-TOKEN=0cb5fc2d-aecb-4072-a37b-ced3cd85cf71"+token);
        return headers;

    }



    
}

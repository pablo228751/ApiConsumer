package ar.com.unont.dato5.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ar.com.unont.dato5.Dato5Setup;
import ar.com.unont.dato5.entity.RegisteredUserResponse;
import ar.com.unont.dato5.entity.Turnero;
import lombok.extern.slf4j.Slf4j;
import com.fasterxml.jackson.databind.ObjectMapper;

@Slf4j
@Service
public class ConsumirApiImpl implements ConsumirApi {

    @Value("${miapp.apiToken}")
    protected String baseUrl;
    @Value("${miapp.test}")
    protected String consumerUrl;
    @Value("${miapp.client_id}")
    private String clientId;
    @Value("${miapp.client_secret}")
    private String clientSecret;

    private RegisteredUserResponse responseBody = new RegisteredUserResponse();

    protected final RestTemplate restTemplate;

    public ConsumirApiImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public RegisteredUserResponse generarToken() {
        try {
            String url = baseUrl;
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("grant_type", "client_credentials");
            body.add("scope", "turnero/control_acceso_read");
            body.add("client_id", clientId);
            body.add("client_secret", clientSecret);
            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);
            ResponseEntity<RegisteredUserResponse> response = restTemplate.postForEntity(url, requestEntity,
                    RegisteredUserResponse.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                responseBody = response.getBody();
                Dato5Setup.token = response.getBody().getAccessToken();
                Dato5Setup.expiro = false;
                return responseBody;
            } else {
                responseBody.setAccessToken("");
                responseBody.setExpiresIn(-1);
                responseBody.setTokenType("");
                Dato5Setup.expiro = true;
                log.error("Error al registrarse");
                return responseBody;
            }
        } catch (Exception e) {
            log.error("Excepción al generar el token", e + " \n\n");
            responseBody.setAccessToken("");
            responseBody.setExpiresIn(-2);
            responseBody.setTokenType("");
            Dato5Setup.expiro = true;
            return responseBody;
        }
    }

    @Override
    public Turnero consultaTurnos(Map<String, String> parametros) {
        ObjectMapper objectMapper = new ObjectMapper();
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(consumerUrl);
        for (Map.Entry<String, String> entry : parametros.entrySet()) {
            builder.queryParam(entry.getKey(), entry.getValue());
        }

        String url = builder.toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(Dato5Setup.token);

        HttpEntity<?> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            String responseBody = response.getBody();
            try {
                Turnero turnero = objectMapper.readValue(responseBody, Turnero.class);
                return turnero;
            } catch (Exception e) {
                System.out.println("Error al convertir el JSON a objeto Turnero");
                e.printStackTrace();
            }
        } else {
            log.error("Error: {}", response.getStatusCode());
            throw new RuntimeException("Error");
        }

        return null;
    }
}

package com.pm.personnelmanagement.user.api;

import com.pm.personnelmanagement.user.dto.Token;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

public class KeycloakApiV1 implements KeycloakApi {
    private final String GRANT_TYPE = "client_credentials";
    private final String CLIENT_ID;
    private final String CLIENT_SECRET;
    private final String HOST_URL;

    public KeycloakApiV1(String CLIENT_ID, String CLIENT_SECRET, String HOST_URL) {
        this.CLIENT_ID = CLIENT_ID;
        this.CLIENT_SECRET = CLIENT_SECRET;
        this.HOST_URL = HOST_URL;
    }

    @Override
    public Token getToken(String username, String password) {
        WebClient webClient = WebClient.create(HOST_URL);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_id", CLIENT_ID);
        map.add("client_secret", CLIENT_SECRET);
        map.add("grant_type", GRANT_TYPE);
        map.add("password", password);
        map.add("username", username);
        map.add("scope", "openid");

        return webClient.post()
                .uri("/realms/app/protocol/openid-connect/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue(map)
                .retrieve()
                .bodyToMono(Token.class)
                .block();
    }
}

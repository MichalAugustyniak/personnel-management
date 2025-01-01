package com.pm.personnelmanagement.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Token {
    private String accessToken;
    private int expiresIn;
    private int refreshExpiresIn;
    private String refreshToken;
    private String tokenType;
    private String idToken;
    private int notBeforePolicy;
    private String sessionState;
    private String scope;

    @JsonProperty("accessToken")
    public String getAccessToken() {
        return accessToken;
    }

    @JsonProperty("access_token")
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @JsonProperty("expiresIn")
    public int getExpiresIn() {
        return expiresIn;
    }

    @JsonProperty("expires_in")
    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }

    @JsonProperty(value = "refreshExpiresIn", required = true)
    public int getRefreshExpiresIn() {
        return refreshExpiresIn;
    }

    @JsonProperty(value = "refresh_expires_in", required = true)
    public void setRefreshExpiresIn(int refreshExpiresIn) {
        this.refreshExpiresIn = refreshExpiresIn;
    }

    @JsonProperty("refreshToken")
    public String getRefreshToken() {
        return refreshToken;
    }

    @JsonProperty("refresh_token")
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    @JsonProperty("tokenType")
    public String getTokenType() {
        return tokenType;
    }

    @JsonProperty("token_type")
    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    @JsonProperty("idToken")
    public String getIdToken() {
        return idToken;
    }

    @JsonProperty("id_token")
    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    @JsonProperty("notBeforePolicy")
    public int getNotBeforePolicy() {
        return notBeforePolicy;
    }

    @JsonProperty("not-before-policy")
    public void setNotBeforePolicy(int notBeforePolicy) {
        this.notBeforePolicy = notBeforePolicy;
    }

    @JsonProperty("sessionState")
    public String getSessionState() {
        return sessionState;
    }

    @JsonProperty("session_state")
    public void setSessionState(String sessionState) {
        this.sessionState = sessionState;
    }

    @JsonProperty("scope")
    public String getScope() {
        return scope;
    }

    @JsonProperty("scope")
    public void setScope(String scope) {
        this.scope = scope;
    }
}

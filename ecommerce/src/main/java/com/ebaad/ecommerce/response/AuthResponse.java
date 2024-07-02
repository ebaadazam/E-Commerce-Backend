package com.ebaad.ecommerce.response;

public class AuthResponse {
    private String jwt;
    private String message;

    public AuthResponse() {
    }

    public String getJwt() {
        return jwt;
    }
    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}

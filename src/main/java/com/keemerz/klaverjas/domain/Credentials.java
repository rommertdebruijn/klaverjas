package com.keemerz.klaverjas.domain;

public class Credentials {

    private final String userId; // Spring security token
    private final String encryptedPassword;

    public Credentials(String userId, String encryptedPassword) {
        this.userId = userId;
        this.encryptedPassword = encryptedPassword;
    }

    public String getUserId() {
        return userId;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

}

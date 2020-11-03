package com.zypper.modules.authenticator.pojo.vo;

/**
 * @author John
 * @version 1.0
 * @since 1.0
 */
public class AuthVO {

    private String username;

    private String secretKey;

    private String pin;

    @Override
    public String toString() {
        return "AuthVO{" +
                "username='" + username + '\'' +
                ", secretKey='" + secretKey + '\'' +
                ", pin='" + pin + '\'' +
                '}';
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }
}

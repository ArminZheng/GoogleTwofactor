package com.arminzheng.system.model;

import lombok.Data;

/**
 * Login form
 */
@Data
public class SysLoginModel {
    private String username;
    private String password;
    private String captcha;
    // two-factor code
    private String pin;
}

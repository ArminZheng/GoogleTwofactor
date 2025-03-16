package com.zypper.modules.authenticator.pojo.vo;

import lombok.Data;

/**
 * @author John
 * @version 1.0
 * @since 1.0
 */
@Data
public class AuthVO {

    private String username;
    private String secretKey;
    private String pin;

}

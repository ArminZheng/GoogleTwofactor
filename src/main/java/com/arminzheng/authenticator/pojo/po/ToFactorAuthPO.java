package com.arminzheng.authenticator.pojo.po;

import java.io.Serializable;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 2FA 
 * 
 * @author John
 * @version 1.0
 * @since 1.0
 */
@Data
@Accessors(chain = true)
public class ToFactorAuthPO implements Serializable {

    private Integer id;

    private String username;

    private String secretkey;
}

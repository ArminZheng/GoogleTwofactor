package com.zypper.modules.authenticator.pojo.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * 2FA
 * 属性: id, username, secretkey
 * @author John
 * @version 1.0
 * @since 1.0
 */
@TableName("to_factor_auth")
public class ToFactorAuthPO implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String username;

    private String secretkey;

    public Integer getId() {
        return id;
    }

    public ToFactorAuthPO setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public ToFactorAuthPO setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getSecretkey() {
        return secretkey;
    }

    public ToFactorAuthPO setSecretkey(String secretkey) {
        this.secretkey = secretkey;
        return this;
    }
}

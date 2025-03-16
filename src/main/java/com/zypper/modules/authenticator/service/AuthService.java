package com.zypper.modules.authenticator.service;

import com.zypper.modules.authenticator.pojo.po.ToFactorAuthPO;

/**
 * @author John
 * @version 1.0
 * @since 1.0
 */
public interface AuthService {

    boolean saveOrUpdate(ToFactorAuthPO toFactorAuthPO);

    ToFactorAuthPO getOne(String username);

    Boolean remove(String eq);
}

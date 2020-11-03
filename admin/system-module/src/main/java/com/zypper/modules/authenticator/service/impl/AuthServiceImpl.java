package com.zypper.modules.authenticator.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zypper.modules.authenticator.mapper.ToFactorAuthMapper;
import com.zypper.modules.authenticator.pojo.po.ToFactorAuthPO;
import com.zypper.modules.authenticator.service.AuthService;
import org.springframework.stereotype.Service;

/**
 * @author John
 * @version 1.0
 * @since 1.0
 */
@Service
public class AuthServiceImpl extends ServiceImpl<ToFactorAuthMapper, ToFactorAuthPO> implements AuthService {
}

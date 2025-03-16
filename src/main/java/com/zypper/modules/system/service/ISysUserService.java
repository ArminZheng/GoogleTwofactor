package com.zypper.modules.system.service;

import com.zypper.modules.authenticator.pojo.po.SysUser;

public interface ISysUserService {
    SysUser getUserByName(String username);
}

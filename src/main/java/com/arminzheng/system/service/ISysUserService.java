package com.arminzheng.system.service;

import com.arminzheng.authenticator.pojo.po.SysUser;

public interface ISysUserService {
    SysUser getUserByName(String username);
}

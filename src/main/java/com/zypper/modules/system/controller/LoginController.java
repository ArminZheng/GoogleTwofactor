package com.zypper.modules.system.controller;

import com.alibaba.fastjson2.JSONObject;
import com.zypper.modules.authenticator.pojo.po.SysUser;
import com.zypper.modules.authenticator.pojo.po.ToFactorAuthPO;
import com.zypper.modules.authenticator.pojo.vo.Result;
import com.zypper.modules.authenticator.service.AuthService;
import com.zypper.modules.authenticator.utils.TOTP;
import com.zypper.modules.system.model.SysLoginModel;
import com.zypper.modules.system.service.ISysUserService;
import com.zypper.modules.system.util.JwtUtil;
import com.zypper.modules.system.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Login Api
 */
@RestController
@RequestMapping("/sys")
@Slf4j
public class LoginController {
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private AuthService authService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Result<JSONObject> login(@RequestBody SysLoginModel sysLoginModel) {

        String username = sysLoginModel.getUsername();
        String password = sysLoginModel.getPassword();
        String pin = sysLoginModel.getPin();
        SysUser sysUser = sysUserService.getUserByName(username);
        if (sysUser == null) {
            return Result.error("该用户不存在");
        }
        //### update-begin---author:John   Date:20201022  for：失败3次锁定30分钟------------
        Object retryCount = redisUtil.get("retryLock:" + username);
        if (retryCount != null && Integer.valueOf(retryCount.toString()) <= 0) {
            return Result.error("密码错误超过3次，请30分钟后尝试");
        }
        // 2. 校验用户名或密码是否正确
        String syspassword = sysUser.getPassword();
        if (!syspassword.equals(password)) {
            Integer retryNumber =
                    (retryCount == null) ? 3 : (Integer.valueOf(retryCount.toString()) - 1);
            redisUtil.set("retryLock:" + username, retryNumber, (30 * 60));
            if (retryNumber == 0) {
                return Result.error("密码错误超过3次，请30分钟后尝试");
            } else {
                return Result.error("用户名或密码错误，还有" + retryNumber + "次机会");
            }
        }
        redisUtil.del("retryLock:" + username);   // 登录成功，重置key
        //### update-end---author:John   Date:20201022  for：失败3次锁定30分钟------------

        // 3. 校验两步验证码
        ToFactorAuthPO authPO = authService.getOne(username);
        if (authPO != null) {
            if (pin.isEmpty()) {
                return Result.error("请输入两部验证码");
            }
            System.out.println(authPO);
            String code = TOTP.getTOTPCode(authPO.getSecretkey());
            if (!pin.equals(code)) { // 未通过
                return Result.error("两部验证码错误");
            }
        }
        // generate token.
        String token = JwtUtil.sign(username, syspassword);
        redisUtil.set("PREFIX_USER_TOKEN" + token, token);
        // set up expire time.
        redisUtil.expire("PREFIX_USER_TOKEN" + token, JwtUtil.EXPIRE_TIME / 1000);
        JSONObject obj = new JSONObject();
        obj.put("token", token);
        obj.put("userInfo", sysUser);
        return Result.ok("Login Success", obj);
    }

}

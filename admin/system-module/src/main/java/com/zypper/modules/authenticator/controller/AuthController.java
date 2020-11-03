package com.zypper.modules.authenticator.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.SneakyThrows;
import com.zypper.modules.authenticator.pojo.po.ToFactorAuthPO;
import com.zypper.modules.authenticator.pojo.vo.AuthVO;
import com.zypper.modules.authenticator.service.AuthService;
import com.zypper.modules.authenticator.service.GoogleAuthenticatorService;
import com.zypper.modules.authenticator.utils.QrCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author John
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    GoogleAuthenticatorService service;

    @Autowired
    AuthService authService;

    /**
     * 生成
     *
     * @param username
     * @return
     */
    @SneakyThrows
    @GetMapping("/generate/")
    public Map<String, String> generate(@RequestParam String username) {
        String secretKey = service.getRandomSecretKey(); // 生成32位密钥
        String barCode = service.getBarCode(secretKey, username, "喜行网约车管理平台");
        String s = QrCodeUtil.crateQrCode(barCode, 200, 200);
        Map<String, String> map = new HashMap<>();
        map.put("src", s);
        map.put("secretKey", secretKey);
        return map;
    }

    /**
     * 验证pin码
     *
     * @param authVO
     * @return
     */
    @PostMapping("/validate")
    public Boolean validateKey(@RequestBody AuthVO authVO) { // username pin secretKey
        System.out.println(authVO);
        // 验证输入的code值
        String code = service.getTOTPCode(authVO.getSecretKey());
        if (authVO.getPin().equals(code)) {
            // 验证通过用户名
            boolean b = authService.saveOrUpdate(new ToFactorAuthPO().setUsername(authVO.getUsername()).setSecretkey(authVO.getSecretKey()));
            System.out.println("验证pin结果为:" + b);
            return b;
        } else {
            return false;
        }
    }

    /**
     * 登陆验证
     *
     * @param authVO
     * @return
     */
    @PostMapping("/toFactorAuth")
    public Boolean loginToFactor(@RequestBody AuthVO authVO) { // username pin
        System.out.println(authVO);
        if (!authVO.getUsername().equals("")) {
            ToFactorAuthPO one = authService.getOne(new QueryWrapper<ToFactorAuthPO>()
                    .lambda()
                    .eq(ToFactorAuthPO::getUsername, authVO.getUsername()).last("limit 1"));
            String secretkey = one.getSecretkey();
            if (one != null) {
                String totpCode = service.getTOTPCode(secretkey);
                if (totpCode.equals(authVO.getPin())) {
                    System.out.println("验证成功");
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 关闭双重认证
     *
     * @param authVO
     * @return
     */
    @PostMapping("/close")
    public Boolean closeToFactor(@RequestBody AuthVO authVO) { // username
        System.out.println(authVO);
        if (!authVO.getUsername().equals("")) {
            return authService.remove(new QueryWrapper<ToFactorAuthPO>()
                    .lambda()
                    .eq(ToFactorAuthPO::getUsername, authVO.getUsername()));
        }
        return false;
    }

    /**
     * 是否开启双重认证
     *
     * @param authVO
     * @return
     */
    @PostMapping("/isTFA")
    public Boolean isTFA(@RequestBody AuthVO authVO) { // username
        System.out.println(authVO);
        if (!authVO.getUsername().equals("")) {
            ToFactorAuthPO one = authService.getOne(new QueryWrapper<ToFactorAuthPO>()
                    .lambda()
                    .eq(ToFactorAuthPO::getUsername, authVO.getUsername()).last("limit 1"));
            if (one != null) {
                System.out.println("one = " + one);
                return true;
            }
        }
        return false;
    }
}

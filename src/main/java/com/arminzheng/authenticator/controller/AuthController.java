package com.arminzheng.authenticator.controller;

import com.arminzheng.authenticator.pojo.po.ToFactorAuthPO;
import com.arminzheng.authenticator.pojo.vo.AuthVO;
import com.arminzheng.authenticator.service.AuthService;
import com.arminzheng.authenticator.service.GoogleAuthenticatorService;
import com.arminzheng.authenticator.utils.QrCodeUtil;
import java.util.HashMap;
import java.util.Map;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Two-factor Controller
 *
 * @author John
 * @version 1.0
 * @since 1.0
 */
@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    GoogleAuthenticatorService googleAuthenticatorService;
    @Autowired
    AuthService authService;

    /**
     * generate secret for two-factor
     *
     * @param username
     * @return
     */
    @SneakyThrows
    @GetMapping("/generate/")
    public Map<String, String> generate(@RequestParam String username) {
        String secretKey = googleAuthenticatorService.getRandomSecretKey(); // generate 32 secret
        String barCode = googleAuthenticatorService.getBarCode(secretKey, username,
                "Platform CompanyA");
        String s = QrCodeUtil.crateQrCode(barCode, 200, 200);
        Map<String, String> map = new HashMap<>();
        map.put("src", s);
        map.put("secretKey", secretKey);
        return map;
    }

    /**
     * validate two-factor
     *
     * @param authVO
     * @return
     */
    @PostMapping("/validate")
    public Boolean validateKey(@RequestBody AuthVO authVO) { // username pin secretKey
        System.out.println(authVO);
        // generate totp code
        String code = googleAuthenticatorService.getTOTPCode(authVO.getSecretKey());
        if (authVO.getPin().equals(code)) {
            // pass
            boolean b = authService.saveOrUpdate(
                    new ToFactorAuthPO().setUsername(authVO.getUsername())
                            .setSecretkey(authVO.getSecretKey()));
            log.info("save result: {}", b);
            return b;
        } else {
            return false;
        }
    }

    /**
     * authorize
     *
     * @param authVO
     * @return
     */
    @PostMapping("/toFactorAuth")
    public Boolean loginToFactor(@RequestBody AuthVO authVO) { // username pin
        System.out.println(authVO);
        if (!authVO.getUsername().equals("")) {
            ToFactorAuthPO one = authService.getOne(authVO.getUsername());
            String secretkey = one.getSecretkey();
            String totpCode = googleAuthenticatorService.getTOTPCode(secretkey);
            if (totpCode.equals(authVO.getPin())) {
                log.info("success");
                return true;
            }
        }
        return false;
    }

    /**
     * close two-factor
     *
     * @param authVO
     * @return
     */
    @PostMapping("/close")
    public Boolean closeTwoFactor(@RequestBody AuthVO authVO) { // username
        log.info("body: {}", authVO);
        if (!"".equals(authVO.getUsername())) {
            return authService.remove(authVO.getUsername());
        }
        return false;
    }

    /**
     * is two-factor enabled
     *
     * @param authVO
     * @return
     */
    @PostMapping("/isTFA")
    public Boolean isTFA(@RequestBody AuthVO authVO) { // username
        System.out.println(authVO);
        if (!"".equals(authVO.getUsername())) {
            ToFactorAuthPO one = authService.getOne(authVO.getUsername());
            if (one != null) {
                System.out.println("one = " + one);
                return true;
            }
        }
        return false;
    }
}

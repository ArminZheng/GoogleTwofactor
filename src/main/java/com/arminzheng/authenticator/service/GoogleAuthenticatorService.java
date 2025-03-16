package com.arminzheng.authenticator.service;

import com.google.zxing.WriterException;

import java.io.IOException;

/**
 * @author John
 * @version 1.0
 * @since 1.0
 */
public interface GoogleAuthenticatorService {
    /**
     * 生成随机生成密钥
     * @return
     */
    String getRandomSecretKey();

    /**
     * 设置用户名, 公司名
     * 生成二维码内容
     * @param secretKey Base32 encoded secret key (may have optional whitespace)
     * @param account The user's account name. e.g. an email address or a username
     * @param issuer The organization managing this account
     * @see <a href="https://github.com/google/google-authenticator/wiki/Key-Uri-Format"/>
     */
    String getBarCode(String secretKey, String account, String issuer);

    /**
     * 创建二维码本地文件
     * @param barCodeData
     * @param filePath
     * @param height
     * @param width
     * @throws WriterException
     * @throws IOException
     */
    @Deprecated
    void createQRCode(String barCodeData, String filePath, int height, int width) throws WriterException, IOException;

    /**
     * 生成6位数字校验码
     * 引用TOTP类校验code值, 方法会获取当前时间
     * @param secretKey 32位key值
     * @return 返回6位数字校验码
     */
    String getTOTPCode(String secretKey);
}

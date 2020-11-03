package com.zypper.modules.authenticator.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.EnumMap;
import java.util.Map;

/**
 * 二维码工具类
 *
 * @author hexinglin
 * @created 2019-07-02 10:21
 **/
public class QrCodeUtil {

    private QrCodeUtil() {
        throw new IllegalStateException("QrCodeUtil class");
    }

    private static final Logger logger = LoggerFactory.getLogger(QrCodeUtil.class);

    public static String crateQrCode(String content, int width, int height) throws IOException {

        String resultImage = "";
        if (!StringUtils.isEmpty(content)) {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            @SuppressWarnings("rawtypes")
            Map<EncodeHintType, Comparable> hints = new EnumMap<>(EncodeHintType.class);
            // 指定字符编码为“utf-8”
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            // 指定二维码的纠错等级为中级
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
            // 设置图片的边距
            hints.put(EncodeHintType.MARGIN, 2);

            try {
                QRCodeWriter writer = new QRCodeWriter();
                BitMatrix bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, width, height, hints);

                BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
                ImageIO.write(bufferedImage, "png", os);
                /**
                 * 原生转码前面没有 data:image/png;base64 这些字段，返回给前端是无法被解析，可以让前端加，也可以在下面加上
                 */
                resultImage = "data:image/png;base64," + Base64.getEncoder().encodeToString(os.toByteArray());

                return resultImage;
            } catch (Exception e) {
                logger.warn("二维码生成Exception", e);
            }
        }
        return null;
    }
}

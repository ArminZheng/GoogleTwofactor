package com.arminzheng.authenticator.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.EnumMap;
import java.util.Map;
import javax.imageio.ImageIO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/**
 * Qr code utils
 */
public final class QrCodeUtil {

    private static final Logger logger = LoggerFactory.getLogger(QrCodeUtil.class);

    private QrCodeUtil() {
        throw new IllegalStateException("QrCodeUtil class");
    }

    public static String crateQrCode(String content, int width, int height) {
        String resultImage;
        if (!StringUtils.isEmpty(content)) {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            @SuppressWarnings("rawtypes")
            Map<EncodeHintType, Comparable> hints = new EnumMap<>(EncodeHintType.class);
            // utf-8
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            // set error correction level: middle.
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
            // set margin to 2
            hints.put(EncodeHintType.MARGIN, 2);

            try {
                QRCodeWriter writer = new QRCodeWriter();
                BitMatrix bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, width, height, hints);

                BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
                ImageIO.write(bufferedImage, "png", os);
                // need add prefix data:image/png;base64
                resultImage = "data:image/png;base64," + Base64.getEncoder().encodeToString(os.toByteArray());

                return resultImage;
            } catch (Exception e) {
                logger.warn("QR generate Exception: {}", e.getMessage());
            }
        }
        return null;
    }
}

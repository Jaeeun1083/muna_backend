package com.life.muna.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Component
public class ImageUtil {
    private static final Logger LOG = LoggerFactory.getLogger(ImageUtil.class);
    private static final int MAX_WIDTH = 800;
    private static final int MAX_HEIGHT = 600;
    private static final int MAX_BLOB_SIZE = 65535; // MariaDB blob 최대 크기 (65,535 bytes)

    public static byte[] resizeThumbnail(MultipartFile file) {
        try {
            BufferedImage image = ImageIO.read(file.getInputStream());
            return resizeThumbnail(image);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] resizeThumbnail(byte[] fileBytes) {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(fileBytes)) {
            BufferedImage image = ImageIO.read(inputStream);
            return resizeThumbnail(image);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static byte[] resizeThumbnail(BufferedImage image) {
        int originalWidth = image.getWidth();
        int originalHeight = image.getHeight();

        if (originalWidth <= MAX_WIDTH && originalHeight <= MAX_HEIGHT) {
            return getBytes(image);
        }

        int scaledWidth;
        int scaledHeight;

        double widthRatio = (double) MAX_WIDTH / originalWidth;
        double heightRatio = (double) MAX_HEIGHT / originalHeight;
        double scaleFactor = Math.min(widthRatio, heightRatio);

        scaledWidth = (int) (originalWidth * scaleFactor);
        scaledHeight = (int) (originalHeight * scaleFactor);

        while (scaledWidth * scaledHeight > MAX_BLOB_SIZE) {
            scaledWidth = (int) (scaledWidth * 0.9); // 가로 크기를 90%로 조정
            scaledHeight = (int) (scaledHeight * 0.9); // 세로 크기를 90%로 조정
        }

        Image resizedImage = image.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
        BufferedImage bufferedImage = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_RGB);
        bufferedImage.getGraphics().drawImage(resizedImage, 0, 0, null);

        return getBytes(bufferedImage);
    }

    private static byte[] getBytes(BufferedImage image) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ImageIO.write(image, "jpg", outputStream);
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

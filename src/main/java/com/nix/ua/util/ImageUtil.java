package com.nix.ua.util;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

public final class ImageUtil {
    private static final String UPLOAD_DIRECTORY = System.getProperty("user.dir") + File.separator + "src" +
            File.separator + "main" + File.separator + "resources" + File.separator + "static" + File.separator;

    private ImageUtil() {
    }

    public static void saveImage(MultipartFile file) {
        if (!file.isEmpty()) {
            final String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
            try {
                final Path path = Paths.get(UPLOAD_DIRECTORY + fileName);
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
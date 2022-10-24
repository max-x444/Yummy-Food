package com.nix.ua.util;

import lombok.SneakyThrows;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ImageUtil {
    private ImageUtil() {
    }

    public static Optional<String> saveImage(MultipartFile file) {
        Optional<String> linkImage = Optional.empty();
        if (!file.isEmpty()) {
            final HttpURLConnection connection = openConnection();
            setParameters(connection, file);
            linkImage = readAnswer(connection);
            connection.disconnect();
        }
        return linkImage;
    }

    @SneakyThrows
    private static HttpURLConnection openConnection() {
        final URL url = new URL("https://api.imgbb.com/1/upload");
        final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Disposition", "multipart/form-data");
        connection.setDoInput(true);
        connection.setDoOutput(true);
        return connection;
    }

    @SneakyThrows
    private static void setParameters(HttpURLConnection connection, MultipartFile file) {
        final String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        final byte[] bytes = Base64.getEncoder().encode(file.getBytes());
        final String image = new String(bytes);

        final Map<String, String> parameters = new HashMap<>();
        parameters.put("key", System.getenv("KEY"));
        parameters.put("image", image);
        parameters.put("name", fileName);

        final DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
        dataOutputStream.writeBytes(getParametersString(parameters));
        dataOutputStream.flush();
        dataOutputStream.close();
    }

    private static Optional<String> readAnswer(HttpURLConnection connection) {
        try (final InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream());
             final BufferedReader in = new BufferedReader(inputStreamReader)) {
            final StringBuilder content = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            return formattedAnswer(content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Optional<String> formattedAnswer(StringBuilder content) {
        final Pattern pattern = Pattern.compile("[\"][u][r][l][\"][:][\"](.+?)[\"]");
        final Matcher matcher = pattern.matcher(content.toString());
        if (matcher.find()) {
            return Optional.of(matcher.group(1).replace("\\/", "/"));
        }
        return Optional.empty();
    }

    private static String getParametersString(Map<String, String> params) {
        final StringBuilder result = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            result.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8));
            result.append("&");
        }
        final String resultString = result.toString();
        return resultString.length() > 0 ? resultString.substring(0, resultString.length() - 1) : resultString;
    }
}
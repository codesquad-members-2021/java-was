package http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class HttpResponse {
    private static final Logger log = LoggerFactory.getLogger(HttpResponse.class);

    private static final Map<String, String> EXTENSIONS = new HashMap<>();
    private final Map<String, String> headers = new HashMap<>();
    private final DataOutputStream dos;

    static {
        EXTENSIONS.put(".css", "text/css");
        EXTENSIONS.put(".js", "application/javascript");
        EXTENSIONS.put(".html", "text/html");
    }

    public HttpResponse(OutputStream out) {
        this.dos = new DataOutputStream(out);
    }

    public void addHeader(String fieldName, String fieldValue) {
        headers.put(fieldName, fieldValue);
    }

    public void forward(String url) throws IOException {
        byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());
        String extension = url.substring(url.lastIndexOf("."));
        response200Header(body.length, EXTENSIONS.get(extension));
        responseBody(body);
    }

    public void forwardBody(String result) {
        byte[] body = result.getBytes(StandardCharsets.UTF_8);
        response200Header(body.length, "text/html");
        responseBody(body);
    }

    public void response200Header(int lengthOfBodyContent, String contentType) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type:" + contentType + ";charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public void responseBody(byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public void sendRedirect(String url) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            processHeaders();
            dos.writeBytes("Location: " + url + "\r\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void processHeaders() {
        try {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                dos.writeBytes(entry.getKey() + ": " + entry.getValue() + " \r\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

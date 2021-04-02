package http;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class HttpResponse {

    private DataOutputStream dos;

    private String startLine;
    private Map<String, String> headers = new HashMap<>();

    public HttpResponse(OutputStream os) {
        dos = new DataOutputStream(os);
    }

    public void forward(String url) throws IOException {
        startLine = "HTTP/1.1 200 OK \r\n";
        byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());

        String contentTypeValue = "text/html;charset=utf-8";
        if (url.endsWith(".css")) {
            contentTypeValue = "text/css;charset=utf-8";
        }

        addHeader("Content-Type", contentTypeValue);
        addHeader("Content-Length", String.valueOf(body.length));

        processHeaders();
        responseBody(body);
    }

    private void responseBody(byte[] body) throws IOException {
        dos.write(body, 0, body.length);
        dos.flush();
    }

    public void redirect(String url) throws IOException {
        startLine = "HTTP/1.1 302 FOUND \r\n";
        addHeader("Location", url);
        processHeaders();
    }

    public void addHeader(String header, String value) {
        headers.put(header, value);
    }

    private void processHeaders() throws IOException {
        dos.writeBytes(startLine);
        for (Map.Entry<String, String> header : headers.entrySet()) {
            dos.writeBytes(header.getKey() + ": " + header.getValue() + "\r\n");
        }
        dos.writeBytes("\r\n");
    }

}

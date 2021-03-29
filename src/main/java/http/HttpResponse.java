package http;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

public class HttpResponse {
    private DataOutputStream dos;

    public HttpResponse(OutputStream os) {
        dos = new DataOutputStream(os);
    }

    public void forward(String url) throws IOException {
        byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());

        if (url.endsWith(".css")) {
            response200Header(dos, body.length, "css");
        } else {
            response200Header(dos, body.length, "html");
        }

        responseBody(dos, body);
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, String contentType) throws IOException {
        dos.writeBytes("HTTP/1.1 200 OK \r\n");
        dos.writeBytes("Content-Type: text/" + contentType + ";charset=utf-8\r\n");
        dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
        dos.writeBytes("\r\n");

    }

    private void responseBody(DataOutputStream dos, byte[] body) throws IOException {
        dos.write(body, 0, body.length);
        dos.flush();

    }

    public void redirect(String url) throws IOException {
        dos.writeBytes("HTTP/1.1 302 FOUND \r\n");
        dos.writeBytes("Location: " + url + "\r\n");
        dos.writeBytes("\r\n");
    }
}

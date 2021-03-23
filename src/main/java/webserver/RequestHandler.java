package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.List;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;
    private List<User> users;

    public RequestHandler(Socket connectionSocket, List<User> users) {
        this.connection = connectionSocket;
        this.users = users;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            HttpRequest httpRequest = HttpRequest.of(in);
            String url = httpRequest.getUrl();


            if ("/user/create".equals(url)) {
                User user = new User(
                        httpRequest.data("userId"),
                        httpRequest.data("password"),
                        httpRequest.data("name"),
                        httpRequest.data("email")
                );
                users.add(user);
                log.debug("user : {}", user);
                //TODO REDIRECT
                byte[] body = Files.readAllBytes(new File("./webapp/index.html").toPath());
                DataOutputStream dos = new DataOutputStream(out);
                response200Header(dos, body.length);
                responseBody(dos, body);
            } else {
                byte[] body = Files.readAllBytes(new File("./webapp" + httpRequest.getUrl()).toPath());
                DataOutputStream dos = new DataOutputStream(out);
                response200Header(dos, body.length);
                responseBody(dos, body);
            }

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}

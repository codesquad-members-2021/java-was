package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.List;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            HttpRequest httpRequest = HttpRequest.of(in);
            String url = httpRequest.getUrl();


            DataOutputStream dos = new DataOutputStream(out);
            if ("/user/create".equals(url)) {
                User user = new User(
                        httpRequest.data("userId"),
                        httpRequest.data("password"),
                        httpRequest.data("name"),
                        httpRequest.data("email")
                );
                DataBase.addUser(user);
                log.debug("user : {}", user);
                response302Header(dos, "/index.html");
            } else if ("/user/login".equals(url)) {
                User user = DataBase.findUserById(httpRequest.data("userId"));
                if (user == null) {
                    log.debug("Not Found");
                    response302HeaderWithCookie(dos, "/user/login_failed.html", "logined=false", "/");
                } else if (user.checkPassword(httpRequest.data("password"))) {
                    log.debug("Login success");
                    response302HeaderWithCookie(dos, "/index.html", "logined=true", "/");
                } else {
                    log.debug("Password was not matched");
                    response302HeaderWithCookie(dos, "/user/login_failed.html", "logined=false", "/");
                }
            } else if ("/user/list".equals(url)) {
                log.debug("Cookie : {}", httpRequest.header("Cookie"));
                if ("true".equals(httpRequest.cookie("logined"))) {
                    byte[] body = Files.readAllBytes(new File("./webapp" + "/user/list.html").toPath());
                    response200Header(dos, body.length, "html");
                    responseBody(dos, body);
                } else {
                    response302Header(dos, "/user/login.html");
                }
            } else {
                log.debug("Cookie : {}", httpRequest.header("Cookie"));
                byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());

                if (url.endsWith(".css")){
                    response200Header(dos, body.length, "css");
                }else {
                    response200Header(dos, body.length, "html");
                }

                responseBody(dos, body);
            }

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response302HeaderWithCookie(DataOutputStream dos, String url, String cookie, String cookiePath) {
        try {
            dos.writeBytes("HTTP/1.1 302 FOUND \r\n");
            dos.writeBytes("Location: " + url + "\r\n");
            dos.writeBytes("Set-Cookie: " + cookie + "; Path=" + cookiePath + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response302Header(DataOutputStream dos, String url) {
        try {
            dos.writeBytes("HTTP/1.1 302 FOUND \r\n");
            dos.writeBytes("Location: " + url + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, String contentType) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/" + contentType + ";charset=utf-8\r\n");
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

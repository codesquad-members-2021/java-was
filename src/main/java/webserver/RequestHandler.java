package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;

import db.DataBase;
import http.HttpRequest;
import http.HttpResponse;
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

            HttpResponse httpResponse = new HttpResponse(out);
            if ("/user/create".equals(url)) {
                User user = new User(
                        httpRequest.data("userId"),
                        httpRequest.data("password"),
                        httpRequest.data("name"),
                        httpRequest.data("email")
                );
                DataBase.addUser(user);
                log.debug("user : {}", user);
                httpResponse.redirect("/index.html");
            } else if ("/user/login".equals(url)) {
                User user = DataBase.findUserById(httpRequest.data("userId"));
                if (user == null) {
                    log.debug("Not Found");
                    httpResponse.addHeader("Set-Cookie", "logined=false; Path=/");
                    httpResponse.redirect("/user/login_failed.html");
                } else if (user.checkPassword(httpRequest.data("password"))) {
                    log.debug("Login success");
                    httpResponse.addHeader("Set-Cookie", "logined=true; Path=/");
                    httpResponse.redirect("/index.html");
                } else {
                    log.debug("Password was not matched");
                    httpResponse.addHeader("Set-Cookie", "logined=false; Path=/");
                    httpResponse.redirect("/user/login_failed.html");
                }
            } else if ("/user/list".equals(url)) {
                log.debug("Cookie : {}", httpRequest.header("Cookie"));
                if ("true".equals(httpRequest.cookie("logined"))) {
                    httpResponse.forward("/user/list.html");
                } else {
                    httpResponse.redirect("/user/login.html");
                }
            } else {
                log.debug("Cookie : {}", httpRequest.header("Cookie"));
                httpResponse.forward(url);
            }

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

}

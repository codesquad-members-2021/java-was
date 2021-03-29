package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.*;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;

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
            HttpRequest httpRequest = new HttpRequest(in);
            HttpResponse httpResponse = new HttpResponse(out);

            HttpMethod method = httpRequest.getMethod();
            log.info("Http Method: {}", method.name());
            String url = httpRequest.getPath();
            log.info("path : {}", url);
            DataOutputStream dos = new DataOutputStream(out);

            if (url.startsWith("/create")) {
                String userId = httpRequest.getParameter("userId");
                String password = httpRequest.getParameter("password");
                String name = httpRequest.getParameter("name");
                String email = httpRequest.getParameter("email");
                DataBase.addUser(new User(userId, password, name, email));
                httpResponse.sendRedirect("/index.html");
            } else if (url.startsWith("/login")) {
                String userId = httpRequest.getParameter("userId");
                String password = httpRequest.getParameter("password");
                User targetUser = DataBase.findUserById(userId);
                if (targetUser == null || !password.equals(targetUser.getPassword())) {
                    httpResponse.addHeader("Set-Cookie", "logined=false");
                    httpResponse.sendRedirect("/user/login_failed.html");
                } else if (password.equals(targetUser.getPassword())) {
                    log.info("로그인 성공");
                    httpResponse.addHeader("Set-Cookie", "logined=true");
                    httpResponse.sendRedirect("/index.html");
                }
            } else if (url.startsWith("/list")) {
                String cookies = httpRequest.getHeader("Cookie");
                Map<String, String> cookieStringMap = HttpRequestUtils.parseCookies(cookies);
                log.info("Cookie: {}", cookieStringMap.toString());
                if (cookieStringMap.get("logined").equals("false")) {
                    httpResponse.sendRedirect("/user/login.html");
                } else {
                    url = "/user/list.html";
                    byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());
                    String bodyStr = new String(body);
                    int tbodyIndex = bodyStr.indexOf("<tbody>");
                    log.info("bodyStr : {}", bodyStr.substring(0, tbodyIndex + 7));

                    StringBuilder result = new StringBuilder(bodyStr.substring(0, tbodyIndex + 7));
                    Collection<User> users = DataBase.findAll();
                    int id = 0;
                    for (User user : users) {
                        id++;
                        result.append("<tr><th scope=\"row\">")
                                .append(id).append("</th> <td>")
                                .append(user.getUserId()).append("</td> <td>")
                                .append(user.getName()).append("</td> <td>")
                                .append(user.getEmail()).append("</td> <td>")
                                .append("<a href=\"#\" class=\"btn btn - success\" role=\"button\">수정</a></td>");
                    }
                    result.append(bodyStr.substring(tbodyIndex + 7));
                    log.info("result: {}", result.toString());
                    httpResponse.response200Header(result.toString().getBytes().length);
                    httpResponse.responseBody(result.toString().getBytes());
                }
            } else {
                httpResponse.forward(url);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }

    }
}

package controller;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import webserver.HttpRequest;
import webserver.HttpResponse;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collection;
import java.util.Map;

public class ListUserController extends AbstractController {
    private static final Logger log = LoggerFactory.getLogger(ListUserController.class);

    @Override
    public void doGet(HttpRequest request, HttpResponse response) {
        String cookies = request.getHeader("Cookie");
        Map<String, String> cookieStringMap = HttpRequestUtils.parseCookies(cookies);
        log.info("Cookie: {}", cookieStringMap.toString());
        if (cookieStringMap.get("logined").equals("false")) {
            response.sendRedirect("/user/login.html");
        } else {
            String url = "/user/list.html";
            byte[] body = new byte[0];
            try {
                body = Files.readAllBytes(new File("./webapp" + url).toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
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
            response.response200Header(result.toString().getBytes().length, "text/html");
            response.responseBody(result.toString().getBytes());
        }
    }

    public boolean isLogin(String id) {

        return false;
    }
}

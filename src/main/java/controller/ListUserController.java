package controller;

import db.DataBase;
import db.SessionDataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import http.HttpRequest;
import http.HttpResponse;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collection;
import java.util.Map;

public class ListUserController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(ListUserController.class);

    @Override
    public void doGet(HttpRequest request, HttpResponse response) {
        String sessionId = request.getHeader("Cookie");
        if (!isLogin(sessionId)) {
            response.sendRedirect("/user/login.html");
        } else {
            String body = getBody("/user/list.html");
            int tbodyIndex = body.indexOf("<tbody>");
            String result = addUserList(body, tbodyIndex).toString();
            response.forwardBody(result);
        }
    }

    private boolean isLogin(String id) {
        Map<String, String> cookieStringMap = HttpRequestUtils.parseCookies(id);
        log.info("Cookie: {}", cookieStringMap.toString());
        return SessionDataBase.isLoginUser(cookieStringMap.get("JSESSIONID"));
    }

    private String getBody(String url) {
        byte[] body = new byte[0];
        try {
            body = Files.readAllBytes(new File("./webapp" + url).toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(body);
    }

    private StringBuilder processUserList(String body, int tbodyIndex) {
        StringBuilder result = new StringBuilder(body.substring(0, tbodyIndex + 7));
        Collection<User> users = DataBase.findAll();
        int id = 0;
        for (User user : users) {
            id++;
            result.append("<tr><th scope=\"row\">")
                    .append(id)
                    .append("</th> <td>")
                    .append(user.getUserId())
                    .append("</td> <td>")
                    .append(user.getName())
                    .append("</td> <td>")
                    .append(user.getEmail())
                    .append("</td> <td>")
                    .append("<a href=\"#\" class=\"btn btn - success\" role=\"button\">수정</a></td>");
        }
        return result;
    }

    private StringBuilder addUserList(String body, int tbodyIndex) {
        StringBuilder result = processUserList(body, tbodyIndex);
        return result.append(body.substring(tbodyIndex + 7));
    }
}

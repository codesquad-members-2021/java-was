package controller;

import db.DataBase;
import db.SessionDataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import http.HttpRequest;
import http.HttpResponse;

import java.util.UUID;

public class LoginController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @Override
    public void doPost(HttpRequest request, HttpResponse response) {
        String userId = request.getParameter("userId");
        String password = request.getParameter("password");
        User targetUser = DataBase.findUserById(userId);
        if (targetUser == null || !targetUser.isValidPassword(password)) {
            response.addHeader("Set-Cookie", "loggedIn=false");
            response.sendRedirect("/user/login_failed.html");
            return;
        }
        UUID uuid = UUID.randomUUID();
        SessionDataBase.sessions.put(uuid.toString(), targetUser);
        response.addHeader("Set-Cookie", SessionDataBase.JSESSIONID + "=" + uuid);
        response.sendRedirect("/index.html");
        log.info("{}님이 로그인하셨습니다.", userId);
    }
}

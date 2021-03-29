package controller;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.HttpRequest;
import webserver.HttpResponse;

public class LoginController extends AbstractController {
    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @Override
    public void doPost(HttpRequest request, HttpResponse response) {
        String userId = request.getParameter("userId");
        String password = request.getParameter("password");
        User targetUser = DataBase.findUserById(userId);
        // todo : 세션의 작동원리를 깊히 생각해보기
        if (targetUser == null || !password.equals(targetUser.getPassword())) {
            response.addHeader("Set-Cookie", "logined=false");
            response.sendRedirect("/user/login_failed.html");
        } else if (password.equals(targetUser.getPassword())) {
            response.addHeader("Set-Cookie", "logined=true");
            response.sendRedirect("/index.html");
            log.info("{}님이 로그인하셨습니다.", userId);
        }
    }
}

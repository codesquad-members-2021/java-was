package controller;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import http.HttpRequest;
import http.HttpResponse;

public class CreateUserController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(CreateUserController.class);

    @Override
    public void doPost(HttpRequest request, HttpResponse response) {
        User user = getUserFrom(request);
        DataBase.addUser(user);
        response.sendRedirect("/index.html");
        log.debug("{}님이 회원가입에 성공하셨습니다.", user.getUserId());
    }

    private User getUserFrom(HttpRequest request) {
        String userId = request.getParameter("userId");
        String password = request.getParameter("password");
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        return new User(userId, password, name, email);
    }
}

package controller;

import db.DataBase;
import http.HttpRequest;
import http.HttpResponse;
import model.User;

import java.io.IOException;

public class LoginController implements Controller {
    @Override
    public void service(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        User user = DataBase.findUserById(httpRequest.data("userId"));

        if (user != null && user.checkPassword(httpRequest.data("password"))) {
            httpResponse.addHeader("Set-Cookie", "logined=true; Path=/");
            httpResponse.redirect("/index.html");
            return;
        }

        httpResponse.addHeader("Set-Cookie", "logined=false; Path=/");
        httpResponse.redirect("/user/login_failed.html");
    }
}

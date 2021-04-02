package controller;

import annotation.RequestMapping;
import db.DataBase;
import http.HttpMethod;
import http.HttpRequest;
import http.HttpResponse;
import model.User;

import java.io.IOException;

@RequestMapping(path = "/user/login", method = HttpMethod.POST)
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

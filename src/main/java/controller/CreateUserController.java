package controller;

import annotation.RequestMapping;
import db.DataBase;
import http.HttpMethod;
import http.HttpRequest;
import http.HttpResponse;
import model.User;

import java.io.IOException;

@RequestMapping(path = "/user/create", method = HttpMethod.POST)
public class CreateUserController implements Controller {
    @Override
    public void service(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        User user = new User(
                httpRequest.data("userId"),
                httpRequest.data("password"),
                httpRequest.data("name"),
                httpRequest.data("email")
        );
        DataBase.addUser(user);
        httpResponse.redirect("/index.html");
    }
}

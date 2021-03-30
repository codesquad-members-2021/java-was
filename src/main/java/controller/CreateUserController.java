package controller;

import db.DataBase;
import http.HttpRequest;
import http.HttpResponse;
import model.User;

import java.io.IOException;

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

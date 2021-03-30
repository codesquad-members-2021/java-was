package controller;

import http.HttpRequest;
import http.HttpResponse;

import java.io.IOException;

public class ListUserController implements Controller{
    @Override
    public void service(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        if ("true".equals(httpRequest.cookie("logined"))) {
            httpResponse.forward("/user/list.html");
        } else {
            httpResponse.redirect("/user/login.html");
        }
    }
}

package controller;

import annotation.RequestMapping;
import http.HttpMethod;
import http.HttpRequest;
import http.HttpResponse;

import java.io.IOException;

@RequestMapping(path = "/user/list", method = HttpMethod.GET)
public class ListUserController implements Controller {
    @Override
    public void service(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        if ("true".equals(httpRequest.cookie("logined"))) {
            httpResponse.forward("/user/list.html");
            return;
        }
        httpResponse.redirect("/user/login.html");
    }
}

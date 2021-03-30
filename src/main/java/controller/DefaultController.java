package controller;

import http.HttpRequest;
import http.HttpResponse;

import java.io.IOException;

public class DefaultController implements Controller {

    @Override
    public void service(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        httpResponse.forward(httpRequest.getUrl());
    }
}

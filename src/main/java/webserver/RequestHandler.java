package webserver;

import java.io.*;
import java.net.Socket;
import java.util.Map;

import controller.Controller;
import controller.ControllerKey;
import http.HttpRequest;
import http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;
    private HandlerAdapter adapter;


    public RequestHandler(Socket connectionSocket,HandlerAdapter adapter) {
        this.connection = connectionSocket;
        this.adapter = adapter;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            HttpRequest httpRequest = HttpRequest.of(in);
            HttpResponse httpResponse = new HttpResponse(out);

            Controller controller = adapter.controller(new ControllerKey(httpRequest.method(), httpRequest.getUrl()));

            controller.service(httpRequest, httpResponse);

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

}

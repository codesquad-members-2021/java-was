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

    private Map<ControllerKey, Controller> controllerMap;
    private Controller defaultController;
    private Socket connection;


    public RequestHandler(Socket connectionSocket, Map<ControllerKey, Controller> controllerMap, Controller defaultController) {
        this.connection = connectionSocket;
        this.controllerMap = controllerMap;
        this.defaultController = defaultController;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            HttpRequest httpRequest = HttpRequest.of(in);
            HttpResponse httpResponse = new HttpResponse(out);

            ControllerKey controllerKey = new ControllerKey(httpRequest.method(), httpRequest.getUrl());
            Controller controller = controllerMap.getOrDefault(controllerKey, defaultController);

            controller.service(httpRequest, httpResponse);

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

}

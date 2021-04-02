package webserver;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import controller.Controller;
import controller.CreateUserController;
import controller.ListUserController;
import controller.LoginController;
import http.HttpRequest;
import http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private static final Map<String, Controller> CONTROLLER_MAP = new HashMap<>();
    private final Socket connection;

    static {
        CONTROLLER_MAP.put("/create", new CreateUserController());
        CONTROLLER_MAP.put("/login", new LoginController());
        CONTROLLER_MAP.put("/list", new ListUserController());
    }

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            HttpRequest httpRequest = new HttpRequest(in);
            HttpResponse httpResponse = new HttpResponse(out);

            String url = httpRequest.getPath();
            if (!CONTROLLER_MAP.containsKey(url)) {
                httpResponse.forward(url);
                return;
            }

            Controller controller = CONTROLLER_MAP.get(url);
            controller.service(httpRequest, httpResponse);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}

package webserver;

import java.io.*;
import java.net.Socket;

import controller.Controller;
import controller.CreateUserController;
import controller.ListUserController;
import controller.LoginController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            HttpRequest httpRequest = new HttpRequest(in);
            HttpResponse httpResponse = new HttpResponse(out);

            HttpMethod method = httpRequest.getMethod();
            log.info("Http Method: {}", method.name());
            String url = httpRequest.getPath();
            log.info("path : {}", url);

            if (url.startsWith("/create")) {
                Controller createUserController = new CreateUserController();
                createUserController.service(httpRequest, httpResponse);
            } else if (url.startsWith("/login")) {
                Controller loginController = new LoginController();
                loginController.service(httpRequest, httpResponse);
            } else if (url.startsWith("/list")) {
                Controller listUserController = new ListUserController();
                listUserController.service(httpRequest, httpResponse);
            } else {
                httpResponse.forward(url);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }

    }
}

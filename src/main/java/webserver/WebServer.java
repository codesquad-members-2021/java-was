package webserver;

import controller.*;
import http.HttpMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class WebServer {
    private static final Logger log = LoggerFactory.getLogger(WebServer.class);
    private static final int DEFAULT_PORT = 8080;

    private static final Controller defaultController = new DefaultController();
    private static final Map<ControllerKey, Controller> controllerMap = new HashMap<>();

    static {
        controllerMap.put(new ControllerKey(HttpMethod.POST, "/user/create"), new CreateUserController());
        controllerMap.put(new ControllerKey(HttpMethod.POST, "/user/login"), new LoginController());
        controllerMap.put(new ControllerKey(HttpMethod.GET, "/user/list"), new ListUserController());
    }

    public static void main(String args[]) throws Exception {
        int port = DEFAULT_PORT;
        if (args != null && args.length > 0) {
            port = Integer.parseInt(args[0]);
        }

        // 서버소켓을 생성한다. 웹서버는 기본적으로 8080번 포트를 사용한다.
        try (ServerSocket listenSocket = new ServerSocket(port)) {
            log.info("Web Application Server started {} port.", port);

            // 클라이언트가 연결될때까지 대기한다.
            Socket connection;
            while ((connection = listenSocket.accept()) != null) {
                RequestHandler requestHandler = new RequestHandler(connection, controllerMap, defaultController);
                requestHandler.start();
            }
        }
    }
}

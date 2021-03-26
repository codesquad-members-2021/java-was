package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.*;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;

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
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));

            String requestLine = br.readLine();
            String line = requestLine;
            Map<String, String> headers = new HashMap<>();
            while (!"".equals(line)) {
                System.out.println(line);
                line = br.readLine();
                String[] headerTokens = line.split(": ");
                if (headerTokens.length == 2) {
                    headers.put(headerTokens[0], headerTokens[1]);
                }
            }

            HttpRequest httpRequest = new HttpRequest(requestLine, headers);
            HttpResponse httpResponse = new HttpResponse(out);

            HttpMethod method = httpRequest.getMethod();
            log.info("Http Method: {}", method.name());
            String url = httpRequest.getPath();
            log.info("path : {}", url);
            DataOutputStream dos = new DataOutputStream(out);

            if (url.startsWith("/create")) {
                String RequestBody = IOUtils.readData(br, Integer.parseInt(headers.get("Content-Length")));
                Map<String, String> parameters = HttpRequestUtils.parseQueryString(RequestBody);
                log.info("user : {}", parameters.toString());
                DataBase.addUser(new User(parameters.get("userId"), parameters.get("password"), parameters.get("name"), parameters.get("email")));
                httpResponse.sendRedirect("/index.html");
            } else if (url.startsWith("/login")) {
                String contentLength = headers.get("Content-Length");
                log.info("contentLength : {}", contentLength);
                String RequestBody = IOUtils.readData(br, Integer.parseInt(headers.get("Content-Length")));

                Map<String, String> info = HttpRequestUtils.parseQueryString(RequestBody);
                String userId = info.get("userId");
                String password = info.get("password");
                User targetUser = DataBase.findUserById(userId);
                log.info("userId : {}", userId);
                log.info("password : {}", password);
                if (targetUser == null || !password.equals(targetUser.getPassword())) {
                    httpResponse.addHeader("Set-Cookie", "logined=false");
                    httpResponse.sendRedirect("/user/login_failed.html");
                } else if (password.equals(targetUser.getPassword())) {
                    log.info("로그인 성공");
                    httpResponse.addHeader("Set-Cookie", "logined=true");
                    httpResponse.sendRedirect("/index.html");
                }
            } else if (url.startsWith("/list")) {
                String cookies = headers.get("Cookie");
                Map<String, String> cookieStringMap = HttpRequestUtils.parseCookies(cookies);
                log.info("Cookie: {}", cookieStringMap.toString());
                if (cookieStringMap.get("logined").equals("false")) {
                    httpResponse.sendRedirect("/user/login.html");
                } else {
                    url = "/user/list.html";
                    byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());
                    String bodyStr = new String(body);
                    int tbodyIndex = bodyStr.indexOf("<tbody>");
                    log.info("bodyStr : {}", bodyStr.substring(0, tbodyIndex + 7));

                    StringBuilder result = new StringBuilder(bodyStr.substring(0, tbodyIndex + 7));
                    Collection<User> users = DataBase.findAll();
                    int id = 0;
                    for (User user : users) {
                        id++;
                        result.append("<tr><th scope=\"row\">")
                                .append(id).append("</th> <td>")
                                .append(user.getUserId()).append("</td> <td>")
                                .append(user.getName()).append("</td> <td>")
                                .append(user.getEmail()).append("</td> <td>")
                                .append("<a href=\"#\" class=\"btn btn - success\" role=\"button\">수정</a></td>");
                    }
                    result.append(bodyStr.substring(tbodyIndex + 7));
                    log.info("result: {}", result.toString());
                    httpResponse.response200Header(result.toString().getBytes().length);
                    httpResponse.responseBody(result.toString().getBytes());
                }
            } else {
                httpResponse.forward(url);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }

    }
}

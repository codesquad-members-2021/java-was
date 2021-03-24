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

            String line = br.readLine();
            System.out.println(line);
            String[] tokens = line.split(" ");
            Map<String, String> headers = new HashMap<>();
            while (!"".equals(line)) {
                System.out.println(line);
                line = br.readLine();
                String[] headerTokens = line.split(": ");
                if (headerTokens.length == 2) {
                    headers.put(headerTokens[0], headerTokens[1]);
                }
            }

            log.info(Arrays.toString(tokens));
            String url = tokens[1];
            log.info("url: {}", url);
            DataOutputStream dos = new DataOutputStream(out);

            if (url.startsWith("/create")) {
                String RequestBody = IOUtils.readData(br, Integer.parseInt(headers.get("Content-Length")));
                Map<String, String> info = HttpRequestUtils.parseQueryString(RequestBody);
                log.info("user : {}", info.toString());
                DataBase.addUser(new User(info.get("userId"), info.get("password"), info.get("name"), info.get("email")));
//                log.info("new user : {}", DataBase.findUserById("trevi").toString());
                response302Header(dos, "/index.html");
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
                    response302HeaderWithCookie(dos, "/user/login_failed.html", "logined=false");
                } else if (password.equals(targetUser.getPassword())) {
                    log.info("로그인 성공");
                    response302HeaderWithCookie(dos, "/index.html", "logined=true");
                }
            } else if (url.startsWith("/list")) {
                String cookies = headers.get("Cookie");
                Map<String, String> cookieStringMap = HttpRequestUtils.parseCookies(cookies);
                log.info("Cookie: {}", cookieStringMap.toString());
                if (cookieStringMap.get("logined").equals("false")) {
                    response302Header(dos, "/user/login.html");
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
//                    response302Header(dos, "/user/list.html");
                    result.append(bodyStr.substring(tbodyIndex + 7));
                    log.info("result: {}", result.toString());
                    response200Header(dos, result.toString().getBytes().length);
                    responseBody(dos, result.toString().getBytes());
                }
            } else {
                byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());
                response200Header(dos, body.length);
                responseBody(dos, body);
            }

        } catch (
                IOException e) {
            log.error(e.getMessage());
        }

    }

    private void response302HeaderWithCookie(DataOutputStream dos, String url, String cookie) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: " + url + "\r\n");
            dos.writeBytes("Set-Cookie: " + cookie + "; Path=/\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response302Header(DataOutputStream dos, String url) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: http://localhost:8080" + url + "\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}

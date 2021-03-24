package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line = br.readLine();
            log.debug("line : {}", line);
            final Map<String, String> headers = parseHeaders(br);
            log.debug("headers : {}, {}", headers.get("Content-Length"), headers.get("Content-Type"));
            String[] firstLine = line.split(" ");
            RequestMethod method = RequestMethod.of(firstLine[0]);
            String url = firstLine[1];
            log.info("url : {}", url);




            if(url.startsWith("/user/create")) {
                if(method == RequestMethod.POST) {
                    String body = IOUtils.readData(br, Integer.parseInt(headers.get("Content-Length")));
                    log.debug("body : {}", body);
                    final Map<String, String> stringQuery = parseQueryString(body);
                    User user = new User(stringQuery.get("userId"), stringQuery.get("password"), stringQuery.get("name"), stringQuery.get("email"));
                    log.debug("user : {}", user);
                }

            }

            DataOutputStream dos = new DataOutputStream(out);

            byte[] body = Files.readAllBytes(new File("./webapp/"+url).toPath());
            response200Header(dos, body.length);
            responseBody(dos, body);
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

    private Map<String, String> parseHeaders(BufferedReader br) throws IOException {
        Map<String, String> headers = new HashMap<>();
        String line = br.readLine();
        while (!"".equals(line)) {
            String[] header = line.split(": ");
            headers.put(header[0], header[1]);
            line = br.readLine();
        }
        return headers;
    }

    private Map<String, String> parseUrl(String url) {
        int i = url.indexOf('?');
        if(i == -1) {
            return new HashMap<>();
        }
        return parseQueryString(url.substring(i + 1, url.length()));
    }

    private Map<String, String> parseQueryString(String queryString) {
        Map<String, String> queryStrings = new HashMap<>();
        String[] split = queryString.split("&");
        for(String query : split) {
            String[] ss = query.split("=");
            queryStrings.put(ss[0], ss[1]);
        }
        return queryStrings;
    }

}

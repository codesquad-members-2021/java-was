package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    private static final Logger log = LoggerFactory.getLogger(HttpRequest.class);

    private String requestLine;
    private Map<String, String> headers;
    private BufferedReader br;
    private String RequestBody = "";
    private String queryString = "";
    private Map<String, String> parameters;

    public HttpRequest(InputStream in) {
        try {
            br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            requestLine = br.readLine();
            String line = requestLine;
            if (line == null) {
                return;
            }
            headers = new HashMap<>();
            while (!line.isEmpty()) {
                log.info("headers : {}", line);
                line = br.readLine();
                if (line == null) {
                    break;
                }
                String[] headerTokens = line.split(": ");
                if (headerTokens.length == 2) {
                    headers.put(headerTokens[0], headerTokens[1]);
                }
            }

            String[] tokens = requestLine.split(" ");
            if (tokens[1].contains("?")) {
                queryString = tokens[1].substring(tokens[1].indexOf("?") + 1);
            }
            if (headers.get("Content-Length") != null) {
                RequestBody = IOUtils.readData(br, Integer.parseInt(headers.get("Content-Length")));
            }
            parameters = HttpRequestUtils.parseQueryString(queryString + "&" + RequestBody);

            log.info("parameters : {}", parameters.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public HttpMethod getMethod() {
        String[] tokens = requestLine.split(" ");
        String method = tokens[0];
        return HttpMethod.valueOf(method);
    }

    public String getPath() {
        String[] tokens = requestLine.split(" ");
        if (tokens[1].contains("?")) {
            return tokens[1].substring(0, tokens[1].indexOf("?"));
        }
        return tokens[1];
    }

    public String getHeader(String fieldName) {
        return headers.get(fieldName);
    }

    public Map<String, String> headers() {
        return headers;
    }

    public String getParameter(String fieldName) {
        return parameters.get(fieldName);
    }
}

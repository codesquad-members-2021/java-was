package http;

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
    private final Map<String, String> headers = new HashMap<>();
    private Map<String, String> parameters;

    public HttpRequest(InputStream in) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            requestLine = br.readLine();
            processHeaders(br);
            processParameters(br);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processHeaders(BufferedReader br) throws IOException {
        String line = br.readLine();
        while (!line.isEmpty()) {
            log.debug("headers : {}", line);
            line = br.readLine();
            if (line == null) {
                return;
            }
            parseHeader(line);
        }
    }

    private void parseHeader(String line) {
        String[] headerTokens = line.split(": ");
        if (headerTokens.length == 2) {
            headers.put(headerTokens[0], headerTokens[1]);
        }
    }

    private void processParameters(BufferedReader br) throws IOException {
        String[] tokens = requestLine.split(" ");
        String queryString = "";
        String requestBody = "";
        if (tokens[1].contains("?")) {
            queryString = tokens[1].substring(tokens[1].indexOf("?") + 1);
        }
        if (headers.get("Content-Length") != null) {
            requestBody = IOUtils.readData(br, Integer.parseInt(headers.get("Content-Length")));
        }
        queryString = queryString + "&" + requestBody;
        parameters = HttpRequestUtils.parseQueryString(queryString);
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

    public String getParameter(String fieldName) {
        return parameters.get(fieldName);
    }
}

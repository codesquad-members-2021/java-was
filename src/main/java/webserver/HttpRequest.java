package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;

import java.io.BufferedReader;
import java.util.Map;

public class HttpRequest {
    private static final Logger log = LoggerFactory.getLogger(HttpRequest.class);

    private String startLine;
    private Map<String, String> headers;

    public HttpRequest(String startLine, Map<String, String> headers) {
        this.startLine = startLine;
        this.headers = headers;
    }

    public HttpMethod getMethod() {
        String[] tokens = startLine.split(" ");
        String method = tokens[0];
        return HttpMethod.valueOf(method);
    }

    public String getPath() {
        String[] tokens = startLine.split(" ");
        return tokens[1];
    }

    public String getHeader(String fieldName) {
        return headers.get(fieldName);
    }

    public String getParameter(String fieldName, BufferedReader br) throws Exception {
        String RequestBody = IOUtils.readData(br, Integer.parseInt(headers.get("Content-Length")));
        Map<String, String> parameters = HttpRequestUtils.parseQueryString(RequestBody);
        log.info("parameters : {}", parameters.toString());
        return parameters.get(fieldName);
    }
}

package webserver;

import util.HttpRequestUtils;

import java.util.HashMap;
import java.util.Map;

public class HttpRequest {

    private String url;
    private String method;
    private String protocol;

    private Map<String, String> headers = new HashMap<>();

    public String getUrl() {
        return url;
    }

    public void addStartLine(String buffer) {
        String[] startLine = buffer.split(" ");
        method = startLine[0];
        url = startLine[1];
        protocol = startLine[2];
    }

    public void addHeaders(String buffer) {
        HttpRequestUtils.Pair pair = HttpRequestUtils.parseHeader(buffer);
        headers.put(pair.getKey(), pair.getValue());
    }
}

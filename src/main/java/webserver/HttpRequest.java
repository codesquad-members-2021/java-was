package webserver;

import util.HttpRequestUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {

    private String url;
    private String method;
    private String protocol;

    private Map<String, String> headers = new HashMap<>();

    private HttpRequest() {

    }

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

    public static HttpRequest of(InputStream in) {
        HttpRequest httpRequest = new HttpRequest();
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String buffer;
        try {
            buffer = br.readLine();
            httpRequest.addStartLine(buffer);
            while(!(buffer = br.readLine()).equals("")) {
                httpRequest.addHeaders(buffer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // TODO: BODY
        return httpRequest;
    }

    public String query(String key) {
        return "";
    }
}

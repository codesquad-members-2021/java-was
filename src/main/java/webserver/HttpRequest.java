package webserver;

import util.HttpRequestUtils;
import util.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {

    private String url;
    private HttpMethod method;
    private String protocol;

    private Map<String, String> headers = new HashMap<>();
    private Map<String, String> data;
    private String body;

    private HttpRequest() {

    }


    public String getUrl() {
        return url;
    }

    public void addStartLine(String buffer) {
        String[] startLine = buffer.split(" ");
        method = HttpMethod.valueOf(startLine[0].toUpperCase());
        url = startLine[1];
        protocol = startLine[2];

        String[] target = startLine[1].split("\\?");
        url = target[0];

        if (target.length > 1 && method == HttpMethod.GET) {
            data = HttpRequestUtils.parseQueryString(target[1]);
        }
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
            while (!(buffer = br.readLine()).equals("")) {
                httpRequest.addHeaders(buffer);
            }

            String contentLength = httpRequest.header("Content-Length");
            if (contentLength != null) {
                httpRequest.addBody(IOUtils.readData(br, Integer.parseInt(contentLength)));
            }


        } catch (IOException e) {
            e.printStackTrace();
        }


        return httpRequest;
    }

    private void addBody(String readData) {
        body = readData;
        if (method == HttpMethod.POST) {
            data = HttpRequestUtils.parseQueryString(readData);
        }

    }

    public String header(String key) {
        return headers.get(key);
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String data(String key) {
        return data.get(key);
    }
}

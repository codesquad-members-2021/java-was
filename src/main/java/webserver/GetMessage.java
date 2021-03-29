package webserver;

import util.HttpRequestUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

public class GetMessage implements RequestMessage {
    private RequestHeader header;

    private GetMessage(RequestHeader header) {
        this.header = header;
    }

    public static GetMessage from(String getMessage) {
        return new GetMessage(Header.requestHeaderFrom(getMessage));
    }

    @Override
    public RequestHeader getHeader() {
        return header;
    }

    @Override
    public String getMethod() {
        return header.getMethod();
    }

    @Override
    public Map<String, String> getParameters() {
        Map<String, String> statusLine = header.getStatusLineAttributes();

        try {
            URI uri = new URI(statusLine.get(RequestHeader.PATH_KEY));
            return HttpRequestUtils.parseQueryString(uri.getQuery());

        } catch (URISyntaxException e) {
            throw new IllegalStateException("Request의 Path가 올바르지 않음. path : " + statusLine.get(RequestHeader.PATH_KEY), e);
        }
    }
}

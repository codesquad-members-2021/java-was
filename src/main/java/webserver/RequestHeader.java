package webserver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestHeader extends Header {
    protected static final String METHOD_KEY = "method";
    protected static final String PATH_KEY = "path";

    protected RequestHeader(Map<String, String> statusLineAttributes, Map<String, String> attributes) {
        super(statusLineAttributes, attributes);
    }

    public static RequestHeader of(List<String> statusLine, Map<String, String> attributes) {
        Map<String, String> statusLineAttributes = new HashMap<>();

        statusLineAttributes.put(METHOD_KEY, statusLine.get(0));
        statusLineAttributes.put(PATH_KEY, statusLine.get(1));
        statusLineAttributes.put(PROTOCOL_VERSION_KEY, statusLine.get(2));

        return new RequestHeader(statusLineAttributes, attributes);
    }

    public String getMethod() {
        return getStatusLineAttributes().get(METHOD_KEY);
    }

    @Override
    protected String statusLine() {
        return statusLineAttributes.get(METHOD_KEY) + " " + statusLineAttributes.get(PATH_KEY) + " " + statusLineAttributes.get(PROTOCOL_VERSION_KEY);
    }
}

package webserver;

import java.util.Map;

public class RequestHeader extends Header {
    private static final String METHOD_KEY = "method";
    private static final String PATH_KEY = "path";

    protected RequestHeader(Map<String, String> attributes) {
        super(attributes);
    }

    public String getMethod() {
        return getStatusLineAttributes().get(METHOD_KEY);
    }

    @Override
    protected void putStatusLine(String[] statusLine) {
        statusLineAttributes.put(METHOD_KEY, statusLine[0]);
        statusLineAttributes.put(PATH_KEY, statusLine[1]);
        statusLineAttributes.put(PROTOCOL_VERSION_KEY, statusLine[2]);
    }

    @Override
    protected String statusLine() {
        return statusLineAttributes.get(METHOD_KEY) + " " + statusLineAttributes.get(PATH_KEY) + " " + statusLineAttributes.get(PROTOCOL_VERSION_KEY);
    }
}

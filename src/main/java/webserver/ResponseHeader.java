package webserver;

import java.util.Map;

public class ResponseHeader extends Header {
    private static final String STATUS_CODE_KEY = "statusCode";
    private static final String STATUS_TEXT_KEY = "statusText";

    public ResponseHeader(Map<String, String> attributes) {
        super(attributes);
    }

    @Override
    protected void putStatusLine(String[] statusLine) {
        statusLineAttributes.put(PROTOCOL_VERSION_KEY, statusLine[0]);
        statusLineAttributes.put(STATUS_CODE_KEY, statusLine[1]);
        statusLineAttributes.put(STATUS_TEXT_KEY, statusLine[2]);
    }

    @Override
    protected String statusLine() {
        return statusLineAttributes.get(PROTOCOL_VERSION_KEY) + " " + statusLineAttributes.get(STATUS_CODE_KEY) + " " + statusLineAttributes.get(STATUS_TEXT_KEY);
    }
}

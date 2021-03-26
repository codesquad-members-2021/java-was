package webserver;

import util.HttpRequestUtils;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public abstract class Header {
    protected static final String PROTOCOL_VERSION_KEY = "protocolVersion";

    protected Map<String, String> statusLineAttributes;
    private Map<String, String> attributes;

    public Header(Map<String, String> attributes) {
        this.attributes = attributes;
        this.statusLineAttributes = new HashMap<>();
    }

    public static Header of(String headerText, String type) {
        Map<String, String> attributes = attributeFrom(headerText);

        String[] splittedHeaderTexts = headerText.split(System.lineSeparator());
        String[] statusLine = splittedHeaderTexts[0].split(" ");

        Header header = of(attributes, type);
        header.putStatusLine(statusLine);

        return header;
    }

    private static Header of(Map<String, String> attributes, String type) {
        switch (type) {
            case "request":
                return new RequestHeader(attributes);
            case "response":
                return new ResponseHeader(attributes);
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }
    }

    private static Map<String, String> attributeFrom(String headerText) {
        Map<String, String> attributes = new LinkedHashMap<>();

        String[] splittedHeaderTexts = headerText.split(System.lineSeparator());
        for (String splittedHeaderText : splittedHeaderTexts) {
            HttpRequestUtils.Pair pair = HttpRequestUtils.parseHeader(splittedHeaderText);

            if (pair != null) {
                attributes.put(pair.getKey(), pair.getValue());
            }
        }

        return attributes;
    }

    protected abstract void putStatusLine(String[] statusLine);

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public Map<String, String> getStatusLineAttributes() {
        return statusLineAttributes;
    }

    public byte[] toByte() {
        StringBuilder sb = new StringBuilder();

        sb.append(statusLine()).append(System.lineSeparator());

        for (Map.Entry<String, String> entry : getAttributes().entrySet()) {
            sb.append(entry.getKey() + ": " + entry.getValue() + System.lineSeparator());
        }

        sb.append(System.lineSeparator());

        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }

    protected abstract String statusLine();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Header header = (Header) o;
        return Objects.equals(statusLineAttributes, header.statusLineAttributes) && Objects.equals(attributes, header.attributes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(statusLineAttributes, attributes);
    }
}

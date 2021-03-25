package webserver;

import util.HttpRequestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Header {

    private String protocolVersion;
    private String statusCode;
    private String statusText;
    private String contentType;
    private int contentLength;

    public Header(String protocolVersion, String statusCode, String statusText, String contentType, int contentLength) {
        this.protocolVersion = protocolVersion;
        this.statusCode = statusCode;
        this.statusText = statusText;
        this.contentType = contentType;
        this.contentLength = contentLength;
    }

    public static Header from(String headerText) {
        String[] splittedHeaderTexts = headerText.split(System.lineSeparator());

        List<HttpRequestUtils.Pair> pairs = new ArrayList<>();

        String protocolVersion = "";
        String statusCode = "";
        String statusText = "";
        String contentType = "";
        int contentLength = -1;

        for (String splittedHeaderText : splittedHeaderTexts) {
            pairs.add(HttpRequestUtils.parseHeader(splittedHeaderText));

            HttpRequestUtils.Pair pair = HttpRequestUtils.parseHeader(splittedHeaderText);

            if (pair != null) {
                contentType = pair.getKey().equals("Content-Type") ? pair.getValue() : contentType;
                contentLength = pair.getKey().equals("Content-Length") ? Integer.parseInt(pair.getValue()) : contentLength;
            }
        }

        String[] statusLine = splittedHeaderTexts[0].split(" ");

        protocolVersion = statusLine[0];
        statusCode = statusLine[1];
        statusText = statusLine[2];

        return new Header(protocolVersion, statusCode, statusText, contentType, contentLength);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Header header = (Header) o;
        return contentLength == header.contentLength && Objects.equals(protocolVersion, header.protocolVersion) && Objects.equals(statusCode, header.statusCode) && Objects.equals(statusText, header.statusText) && Objects.equals(contentType, header.contentType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(protocolVersion, statusCode, statusText, contentType, contentLength);
    }
}

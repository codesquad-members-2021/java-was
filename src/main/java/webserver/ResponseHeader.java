package webserver;

import util.HttpRequestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ResponseHeader extends Header {
    private String statusCode;
    private String statusText;
    private String contentType;
    private int contentLength;

    public ResponseHeader(String protocolVersion, String statusCode, String statusText, String contentType, int contentLength) {
        super(protocolVersion);
        this.statusCode = statusCode;
        this.statusText = statusText;
        this.contentType = contentType;
        this.contentLength = contentLength;
    }

    public static Header from(String headerText) {
        String[] splittedHeaderTexts = headerText.split(System.lineSeparator());

        Builder builder = new Builder();
        List<HttpRequestUtils.Pair> pairs = new ArrayList<>();

        for (String splittedHeaderText : splittedHeaderTexts) {
            pairs.add(HttpRequestUtils.parseHeader(splittedHeaderText));

            HttpRequestUtils.Pair pair = HttpRequestUtils.parseHeader(splittedHeaderText);

            if (pair != null && pair.getKey().equals("Content-Type")) {
                builder.contentType(pair.getValue());
            }
            if (pair != null && pair.getKey().equals("Content-Length")) {
                builder.contentLength(Integer.parseInt(pair.getValue()));
            }
        }

        String[] statusLine = splittedHeaderTexts[0].split(" ");

        builder.protocolVersion(statusLine[0]);
        builder.statusCode(statusLine[1]);
        builder.statusText(statusLine[2]);

        return builder.build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String protocolVersion;
        private String statusCode;
        private String statusText;
        private String contentType;
        private int contentLength;

        public Builder protocolVersion(String protocolVersion) {
            this.protocolVersion = protocolVersion;
            return this;
        }

        public Builder statusCode(String statusCode) {
            this.statusCode = statusCode;
            return this;
        }

        public Builder statusText(String statusText) {
            this.statusText = statusText;
            return this;
        }

        public Builder contentType(String contentType) {
            this.contentType = contentType;
            return this;
        }

        public Builder contentLength(int contentLength) {
            this.contentLength = contentLength;
            return this;
        }

        public ResponseHeader build() {
            return new ResponseHeader(protocolVersion, statusCode, statusText, contentType, contentLength);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ResponseHeader that = (ResponseHeader) o;
        return contentLength == that.contentLength && Objects.equals(statusCode, that.statusCode) && Objects.equals(statusText, that.statusText) && Objects.equals(contentType, that.contentType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), statusCode, statusText, contentType, contentLength);
    }
}

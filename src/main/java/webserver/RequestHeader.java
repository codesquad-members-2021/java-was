package webserver;

import util.HttpRequestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RequestHeader extends Header {
    private String cookie;
    private String method;
    private String path;
    private String host;

    public RequestHeader(String protocolVersion, String cookie, String method, String path, String host) {
        super(protocolVersion);
        this.cookie = cookie;
        this.method = method;
        this.path = path;
        this.host = host;
    }

    public static RequestHeader from(String headerText) {
        String[] splittedHeaderTexts = headerText.split(System.lineSeparator());

        Builder builder = new Builder();
        List<HttpRequestUtils.Pair> pairs = new ArrayList<>();

        for (String splittedHeaderText : splittedHeaderTexts) {
            pairs.add(HttpRequestUtils.parseHeader(splittedHeaderText));

            HttpRequestUtils.Pair pair = HttpRequestUtils.parseHeader(splittedHeaderText);

            if (pair != null && pair.getKey().equals("Host")) {
                builder.host(pair.getValue());
            }
            if (pair != null && pair.getKey().equals("Cookie")) {
                builder.cookie(pair.getValue());
            }
        }

        String[] statusLine = splittedHeaderTexts[0].split(" ");

        builder.method(statusLine[0]);
        builder.path(statusLine[1]);
        builder.protocolVersion(statusLine[2]);

        return builder.build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String protocolVersion;
        private String cookie;
        private String method;
        private String path;
        private String host;

        public Builder protocolVersion(String protocolVersion) {
            this.protocolVersion = protocolVersion;
            return this;
        }

        public Builder cookie(String cookie) {
            this.cookie = cookie;
            return this;
        }

        public Builder method(String method) {
            this.method = method;
            return this;
        }

        public Builder path(String path) {
            this.path = path;
            return this;
        }

        public Builder host(String host) {
            this.host = host;
            return this;
        }

        public RequestHeader build() {
            return new RequestHeader(protocolVersion, cookie, method, path, host);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        RequestHeader that = (RequestHeader) o;
        return Objects.equals(cookie, that.cookie) && Objects.equals(method, that.method) && Objects.equals(path, that.path) && Objects.equals(host, that.host);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), cookie, method, path, host);
    }
}

package webserver;

import java.util.Objects;

public class Header {
    private String protocolVersion;

    public Header(String protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Header header = (Header) o;
        return Objects.equals(protocolVersion, header.protocolVersion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(protocolVersion);
    }
}

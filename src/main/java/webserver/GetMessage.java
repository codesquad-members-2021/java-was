package webserver;

public class GetMessage {
    private Header header;

    private GetMessage(Header header) {
        this.header = header;
    }

    public static GetMessage from(String getMessage) {
        return new GetMessage(Header.of(getMessage, "request"));
    }

    public Header getHeader() {
        return header;
    }
}

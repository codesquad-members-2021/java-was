package webserver;

public class GetMessage {
    private RequestHeader header;

    private GetMessage(RequestHeader header) {
        this.header = header;
    }

    public static GetMessage from(String getMessage) {
        return new GetMessage(Header.requestHeaderFrom(getMessage));
    }

    public RequestHeader getHeader() {
        return header;
    }

    public String getMethod() {
        return header.getMethod();
    }
}

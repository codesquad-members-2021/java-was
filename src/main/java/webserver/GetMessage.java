package webserver;

public class GetMessage implements RequestMessage {
    private RequestHeader header;

    private GetMessage(RequestHeader header) {
        this.header = header;
    }

    public static GetMessage from(String getMessage) {
        return new GetMessage(Header.requestHeaderFrom(getMessage));
    }

    @Override
    public RequestHeader getHeader() {
        return header;
    }

    @Override
    public String getMethod() {
        return header.getMethod();
    }
}

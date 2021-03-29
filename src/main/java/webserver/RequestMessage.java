package webserver;

public interface RequestMessage {
    RequestHeader getHeader();

    String getMethod();
}

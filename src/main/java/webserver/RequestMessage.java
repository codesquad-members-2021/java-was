package webserver;

import java.util.Map;

public interface RequestMessage {
    RequestHeader getHeader();

    String getMethod();

    Map<String, String> getParameters();
}

package webserver;

import java.util.Locale;

public enum RequestMethod {
    GET, POST;

    public static RequestMethod of(String method) {
        return RequestMethod.valueOf(method.toUpperCase());
    }
}

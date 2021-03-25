package webserver;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class Body {
    private static final Charset DEFAULT_ENCODING = StandardCharsets.UTF_8;

    private byte[] data;

    private Body(byte[] data) {
        this.data = data;
    }

    public static Body create(String bodyText) {
        return new Body(bodyText.getBytes(DEFAULT_ENCODING));
    }
}

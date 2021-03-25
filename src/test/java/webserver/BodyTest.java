package webserver;

import org.junit.jupiter.api.Test;

class BodyTest {

    @Test
    void init() {
        String data = "Hello World";
        Body expected = Body.create(data);

        Body body = Body.create(data);
    }
}

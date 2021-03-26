package webserver;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class BodyTest {

    @Test
    void init() {
        String data = "Hello World";
        Body expectedBody = Body.create(data);

        Body body = Body.create(data);

        assertThat(body).isEqualTo(expectedBody);
    }
}

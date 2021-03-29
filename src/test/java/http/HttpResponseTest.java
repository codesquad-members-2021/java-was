package http;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;

import org.assertj.core.api.SoftAssertions;

class HttpResponseTest {

    private SoftAssertions softly;
    private ByteArrayOutputStream os;

    @BeforeEach
    public void beforeEach() {
        softly = new SoftAssertions();
        os = new ByteArrayOutputStream();
    }

    @AfterEach
    public void afterEach() {
        softly.assertAll();
    }

    @Test
    public void sendOk() {
        HttpResponse response = new HttpResponse(os);
        response.forward("/index.html");

        softly.assertThat(os.toString()).contains("HTTP/1.1 200 OK\r\n");
        softly.assertThat(os.toString()).contains("Content-Type: text/html;charset=utf-8\r\n");
        softly.assertThat(os.toString()).contains("Content-Length: 150\r\n");
        softly.assertThat(os.toString()).contains("<title>SLiPP Java Web Programming</title>");
    }

}

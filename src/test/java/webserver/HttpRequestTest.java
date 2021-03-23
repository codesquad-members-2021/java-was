package webserver;

import static org.assertj.core.api.Assertions.*;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;

class HttpRequestTest {

    private SoftAssertions softly;

    @BeforeEach
    public void beforeEach() {
        softly = new SoftAssertions();
    }

    @AfterEach
    public void afterEach() {
        softly.assertAll();
    }

    @Test
    @DisplayName("리퀘스트 객체를 생성")
    public void createRequest() {
        String request = "GET /index.html HTTP/1.1\r\n" +
                "Host: localhost:8080\r\n" +
                "Connection: keep-alive\r\n" +
                "Accept: */*\r\n\r\n";
        InputStream in = new ByteArrayInputStream(request.getBytes());
        HttpRequest httpRequest = HttpRequest.of(in);
        softly.assertThat(httpRequest.getUrl()).isEqualTo("/index.html");
    }

    @Test
    @DisplayName("리퀘스트 쿼리 분석")
    public void requestQuery() {
        String request = "GET /user/create?userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net HTTP/1.1\r\n" +
                "Host: localhost:8080\r\n" +
                "Connection: keep-alive\r\n" +
                "Accept: */*\r\n\r\n";
        InputStream in = new ByteArrayInputStream(request.getBytes());
        HttpRequest httpRequest = HttpRequest.of(in);
        softly.assertThat(httpRequest.query("userId")).as("User ID").isEqualTo("javajigi");
        softly.assertThat(httpRequest.query("password")).as("Password").isEqualTo("password");
        softly.assertThat(httpRequest.query("name")).as("Name").isEqualTo("%EB%B0%95%EC%9E%AC%EC%84%B1");
        softly.assertThat(httpRequest.query("email")).as("Email").isEqualTo("javajigi%40slipp.net");
    }
}

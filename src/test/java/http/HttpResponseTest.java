package http;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.assertj.core.api.SoftAssertions;

class HttpResponseTest {

    private SoftAssertions softly;
    private ByteArrayOutputStream os;
    private HttpResponse response;

    @BeforeEach
    public void beforeEach() {
        softly = new SoftAssertions();
        os = new ByteArrayOutputStream();
        response = new HttpResponse(os);
    }

    @AfterEach
    public void afterEach() {
        softly.assertAll();
    }

    @Test
    @DisplayName("index.html 읽기")
    public void sendOk() throws IOException {
        response.forward("/index.html");

        assertResponseHeader("/index.html", "html");
        softly.assertThat(os.toString()).contains("<title>SLiPP Java Web Programming</title>");
    }

    @Test
    @DisplayName("css 파일 읽기")
    public void sendOkCss() throws IOException {
        response.forward("/css/styles.css");

        assertResponseHeader("/css/styles.css", "css");
        softly.assertThat(os.toString()).contains(".navbar-default .dropdown-menu li > a {padding-left:30px;}");
    }

    @Test
    @DisplayName("redirect to /index.html")
    public void sendRedirect() throws IOException {
        String url = "/index.html";
        response.redirect(url);

        assertRedirectResponseHeader(url);
    }

    @Test
    @DisplayName("redirect cookie test")
    public void sendRedirectWithCookie() throws IOException {
        String url = "/index.html";
        response.addHeader("Set-Cookie", "logined=true");
        response.redirect(url);

        assertRedirectResponseHeader(url);
        softly.assertThat(os.toString())
                .contains("Set-Cookie: logined=true");
    }

    private void assertRedirectResponseHeader(String url) {
        softly.assertThat(os.toString())
                .contains("HTTP/1.1 302 FOUND \r\n")
                .contains("Location: " + url + "\r\n")
                .contains("\r\n\r\n");
    }

    private void assertResponseHeader(String url, String type) throws IOException {
        byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());

        softly.assertThat(os.toString())
                .contains("HTTP/1.1 200 OK \r\n")
                .contains("Content-Type: text/" + type + ";charset=utf-8\r\n")
                .contains("Content-Length: " + body.length + "\r\n");
    }

}

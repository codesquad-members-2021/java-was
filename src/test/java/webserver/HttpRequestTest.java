package webserver;

import http.HttpRequest;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.InjectSoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;

import java.io.FileInputStream;
import java.io.InputStream;

@ExtendWith(SoftAssertionsExtension.class)
class HttpRequestTest {
    private final String testDirectory = "./src/test/resources/";

    @InjectSoftAssertions
    SoftAssertions softly;

    @Test
    public void request_GET() throws Exception {
        InputStream in = new FileInputStream(testDirectory + "Http_GET.txt");
        HttpRequest request = new HttpRequest(in);

        softly.assertThat(request.getMethod().name()).isEqualTo("GET");
        softly.assertThat(request.getPath()).isEqualTo("/user/create");
        softly.assertThat(request.getHeader("Connection")).isEqualTo("keep-alive");
        softly.assertThat(request.getParameter("userId")).isEqualTo("javajigi");
    }

    @Test
    public void request_POST() throws Exception {
        InputStream in = new FileInputStream(testDirectory + "Http_POST.txt");
        HttpRequest request = new HttpRequest(in);

        softly.assertThat(request.getMethod().name()).isEqualTo("POST");
        softly.assertThat(request.getPath()).isEqualTo("/user/create");
        softly.assertThat(request.getHeader("Connection")).isEqualTo("keep-alive");
        softly.assertThat(request.getParameter("userId")).isEqualTo("javajigi");
    }

    @Test
    public void request_POST2() throws Exception {
        InputStream in = new FileInputStream(testDirectory + "Http_POST2.txt");
        HttpRequest request = new HttpRequest(in);

        softly.assertThat(request.getMethod().name()).isEqualTo("POST");
        softly.assertThat(request.getPath()).isEqualTo("/user/create");
        softly.assertThat(request.getHeader("Connection")).isEqualTo("keep-alive");
        softly.assertThat(request.getParameter("id")).isEqualTo("1");
        softly.assertThat(request.getParameter("userId")).isEqualTo("javajigi");
    }
}

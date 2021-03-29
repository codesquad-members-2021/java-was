package webserver;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

class HttpRequestTest {

    private String testDirectory = "./src/test/resources/";

    @Test
    public void request_GET() throws Exception {
        InputStream in = new FileInputStream(testDirectory + "Http_GET.txt");
        HttpRequest request = new HttpRequest(in);

        assertThat("GET").isEqualTo(request.getMethod().name());
        assertThat("/user/create").isEqualTo(request.getPath());
        assertThat("keep-alive").isEqualTo(request.getHeader("Connection"));
        assertThat("javajigi").isEqualTo(request.getParameter("userId"));
    }

    @Test
    public void request_POST() throws Exception {
        InputStream in = new FileInputStream(testDirectory + "Http_POST.txt");
        HttpRequest request = new HttpRequest(in);

        assertThat("POST").isEqualTo(request.getMethod().name());
        assertThat("/user/create").isEqualTo(request.getPath());
        assertThat("keep-alive").isEqualTo(request.getHeader("Connection"));
        assertThat("javajigi").isEqualTo(request.getParameter("userId"));
    }

}

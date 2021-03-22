package webserver;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;

class HttpRequestTest {

    private String request = "GET /index.html HTTP/1.1\r\n" +
            "Host: localhost:8080\r\n" +
            "Connection: keep-alive\r\n" +
            "Accept: */*\r\n\r\n";

    @BeforeEach
    public void beforeEach() {

    }

    @Test
    @DisplayName("리퀘스트 객체를 생성")
    public void createRequest() {
        InputStream in = new ByteArrayInputStream(request.getBytes());
        HttpRequest httpRequest = new HttpRequest();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
            String buffer;
            buffer = br.readLine();
            httpRequest.addStartLine(buffer);
            while(!(buffer = br.readLine()).equals("")) {
                httpRequest.addHeaders(buffer);
            }
            // TODO: BODY

        } catch (IOException e) {
            e.printStackTrace();
        }
        assertThat(httpRequest.url()).isEqualTo("/index.html");

    }
}

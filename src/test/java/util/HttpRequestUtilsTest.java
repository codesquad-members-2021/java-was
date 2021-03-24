package util;

import org.junit.jupiter.api.Test;
import util.HttpRequestUtils.Pair;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;


class HttpRequestUtilsTest {
    @Test
    void parseQueryString() {
        String queryString = "userId=javajigi";
        Map<String, String> parameters = HttpRequestUtils.parseQueryString(queryString);
        assertThat(parameters.get("userId")).isEqualTo(("javajigi"));
        assertThat(parameters.get("password")).isNull();

        queryString = "userId=javajigi&password=password2";
        parameters = HttpRequestUtils.parseQueryString(queryString);
        assertThat(parameters.get("userId")).isEqualTo(("javajigi"));
        assertThat(parameters.get("password")).isEqualTo("password2");
    }

    @Test
    void parseQueryString_null() {
        Map<String, String> parameters = HttpRequestUtils.parseQueryString(null);
        assertThat(parameters.isEmpty()).isTrue();

        parameters = HttpRequestUtils.parseQueryString("");
        assertThat(parameters.isEmpty()).isTrue();

        parameters = HttpRequestUtils.parseQueryString(" ");
        assertThat(parameters.isEmpty()).isTrue();
    }

    @Test
    void parseQueryString_invalid() {
        String queryString = "userId=javajigi&password";
        Map<String, String> parameters = HttpRequestUtils.parseQueryString(queryString);
        assertThat(parameters.get("userId")).isEqualTo("javajigi");
        assertThat(parameters.get("password")).isNull();
    }

    @Test
    void parseCookies() {
        String cookies = "logined=true; JSessionId=1234";
        Map<String, String> parameters = HttpRequestUtils.parseCookies(cookies);
        assertThat(parameters.get("logined")).isEqualTo("true");
        assertThat(parameters.get("JSessionId")).isEqualTo("1234");
        assertThat(parameters.get("session")).isNull();
    }

    @Test
    void getKeyValue(){
        Pair pair = HttpRequestUtils.getKeyValue("userId=javajigi", "=");
        assertThat(pair).isEqualTo(new Pair("userId", "javajigi"));
    }

    @Test
    void getKeyValue_invalid(){
        Pair pair = HttpRequestUtils.getKeyValue("userId", "=");
        assertThat(pair).isNull();
    }

    @Test
    void parseHeader(){
        String header = "Content-Length: 59";
        Pair pair = HttpRequestUtils.parseHeader(header);
        assertThat(pair).isEqualTo(new Pair("Content-Length", "59"));
    }
}

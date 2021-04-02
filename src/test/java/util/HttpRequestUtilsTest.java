package util;

import java.util.Map;

import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.InjectSoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import util.HttpRequestUtils.Pair;

@ExtendWith(SoftAssertionsExtension.class)
class HttpRequestUtilsTest {

    @InjectSoftAssertions
    SoftAssertions softly;

    @Test
    void parseQueryString() {
        String queryString = "userId=javajigi";
        Map<String, String> parameters = HttpRequestUtils.parseQueryString(queryString);
        softly.assertThat(parameters.get("userId")).isEqualTo("javajigi");
        softly.assertThat(parameters.get("password")).isNull();

        queryString = "userId=javajigi&password=password2";
        parameters = HttpRequestUtils.parseQueryString(queryString);
        softly.assertThat(parameters.get("userId")).isEqualTo("javajigi");
        softly.assertThat(parameters.get("password")).isEqualTo("password2");
    }

    @Test
    void parseQueryString_null() {
        Map<String, String> parameters = HttpRequestUtils.parseQueryString(null);
        softly.assertThat(parameters.isEmpty()).isTrue();

        parameters = HttpRequestUtils.parseQueryString("");
        softly.assertThat(parameters.isEmpty()).isTrue();

        parameters = HttpRequestUtils.parseQueryString(" ");
        softly.assertThat(parameters.isEmpty()).isTrue();
    }

    @Test
    void parseQueryString_invalid() {
        String queryString = "userId=javajigi&password";
        Map<String, String> parameters = HttpRequestUtils.parseQueryString(queryString);
        softly.assertThat(parameters.get("userId")).isEqualTo("javajigi");
        softly.assertThat(parameters.get("password")).isNull();
    }

    @Test
    void parseCookies() {
        String cookies = "logined=true; JSessionId=1234";
        Map<String, String> parameters = HttpRequestUtils.parseCookies(cookies);
        softly.assertThat(parameters.get("logined")).isEqualTo("true");
        softly.assertThat(parameters.get("JSessionId")).isEqualTo("1234");
        softly.assertThat(parameters.get("session")).isNull();
    }

    @Test
    void getKeyValue() {
        Pair pair = HttpRequestUtils.getKeyValue("userId=javajigi", "=");
        softly.assertThat(pair).isEqualTo(new Pair("userId", "javajigi"));
    }

    @Test
    void getKeyValue_invalid() {
        Pair pair = HttpRequestUtils.getKeyValue("userId", "=");
        softly.assertThat(pair).isNull();
    }

    @Test
    void parseHeader() {
        String header = "Content-Length: 59";
        Pair pair = HttpRequestUtils.parseHeader(header);
        softly.assertThat(pair).isEqualTo(new Pair("Content-Length", "59"));
    }
}

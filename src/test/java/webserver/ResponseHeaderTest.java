package webserver;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class ResponseHeaderTest {
    @ParameterizedTest
    @MethodSource
    void getAttributes(String headerText, Map<String, String> expectedAttributes) {
        assertThat(Header.responseHeaderFrom(headerText).getAttributes())
                .isEqualTo(expectedAttributes);
    }

    @SuppressWarnings("unused")
    static Stream<Arguments> getAttributes() {
        return Stream.of(
                Arguments.of(
                        "HTTP/1.1 200 OK" + System.lineSeparator() +
                                "Content-Type: text/html;charset=utf-8" + System.lineSeparator() +
                                "Content-Length: " + "Hello World".getBytes().length + System.lineSeparator() +
                                System.lineSeparator(),
                        new LinkedHashMap() {{
                            put("Content-Type", "text/html;charset=utf-8");
                            put("Content-Length", String.valueOf("Hello World".getBytes().length));
                        }}
                )
        );
    }

    @ParameterizedTest
    @MethodSource
    void getStatusLineAttributes(String headerText, Map<String, String> expectedAttributes) {
        assertThat(Header.responseHeaderFrom(headerText).getStatusLineAttributes())
                .isEqualTo(expectedAttributes);
    }

    @SuppressWarnings("unused")
    static Stream<Arguments> getStatusLineAttributes() {
        return Stream.of(
                Arguments.of(
                        "HTTP/1.1 200 OK" + System.lineSeparator() +
                                "Content-Type: text/html;charset=utf-8" + System.lineSeparator() +
                                "Content-Length: " + "Hello World".getBytes().length + System.lineSeparator() +
                                System.lineSeparator(),
                        new HashMap() {{
                            put("protocolVersion", "HTTP/1.1");
                            put("statusText", "OK");
                            put("statusCode", "200");
                        }}
                )
        );
    }

    @ParameterizedTest
    @MethodSource
    void toByte(String headerText, byte[] expectedHeaderByte) {
        byte[] headerByte = Header.responseHeaderFrom(headerText).toByte();

        assertAll(
                () -> assertThat(headerByte).isEqualTo(expectedHeaderByte),
                () -> assertThat(new String(headerByte)).isEqualTo(new String(expectedHeaderByte))
        );
    }

    @SuppressWarnings("unused")
    static Stream<Arguments> toByte() {
        return Stream.of(
                Arguments.of(
                        "HTTP/1.1 200 OK" + System.lineSeparator() +
                                "Content-Type: text/html;charset=utf-8" + System.lineSeparator() +
                                "Content-Length: " + "Hello World".getBytes().length + System.lineSeparator() +
                                System.lineSeparator(),
                        ("HTTP/1.1 200 OK" + System.lineSeparator() +
                                "Content-Type: text/html;charset=utf-8" + System.lineSeparator() +
                                "Content-Length: " + "Hello World".getBytes().length + System.lineSeparator() +
                                System.lineSeparator()).getBytes(StandardCharsets.UTF_8)
                )
        );
    }
}

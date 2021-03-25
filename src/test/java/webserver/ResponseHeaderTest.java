package webserver;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class ResponseHeaderTest {
    @ParameterizedTest
    @MethodSource
    void from(String headerText, ResponseHeader expectedHeader) {
        assertThat(ResponseHeader.from(headerText))
                .isEqualTo(expectedHeader);
    }

    @SuppressWarnings("unused")
    static Stream<Arguments> from() {
        return Stream.of(
                Arguments.of(
                        "HTTP/1.1 200 OK " + System.lineSeparator() +
                                "Content-Type: text/html;charset=utf-8" + System.lineSeparator() +
                                "Content-Length: " + "Hello World".getBytes().length + System.lineSeparator() +
                                System.lineSeparator(),
                        ResponseHeader.builder()
                                .protocolVersion("HTTP/1.1")
                                .statusCode("200")
                                .statusText("OK")
                                .contentType("text/html;charset=utf-8")
                                .contentLength("Hello World".getBytes().length)
                                .build()
                )
        );
    }
}

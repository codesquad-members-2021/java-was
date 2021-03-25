package webserver;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class RequestHeaderTest {
    @ParameterizedTest
    @MethodSource
    void from(String headerText, RequestHeader expectedHeader) {
        assertThat(RequestHeader.from(headerText))
                .isEqualTo(expectedHeader);
    }

    @SuppressWarnings("unused")
    static Stream<Arguments> from() {
        return Stream.of(
                Arguments.of("GET / HTTP/1.1" + System.lineSeparator() +
                                "Host: localhost:8080" + System.lineSeparator() +
                                "Connection: keep-alive" + System.lineSeparator() +
                                "Cache-Control: max-age=0" + System.lineSeparator() +
                                "sec-ch-ua: \"Google Chrome\";v=\"89\", \"Chromium\";v=\"89\", \";Not A Brand\";v=\"99\"" + System.lineSeparator() +
                                "sec-ch-ua-mobile: ?0" + System.lineSeparator() +
                                "Upgrade-Insecure-Requests: 1" + System.lineSeparator() +
                                "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.90 Safari/537.36" + System.lineSeparator() +
                                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9" + System.lineSeparator() +
                                "Sec-Fetch-Site: none" + System.lineSeparator() +
                                "Sec-Fetch-Mode: navigate" + System.lineSeparator() +
                                "Sec-Fetch-User: ?1" + System.lineSeparator() +
                                "Sec-Fetch-Dest: document" + System.lineSeparator() +
                                "Accept-Encoding: gzip, deflate, br" + System.lineSeparator() +
                                "Accept-Language: ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7" + System.lineSeparator() +
                                "Cookie: Idea-1c77831=5ced54c8-cabd-4355-ae5a-97b17f9d7443" + System.lineSeparator() +
                                System.lineSeparator(),
                        RequestHeader.builder()
                                .protocolVersion("HTTP/1.1")
                                .method("GET")
                                .path("/")
                                .cookie("Idea-1c77831=5ced54c8-cabd-4355-ae5a-97b17f9d7443")
                                .host("localhost:8080")
                                .build()
                )
        );
    }
}

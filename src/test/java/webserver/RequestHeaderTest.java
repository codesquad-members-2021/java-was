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

class RequestHeaderTest {

    @ParameterizedTest
    @MethodSource
    void getAttributes(String headerText, Map<String, String> expectedAttributes) {
        assertThat(Header.of(headerText, "request").getAttributes())
                .isEqualTo(expectedAttributes);
    }

    @ParameterizedTest
    @MethodSource
    void getMethod(String headerText, String expectedMethod) {
        assertThat(((RequestHeader) Header.of(headerText, "request")).getMethod())
                .isEqualTo(expectedMethod);
    }

    static Stream<Arguments> getMethod() {
        return Stream.of(
                Arguments.of(
                        "GET / HTTP/1.1" + System.lineSeparator() +
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
                        "GET"
                ), Arguments.of(
                        "POST /user/create HTTP/1.1" + System.lineSeparator() +
                                "Host: localhost:8080" + System.lineSeparator() +
                                "Connection: keep-alive" + System.lineSeparator() +
                                "Content-Length: 59" + System.lineSeparator() +
                                "Content-Type: application/x-www-form-urlencoded" + System.lineSeparator() +
                                "Accept: */*" + System.lineSeparator() +
                                "" + System.lineSeparator() +
                                "userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net",
                        "POST"
                )
        );
    }

    @SuppressWarnings("unused")
    static Stream<Arguments> getAttributes() {
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
                        new LinkedHashMap() {{
                            put("Host", "localhost:8080");
                            put("Connection", "keep-alive");
                            put("Cache-Control", "max-age=0");
                            put("sec-ch-ua", "\"Google Chrome\";v=\"89\", \"Chromium\";v=\"89\", \";Not A Brand\";v=\"99\"");
                            put("sec-ch-ua-mobile", "?0");
                            put("Upgrade-Insecure-Requests", "1");
                            put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.90 Safari/537.36");
                            put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
                            put("Sec-Fetch-Site", "none");
                            put("Sec-Fetch-Mode", "navigate");
                            put("Sec-Fetch-User", "?1");
                            put("Sec-Fetch-Dest", "document");
                            put("Accept-Encoding", "gzip, deflate, br");
                            put("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7");
                            put("Cookie", "Idea-1c77831=5ced54c8-cabd-4355-ae5a-97b17f9d7443");
                        }}
                )
        );
    }

    @ParameterizedTest
    @MethodSource
    void getStatusLineAttributes(String headerText, Map<String, String> expectedAttributes) {
        assertThat(Header.of(headerText, "request").getStatusLineAttributes())
                .isEqualTo(expectedAttributes);
    }

    @SuppressWarnings("unused")
    static Stream<Arguments> getStatusLineAttributes() {
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
                        new HashMap() {{
                            put("method", "GET");
                            put("path", "/");
                            put("protocolVersion", "HTTP/1.1");
                        }}
                )
        );
    }

    @ParameterizedTest
    @MethodSource
    void toByte(String headerText, byte[] expectedHeaderByte) {
        byte[] headerByte = Header.of(headerText, "request").toByte();

        assertAll(
                () -> assertThat(headerByte).isEqualTo(expectedHeaderByte),
                () -> assertThat(new String(headerByte)).isEqualTo(new String(expectedHeaderByte))
        );

    }

    @SuppressWarnings("unused")
    static Stream<Arguments> toByte() {
        return Stream.of(
                Arguments.of(
                        "GET / HTTP/1.1" + System.lineSeparator() +
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
                        ("GET / HTTP/1.1" + System.lineSeparator() +
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
                                System.lineSeparator()).getBytes(StandardCharsets.UTF_8)
                )
        );
    }
}

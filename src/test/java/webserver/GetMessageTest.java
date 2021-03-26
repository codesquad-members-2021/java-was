package webserver;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class GetMessageTest {
    @ParameterizedTest
    @MethodSource
    void getHeader(String messageText, RequestHeader expectedRequestHeader) {
        GetMessage getMessage = GetMessage.from(messageText);

        assertThat(getMessage.getHeader()).isEqualTo(expectedRequestHeader);
    }

    static Stream<Arguments> getHeader() {
        return Stream.of(
                //TODO getMessage인데 POST가  성공하는 테스트케이스로 들어가 있음!
                Arguments.of("POST /user/create HTTP/1.1" + System.lineSeparator() +
                                "Host: localhost:8080" + System.lineSeparator() +
                                "Connection: keep-alive" + System.lineSeparator() +
                                "Content-Length: 59" + System.lineSeparator() +
                                "Content-Type: application/x-www-form-urlencoded" + System.lineSeparator() +
                                "Accept: */*" + System.lineSeparator() +
                                "" + System.lineSeparator(),
                        Header.of("POST /user/create HTTP/1.1" + System.lineSeparator() +
                                "Host: localhost:8080" + System.lineSeparator() +
                                "Connection: keep-alive" + System.lineSeparator() +
                                "Content-Length: 59" + System.lineSeparator() +
                                "Content-Type: application/x-www-form-urlencoded" + System.lineSeparator() +
                                "Accept: */*" + System.lineSeparator() +
                                "" + System.lineSeparator(), "request")
                )
        );
    }

    @ParameterizedTest
    @MethodSource
    void getMethod(String messageText, String expectedRequestMethod) {
        GetMessage getMessage = GetMessage.from(messageText);
        assertThat(getMessage.getMethod()).isEqualTo(expectedRequestMethod);
    }

    static Stream<Arguments> getMethod() {
        return Stream.of(
                Arguments.of("GET /user/create HTTP/1.1" + System.lineSeparator() +
                                "Host: localhost:8080" + System.lineSeparator() +
                                "Connection: keep-alive" + System.lineSeparator() +
                                "Content-Length: 59" + System.lineSeparator() +
                                "Content-Type: application/x-www-form-urlencoded" + System.lineSeparator() +
                                "Accept: */*" + System.lineSeparator() +
                                "" + System.lineSeparator(),
                        "GET"
                )
        );
    }
}
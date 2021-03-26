package webserver;

public class PostMessage {
    private RequestHeader header;
    private Body body;

    public PostMessage(RequestHeader header, Body body) {
        this.header = header;
        this.body = body;
    }

    public static PostMessage from(String postMessage) {
        String[] splittedPostMessage = postMessage.split(System.lineSeparator() + System.lineSeparator());
        //TODO body가 비어서 들어오는 경우 실제 form으로 테스트 해서 확인해볼 필요 있음
        return new PostMessage(Header.requestHeaderFrom(splittedPostMessage[0]), Body.from(splittedPostMessage[1]));
    }

    public RequestHeader getHeader() {
        return header;
    }

    public Body getBody() {
        return body;
    }

    public String getMethod() {
        return header.getMethod();
    }
}

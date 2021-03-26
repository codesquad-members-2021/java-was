package webserver;

public class PostMessage {
    private Header header;
    private Body body;

    public PostMessage(Header header, Body body) {
        this.header = header;
        this.body = body;
    }

    public static PostMessage from(String postMessage) {
        String[] splittedPostMessage = postMessage.split(System.lineSeparator() + System.lineSeparator());
        //TODO body가 비어서 들어오는 경우 실제 form으로 테스트 해서 확인해볼 필요 있음
        return new PostMessage(Header.of(splittedPostMessage[0], "request"), Body.from(splittedPostMessage[1]));
    }

    public Header getHeader() {
        return header;
    }

    public Body getBody() {
        return body;
    }

    public String getMethod() {
        return ((RequestHeader) header).getMethod();
    }
}

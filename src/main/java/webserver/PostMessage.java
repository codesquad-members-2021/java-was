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

        return new PostMessage(Header.of(splittedPostMessage[0], "request"), Body.from(splittedPostMessage[1]));
    }

    public Header getHeader() {
        return header;
    }

    public Body getBody() {
        return body;
    }
}

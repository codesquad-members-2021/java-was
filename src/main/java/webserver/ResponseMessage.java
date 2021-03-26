package webserver;

public class ResponseMessage {
    private Header header;
    private Body body;

    public ResponseMessage(Header header, Body body) {
        this.header = header;
        this.body = body;
    }

    public static ResponseMessage from(String responseMessage) {
        String[] splittedResponseMessage = responseMessage.split(System.lineSeparator() + System.lineSeparator());

        return new ResponseMessage(Header.of(splittedResponseMessage[0], "response"), Body.from(splittedResponseMessage[1]));
    }

    public Header getHeader() {
        return header;
    }

    public Body getBody() {
        return body;
    }
}

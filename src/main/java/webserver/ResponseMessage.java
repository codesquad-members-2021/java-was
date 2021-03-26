package webserver;

public class ResponseMessage {
    private ResponseHeader header;
    private Body body;

    public ResponseMessage(ResponseHeader header, Body body) {
        this.header = header;
        this.body = body;
    }

    public static ResponseMessage from(String responseMessage) {
        String[] splittedResponseMessage = responseMessage.split(System.lineSeparator() + System.lineSeparator());

        return new ResponseMessage(Header.responseHeaderFrom(splittedResponseMessage[0]), Body.from(splittedResponseMessage[1]));
    }

    public ResponseHeader getHeader() {
        return header;
    }

    public Body getBody() {
        return body;
    }
}

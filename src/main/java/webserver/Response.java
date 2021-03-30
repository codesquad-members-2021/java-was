package webserver;

import java.io.*;
import java.util.Objects;

public class Response {
    private ResponseMessage responseMessage;

    public Response(ResponseMessage responseMessage) {
        this.responseMessage = responseMessage;
    }

    public static Response from(String message) {
        return new Response(ResponseMessage.from(message));
    }

    public void write(OutputStream outputStream) {
        try (DataOutputStream dos = new DataOutputStream(outputStream)) {
            byte[] header = responseMessage.getHeader().getBytes();
            byte[] body = responseMessage.getBody().getBytes();

            dos.write(header);
            dos.write(body);
            dos.flush();
        } catch (IOException e) {
            throw new IllegalStateException("Response 출력오류", e);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Response response = (Response) o;
        return Objects.equals(responseMessage, response.responseMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(responseMessage);
    }
}

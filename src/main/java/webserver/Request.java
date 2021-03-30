package webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class Request {
    private RequestMessage requestMessage;

    private Request(RequestMessage requestMessage) {
        this.requestMessage = requestMessage;
    }

    public static Request from(InputStream inputStream){

        try(BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))){
            String message = br.lines().collect(Collectors.joining(System.lineSeparator()));

            return new Request(RequestMessage.from(message));
        } catch (IOException e) {
            throw new IllegalArgumentException("inputStream 이상함.",e);
        }
    }

    public RequestMessage getRequestMessage() {
        return requestMessage;
    }
}

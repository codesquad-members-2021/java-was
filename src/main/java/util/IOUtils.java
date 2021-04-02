package util;

import java.io.BufferedReader;
import java.io.IOException;

public class IOUtils    {
    /**
     * @param bufferedReader
     *            Request Body를 시작하는 시점이어야
     * @param contentLength
     *            Request Header의 Content-Length 값이다.
     * @return
     * @throws IOException
     */
    public static String readData(BufferedReader bufferedReader, int contentLength) throws IOException {
        char[] body = new char[contentLength];
        bufferedReader.read(body, 0, contentLength);
        return String.copyValueOf(body);
    }
}

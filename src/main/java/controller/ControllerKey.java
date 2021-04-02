package controller;

import http.HttpMethod;

import java.util.Objects;

public class ControllerKey {

    private HttpMethod method;
    private String url;

    public ControllerKey(HttpMethod method, String url) {
        this.method = method;
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ControllerKey that = (ControllerKey) o;
        return method == that.method && Objects.equals(url, that.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(method, url);
    }
}

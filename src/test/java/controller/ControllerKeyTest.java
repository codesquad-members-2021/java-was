package controller;

import http.HttpMethod;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ControllerKeyTest {

    private SoftAssertions softly;

    @BeforeEach
    public void beforeEach() {
        softly = new SoftAssertions();
    }

    @AfterEach
    public void afterEach() {
        softly.assertAll();
    }

    @Test
    @DisplayName("get /index.html")
    void getIndex() {
        String url = "/index.html";
        ControllerKey key1 = new ControllerKey(HttpMethod.GET, url);
        ControllerKey key2 = new ControllerKey(HttpMethod.GET, url);

        softly.assertThat(key1).isEqualTo(key2);
        softly.assertThat(key1.hashCode()).isEqualTo(key2.hashCode());
    }

    @Test
    @DisplayName("post와 get은 달라야한다.")
    void getAndPost() {
        String url = "/index.html";
        ControllerKey key1 = new ControllerKey(HttpMethod.GET, url);
        ControllerKey key2 = new ControllerKey(HttpMethod.POST, url);

        softly.assertThat(key1).isNotEqualTo(key2);
        softly.assertThat(key1.hashCode()).isNotEqualTo(key2.hashCode());
    }
}
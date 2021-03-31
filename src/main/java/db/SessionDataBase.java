package db;

import com.google.common.collect.Maps;
import model.User;

import java.util.Map;

public class SessionDataBase {
    public static Map<String, User> sessions = Maps.newHashMap();

    public static final String JSESSIONID = "JSESSIONID";

    public static boolean isLoginUser(String sessionId) {
        return getSessionUser(sessionId) != null;
    }

    public static User getSessionUser(String sessionId) {
        return sessions.get(sessionId);
    }
}

package malkawi.logging.web.sessions;

import lombok.Getter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Sessions {

    private final @Getter Map<String, UserSession> users;

    private static Sessions instance = null;

    private Sessions() {
        users = new ConcurrentHashMap<>();
        startCleaner();
    }

    public static Sessions get() {
        if(instance == null)
            instance = new Sessions();
        return instance;
    }

    public UserSession user(String sessionId) {
        return users.get(sessionId);
    }

    public void add(UserSession session) {
        users.put(session.getSessionId(), session);
    }

    public void deleteSession(String sessionId) {
        UserSession session = user(sessionId);
        if(session != null) {
            session.close();
            users.remove(sessionId);
        }
    }

    private void startCleaner() {
        Thread cleanerThread = new Thread(new SessionCleaner(this));
        cleanerThread.setDaemon(true);
    }

}

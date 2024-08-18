package malkawi.logging.web.controllers.services;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import malkawi.logging.config.BootstrapConfig;
import malkawi.logging.database.data.users.User;
import malkawi.logging.net.client.impl.database.data.ServerInfo;
import malkawi.logging.web.data.AccessDetailsStore;
import malkawi.logging.web.sessions.Sessions;
import malkawi.logging.web.sessions.UserSession;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AccessDetailsStore accessDetailsStore = new AccessDetailsStore();

    private final @NonNull BootstrapConfig bootstrapConfig;

    public UserSession authenticate(String username, String password, String sessionId) {
        return authenticate(new User(-1, username, password, null), sessionId);
    }

    public UserSession authenticate(User user, String sessionId) {
        UserSession session = getOrCreateSession(user, sessionId);
        if(session == null)
            return null;
        if(session.isAuthenticated())
            return session;
        if(session.startSession()) {
            Sessions.get().add(session);
            return session;
        }
        return null;
    }

    public UserSession getOrCreateSession(User user, String sessionId) {
        UserSession session = Sessions.get().getUsers().getOrDefault(sessionId,
                populateSession(new UserSession(user, sessionId)));
        if(session.getServerInfo() != null) {
            accessDetailsStore.storeAccessPair(session.getUser(), session.getServerInfo());
            return session;
        }
        return null;
    }

    private UserSession populateSession(UserSession session) {
        Map.Entry<User, ServerInfo> previousEntry = accessDetailsStore.getAccessPair(session.getUser());
        if(previousEntry != null) {
            session.setUser(previousEntry.getKey());
            session.setServerInfo(previousEntry.getValue());
            return session;
        }
        session.initSession(new ServerInfo(bootstrapConfig.getIp(), bootstrapConfig.getPort()));
        return session;
    }

    public void deleteSession(String sessionId) {
        Sessions.get().deleteSession(sessionId);
    }

    public boolean isAuthenticated(String sessionId) {
        UserSession session = Sessions.get().user(sessionId);
        return session != null && session.isAuthenticated();
    }

}

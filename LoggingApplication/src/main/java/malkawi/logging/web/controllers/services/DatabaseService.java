package malkawi.logging.web.controllers.services;

import malkawi.logging.database.entities.LogInfo;
import malkawi.logging.web.sessions.Sessions;
import malkawi.logging.web.sessions.UserSession;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class DatabaseService {

    public List<LogInfo> fetchAllLogs(String sessionId) {
        UserSession userSession = Sessions.get().user(sessionId);
        return userSession != null ? userSession.getDatabaseDAO().getAll(userSession.getUser().getUsername())
                : Collections.emptyList();
    }

    public LogInfo fetchLog(int documentId, String sessionId) {
        UserSession userSession = Sessions.get().user(sessionId);
        return userSession != null ?
                userSession.getDatabaseDAO().get(documentId, userSession.getUser().getUsername()) : null;
    }

    public boolean appendLog(LogInfo log, String sessionId) {
        UserSession userSession = Sessions.get().user(sessionId);
        if(userSession != null) {
            if(log.getId() < 0)
                return userSession.getDatabaseDAO().create(userSession.getUser().getUsername(), log) != null;
            else
                return userSession.getDatabaseDAO().update(log.getId(), userSession.getUser().getUsername(),
                        "message", log.getMessage(), "date", log.getDate());
        }
        return false;
    }

    public boolean deleteLog(int logID, String sessionId) {
        UserSession userSession = Sessions.get().user(sessionId);
        return userSession != null && userSession.getDatabaseDAO().delete(userSession.getUser().getUsername(), logID);
    }

}

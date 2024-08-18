package malkawi.logging.web.sessions;

import lombok.Getter;
import lombok.Setter;
import malkawi.logging.database.DatabaseSession;
import malkawi.logging.database.data.users.User;
import malkawi.logging.database.entities.LogInfo;
import malkawi.logging.database.repositories.DatabaseDAO;
import malkawi.logging.database.repositories.impl.LogInfoDAOImpl;
import malkawi.logging.net.client.impl.database.data.ServerInfo;
import malkawi.logging.service.BootstrapService;
import malkawi.logging.utilities.io.console.Console;

public class UserSession implements AutoCloseable {

    private @Getter @Setter User user;

    private final @Getter String sessionId;

    private @Getter @Setter ServerInfo serverInfo;

    private DatabaseSession databaseSession;

    private @Getter DatabaseDAO<LogInfo> databaseDAO;

    private @Getter @Setter long lastAccessTime = System.currentTimeMillis();

    public UserSession(User user, String sessionId) {
        this.user = user;
        this.sessionId = sessionId;
    }

    public boolean startSession() {
        Console.info("[UserSession] Starting Database Session");
        this.databaseSession = new DatabaseSession(serverInfo, user, "logs").connect();
        this.databaseDAO = new LogInfoDAOImpl(databaseSession);
        if(isAuthenticated()) {
            Console.success("[UserSession] Authenticated Database Session");
            initCollection();
            return true;
        }
        return false;
    }

    public UserSession initSession(ServerInfo bootstrapServerInfo) {
        BootstrapService service = new BootstrapService(user, bootstrapServerInfo);
        if(service.requestDetails()) {
            user = service.getUser();
            serverInfo = service.getDesignatedServerInfo();
        }
        return this;
    }

    private void initCollection() {
        databaseDAO.createCollectionIfNotExist(user.getUsername());
    }

    public boolean isAuthenticated() {
        return databaseSession != null && databaseSession.isConnected() && databaseSession.isDatabaseLogged();
    }

    @Override
    public void close() {
        if(databaseSession != null)
            databaseSession.close();
    }

}

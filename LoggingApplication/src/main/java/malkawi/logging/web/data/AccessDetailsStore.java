package malkawi.logging.web.data;

import malkawi.logging.database.data.users.User;
import malkawi.logging.net.client.impl.database.data.ServerInfo;

import java.util.HashMap;
import java.util.Map;

public class AccessDetailsStore {

    private final Map<User, ServerInfo> accessDetails = new HashMap<>();

    public Map.Entry<User, ServerInfo> getAccessPair(User user) {
        if(user == null)
            return null;
        return accessDetails.entrySet().stream().filter(e -> {
            User key = e.getKey();
            return key != null &&
                    key.getUsername().equals(user.getUsername()) && key.getPassword().equals(user.getPassword());
        }).findFirst().orElse(null);
    }

    public void storeAccessPair(User user, ServerInfo serverInfo) {
        accessDetails.put(user, serverInfo);
    }

}

package malkawi.logging.service;

import lombok.Getter;
import lombok.Setter;
import malkawi.logging.database.data.users.User;
import malkawi.logging.net.client.impl.bootstrap.BootstrapClient;
import malkawi.logging.net.client.impl.database.data.ServerInfo;
import malkawi.logging.net.global.data.PacketBuilder;
import malkawi.logging.utilities.Sleep;

@Getter
public class BootstrapService {

    private @Setter User user;

    private @Setter ServerInfo designatedServerInfo;

    private final ServerInfo bootstrapServerInfo;

    public BootstrapService(User user, ServerInfo bootstrapServerInfo) {
        this.user = user;
        this.bootstrapServerInfo = bootstrapServerInfo;
    }

    public boolean requestDetails() {
        BootstrapClient bootstrapClient = new BootstrapClient(this);
        bootstrapClient.start();
        bootstrapClient.getConnection().send(new PacketBuilder().typeName("SYN").build());
        return Sleep.sleepUntil(() -> designatedServerInfo != null, 20000, 50);
    }

}

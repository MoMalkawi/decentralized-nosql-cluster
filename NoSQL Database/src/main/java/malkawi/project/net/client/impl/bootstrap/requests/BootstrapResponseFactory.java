package malkawi.project.net.client.impl.bootstrap.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import malkawi.project.net.client.impl.bootstrap.requests.impl.ReceiveSystemSetup;
import malkawi.project.net.global.connections.AbstractConnection;
import malkawi.project.net.global.data.Packet;
import malkawi.project.net.global.requests.AbstractResponse;
import malkawi.project.net.global.requests.impl.Terminate;
import malkawi.project.net.client.impl.database.requests.impl.*;

import java.util.function.BiFunction;

@AllArgsConstructor
@SuppressWarnings("unused")
public enum BootstrapResponseFactory {

    SYN_ACK(SendCredentials::new),
    SYSTEM_SETUP(ReceiveSystemSetup::new),
    TERMINATE(Terminate::new);

    private final @Getter BiFunction<AbstractConnection, Packet, AbstractResponse> responseInstance;

    public static AbstractResponse getResponse(AbstractConnection abstractConnection, Packet packet) {
        BootstrapResponseFactory factory = getFactoryByName(packet.getTypeName().toUpperCase());
        return factory != null ? factory.getResponseInstance().apply(abstractConnection, packet) : null;
    }

    public static BootstrapResponseFactory getFactoryByName(String name) {
        try {
            return valueOf(name);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

}

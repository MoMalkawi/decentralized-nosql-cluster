package malkawi.logging.net.client.impl.bootstrap.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import malkawi.logging.net.client.impl.bootstrap.requests.impl.RegisterNewCredentials;
import malkawi.logging.net.client.impl.bootstrap.requests.impl.RequestCredentials;
import malkawi.logging.net.client.impl.database.requests.impl.RegisterAPIResponse;
import malkawi.logging.net.client.impl.database.requests.impl.RegisterDatabase;
import malkawi.logging.net.client.impl.database.requests.impl.SendCredentials;
import malkawi.logging.net.client.impl.database.requests.impl.SetAuthorized;
import malkawi.logging.net.global.connections.AbstractConnection;
import malkawi.logging.net.global.data.Packet;
import malkawi.logging.net.global.requests.AbstractResponse;
import malkawi.logging.net.global.requests.impl.Terminate;

import java.util.function.BiFunction;

@AllArgsConstructor
@SuppressWarnings("unused")
public enum BootstrapResponseFactory {

    SYN_ACK(RequestCredentials::new),
    NEW_CREDENTIALS(RegisterNewCredentials::new),
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

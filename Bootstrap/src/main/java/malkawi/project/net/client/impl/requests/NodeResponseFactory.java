package malkawi.project.net.client.impl.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import malkawi.project.net.global.connections.AbstractConnection;
import malkawi.project.net.global.data.Packet;
import malkawi.project.net.global.requests.AbstractResponse;
import malkawi.project.net.global.requests.impl.Terminate;
import malkawi.project.net.client.impl.requests.impl.*;

import java.util.function.BiFunction;

@AllArgsConstructor
@SuppressWarnings("unused")
public enum NodeResponseFactory {

    SYN_ACK(SendCredentials::new),
    AUTH_SUCCESS(SetAuthorized::new),
    TERMINATE(Terminate::new);

    private final @Getter BiFunction<AbstractConnection, Packet, AbstractResponse> responseInstance;

    public static AbstractResponse getResponse(AbstractConnection abstractConnection, Packet packet) {
        NodeResponseFactory factory = getFactoryByName(packet.getTypeName().toUpperCase());
        return factory != null ? factory.getResponseInstance().apply(abstractConnection, packet) : null;
    }

    public static NodeResponseFactory getFactoryByName(String name) {
        try {
            return valueOf(name);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

}

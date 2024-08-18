package malkawi.project.net.client.impl.database.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import malkawi.project.net.client.impl.database.requests.impl.RegisterAPIResponse;
import malkawi.project.net.client.impl.database.requests.impl.RegisterDatabase;
import malkawi.project.net.client.impl.database.requests.impl.SendCredentials;
import malkawi.project.net.client.impl.database.requests.impl.SetAuthorized;
import malkawi.project.net.global.connections.AbstractConnection;
import malkawi.project.net.global.data.Packet;
import malkawi.project.net.global.requests.AbstractResponse;
import malkawi.project.net.global.requests.impl.Terminate;

import java.util.function.BiFunction;

@AllArgsConstructor
@SuppressWarnings("unused")
public enum DBResponseFactory {

    SYN_ACK(SendCredentials::new),
    AUTH_SUCCESS(SetAuthorized::new),
    API_RESPONSE(RegisterAPIResponse::new),
    LOGGED_DATABASE(RegisterDatabase::new),
    TERMINATE(Terminate::new);

    private final @Getter BiFunction<AbstractConnection, Packet, AbstractResponse> responseInstance;

    public static AbstractResponse getResponse(AbstractConnection abstractConnection, Packet packet) {
        DBResponseFactory factory = getFactoryByName(packet.getTypeName().toUpperCase());
        return factory != null ? factory.getResponseInstance().apply(abstractConnection, packet) : null;
    }

    public static DBResponseFactory getFactoryByName(String name) {
        try {
            return valueOf(name);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

}

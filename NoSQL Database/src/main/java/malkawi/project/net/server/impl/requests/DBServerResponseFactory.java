package malkawi.project.net.server.impl.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import malkawi.project.net.global.connections.AbstractConnection;
import malkawi.project.net.global.data.Packet;
import malkawi.project.net.global.requests.AbstractResponse;
import malkawi.project.net.server.impl.requests.impl.*;
import malkawi.project.net.global.requests.impl.*;

import java.util.function.BiFunction;

@AllArgsConstructor
@SuppressWarnings("unused")
public enum DBServerResponseFactory {

    SYN(AcknowledgeSYN::new),
    ACK(AuthorizeACK::new),
    PING(PingResponse::new),
    API(OperateDatabase::new),
    USE_DATABASE(UseDatabase::new),
    ADD_USER(AddRemoteUser::new),
    TERMINATE(Terminate::new);

    private final @Getter BiFunction<AbstractConnection, Packet, AbstractResponse> responseInstance;

    public static AbstractResponse getResponse(AbstractConnection abstractConnection, Packet packet) {
        DBServerResponseFactory factory = getFactoryByName(packet.getTypeName().toUpperCase());
        return factory != null ? factory.getResponseInstance().apply(abstractConnection, packet) : null;
    }

    public static DBServerResponseFactory getFactoryByName(String name) {
        try {
            return valueOf(name);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

}


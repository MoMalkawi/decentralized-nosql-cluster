package malkawi.logging.net.global.requests;

import lombok.AllArgsConstructor;
import malkawi.logging.net.global.connections.AbstractConnection;
import malkawi.logging.net.global.data.Packet;

@AllArgsConstructor
public abstract class AbstractResponse {

    protected final AbstractConnection connection;

    protected final Packet requestPacket;

    public abstract void respond();

    public abstract boolean validate();

    protected boolean checkContentLength(int length, boolean equal) {
        if(length <= 0)
            return requestPacket.getContent() == null || requestPacket.getContent().length == 0;
        return requestPacket.getContent() != null &&
                (equal ? requestPacket.getContent().length == length :
                        requestPacket.getContent().length >= length);
    }

}

package malkawi.logging.net.global.connections;

import lombok.Getter;
import lombok.Setter;
import malkawi.logging.net.global.data.Packet;
import malkawi.logging.net.global.data.PacketBuilder;
import malkawi.logging.net.global.security.AESEncryption;
import malkawi.logging.utilities.Sleep;
import malkawi.logging.utilities.io.JSONUtilities;
import malkawi.logging.utilities.io.console.Console;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

public abstract class AbstractConnection {

    protected final Socket socket;

    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    @Getter @Setter
    private long lastReceivedPacket;

    protected AbstractConnection(Socket socket) {
        this.socket = socket;
        this.lastReceivedPacket = System.currentTimeMillis();
    }

    public void send(Packet packet) {
        try {
            String encryptedJSONPacket = AESEncryption.encrypt(JSONUtilities.convertToJson(packet));
            if(encryptedJSONPacket != null)
                getObjectOutputStream().writeObject(encryptedJSONPacket);
        } catch (IOException e) {
            Console.error(
                    "[Connection] Error writing packet [" + packet.getTypeName() + "].\n" + e.getMessage());
        }
    }

    private ObjectOutputStream getObjectOutputStream() throws IOException {
        if(objectOutputStream == null)
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        return objectOutputStream;
    }

    public Packet receivePacket() {
        if(isAlive()) {
            try {
                return extractPacket(getObjectInputStream().readObject());
            } catch (EOFException | SocketException e) {
                Console.info("[Connection] closed connection.");
                close();
            } catch (IOException | ClassNotFoundException e) {
                Console.error(
                        "[Connection] problem reading packet at AbstractConnection:receivePacket.\n");
            }
        }
        return null;
    }

    private Packet extractPacket(Object rawPacket) {
        if(rawPacket != null) {
            String json = AESEncryption.decrypt((String) rawPacket);
            if(json != null)
                return JSONUtilities.jsonToObject(json, Packet.class);
        }
        return null;
    }

    public ObjectInputStream getObjectInputStream() throws IOException {
        if(objectInputStream == null)
            objectInputStream = new ObjectInputStream(socket.getInputStream());
        return objectInputStream;
    }

    public boolean isAlive() {
        return socket != null && !socket.isClosed();
    }

    public void terminate() {
        if(isAlive()) {
            send(new PacketBuilder().typeName("TERMINATE").values("Reciprocal termination request").build());
            close();
            Console.info("[Connection] Connection terminated at AbstractConnection:terminate.");
        }
    }

    private void close() {
        if(socket.isClosed())
            return;
        try {
            socket.close();
            Sleep.sleepUntil(socket::isClosed, 1000, 10);
        } catch (IOException e) {
            Console.error(
                    "[Connection] Can't close client connection at AbstractConnection:close.\n"
                            + e.getMessage());
        }
    }

}


package malkawi.project.net.global.connections;

import lombok.Getter;
import lombok.Setter;
import malkawi.project.net.global.data.Packet;
import malkawi.project.net.global.data.PacketBuilder;
import malkawi.project.net.global.security.AESEncryption;
import malkawi.project.utilities.Sleep;
import malkawi.project.utilities.io.JSONUtilities;
import malkawi.project.utilities.io.console.Console;

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
    private long latestActivityTimeMillis;

    protected AbstractConnection(Socket socket) {
        this.socket = socket;
        updateActivityTime();
    }

    public void send(Packet packet) {
        if(!isAlive())
            return;
        updateActivityTime();
        try {
            String encryptedJSONPacket = AESEncryption.encrypt(JSONUtilities.convertToJson(packet));
            if(encryptedJSONPacket != null)
                getObjectOutputStream().writeObject(encryptedJSONPacket);
        } catch (IOException e) {
            Console.error(
                    "[ERROR] Error writing packet [" + packet.getTypeName() + "].\n" + e.getMessage());
        }
    }

    private ObjectOutputStream getObjectOutputStream() throws IOException {
        if(objectOutputStream == null)
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        return objectOutputStream;
    }

    public Packet receivePacket() {
        if(isAlive()) {
            updateActivityTime();
            try {
                return extractPacket(getObjectInputStream().readObject());
            } catch (EOFException | SocketException e) {
                Console.info("[Connection] connection closed.");
                close();
            } catch (IOException | ClassNotFoundException e) {
                Console.error(
                        "[Error] problem reading packet at AbstractConnection:receivePacket.\n");
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
            Console.info("[Termination] a client connection has been terminated at AbstractConnection:terminate.");
        }
    }

    private void close() {
        try {
            socket.close();
            Sleep.sleepUntil(socket::isClosed, 1000, 10);
        } catch (IOException e) {
            Console.error(
                    "[ERROR] Can't close client connection at AbstractConnection:close.\n"
                            + e.getMessage());
        }
    }

    private void updateActivityTime() {
        this.latestActivityTimeMillis = System.currentTimeMillis();
    }

}


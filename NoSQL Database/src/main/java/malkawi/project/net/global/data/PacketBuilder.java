package malkawi.project.net.global.data;

import java.util.ArrayList;
import java.util.List;

public class PacketBuilder {

    private String typeName;

    private final List<Object> content = new ArrayList<>();

    private final List<String> contentTypes = new ArrayList<>();

    private Long identifier = 0L;

    public PacketBuilder typeName(String typeName) {
        this.typeName = typeName;
        return this;
    }

    public PacketBuilder values(Object... values) {
        for(Object value : values) {
            content.add(value);
            String qualifiedName = value.getClass().getName();
            contentTypes.add(qualifiedName.startsWith("malkawi") ?
                    value.getClass().getSimpleName() : qualifiedName);
        }
        return this;
    }

    public PacketBuilder identifier(Long identifier) {
        this.identifier = identifier;
        return this;
    }

    public PacketBuilder generateIdentifier() {
        this.identifier = IdentifierGenerator.generate();
        return this;
    }

    public Packet build() {
        Packet packet = new Packet(typeName,
                content.toArray(new Object[0]), contentTypes.toArray(new String[0]));
        packet.setIdentifier(identifier);
        return packet;
    }

}

package malkawi.logging.net.global.data;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class Packet {

    private Object[] content;

    private String[] contentTypes;

    private String typeName;

    private @Setter Long identifier = 0L;

    Packet(String typeName, Object[] content, String[] contentTypes) {
        this.typeName = typeName;
        this.content = content;
        this.contentTypes = contentTypes;
    }



}

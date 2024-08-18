package malkawi.project.database.components.collections.documents;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@Getter
public class Document implements Serializable {

    private @Setter int id;

    private final Map<Integer, Object> values;

    private @Setter int collectionId;

    private transient @Getter @Setter boolean changed;

    public Document(int id) {
        this.id = id;
        this.values = new HashMap<>();
    }

    public Document() {
        this(-1);
    }

    public void addValue(int index, Object value) {
        values.put(index, value);
    }

    public Object removeValue(int index) {
        return values.containsKey(index) ? values.remove(index) : null;
    }

}

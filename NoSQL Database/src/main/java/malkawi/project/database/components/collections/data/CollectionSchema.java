package malkawi.project.database.components.collections.data;

import lombok.Getter;
import lombok.Setter;
import malkawi.project.database.components.collections.data.properties.Property;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Getter
public class CollectionSchema implements Serializable {

    private final Map<Integer, Property> properties;

    private volatile @Setter int lastIndex = -1;

    private transient @Getter @Setter boolean changed;

    public CollectionSchema() {
        this.properties = new LinkedHashMap<>();
    }

    public Object[] getRawSchemaProperties() {
        List<Object> propertiesRawList = new ArrayList<>();
        properties.values().forEach(p -> {
            propertiesRawList.add(p.getType().getName());
            propertiesRawList.add(p.getName());
        });
        return propertiesRawList.toArray(new Object[0]);
    }

}

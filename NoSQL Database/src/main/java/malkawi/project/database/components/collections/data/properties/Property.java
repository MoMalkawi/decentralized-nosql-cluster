package malkawi.project.database.components.collections.data.properties;

import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
public class Property implements Serializable {

    private @Setter int index;

    private final @NonNull Class<?> type;

    private @NonNull @Setter String name;

}

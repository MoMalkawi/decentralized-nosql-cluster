package malkawi.logging.database.data.users;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@EqualsAndHashCode
public class User {

    private @Getter int id;

    private final @Getter String username;

    private final @Getter String password;

    private @Setter @Getter Role role;

    public enum Role {

        ROOT(-1), USER(0);

        private final @Getter int id;

        Role(int id) {
            this.id = id;
        }

    }

}

package malkawi.project.data.profiles;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@AllArgsConstructor
public class User {

    private @Getter int id;

    private final @Getter String username;

    private final String password;

    private @Getter @Setter Role role;

    @AllArgsConstructor
    public enum Role {
        ROOT(-1), USER(0);

        private final @Getter int id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return username.equals(user.username) && password.equals(user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password);
    }

}

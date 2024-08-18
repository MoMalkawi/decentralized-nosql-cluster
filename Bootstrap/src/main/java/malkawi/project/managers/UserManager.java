package malkawi.project.managers;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import malkawi.project.BootstrapInstance;
import malkawi.project.data.profiles.User;
import malkawi.project.utilities.io.JSONUtilities;

import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
@Getter
public class UserManager {

    private @Setter Set<User> users = new HashSet<>();

    private @Setter int largestUserId;

    private final @NonNull BootstrapInstance instance;

    public User generateUser() {
        int index = generateNewIndex();
        User user = new User(index, "user" + index, "password", User.Role.USER);
        users.add(user);
        JSONUtilities.overwriteJSONFile(users, instance.getBaseFile("users"));
        return user;
    }

    private int generateNewIndex() {
        return ++largestUserId;
    }

}

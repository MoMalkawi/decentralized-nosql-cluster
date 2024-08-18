package malkawi.project.io;

import com.google.gson.reflect.TypeToken;
import lombok.AllArgsConstructor;
import malkawi.project.data.profiles.User;
import malkawi.project.BootstrapInstance;
import malkawi.project.utilities.io.JSONUtilities;

import java.io.File;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
public class BaseLoader {

    private final BootstrapInstance session;

    private static final Type userType = new TypeToken<HashSet<User>>() {}.getType();

    public void load() {
        loadUsers();
    }

    private void loadUsers() {
        File usersFile = session.getBaseFile("users");
        if(usersFile.exists() && usersFile.isFile()) {
            Set<User> users = JSONUtilities.readJSONFile(usersFile.getPath(), userType);
            session.getUserManager().setUsers(users != null ? users : new HashSet<>());
            for(User user : session.getUserManager().getUsers())
                session.getUserManager().setLargestUserId(Math.max(user.getId(),
                        session.getUserManager().getLargestUserId()));
        }
    }

}

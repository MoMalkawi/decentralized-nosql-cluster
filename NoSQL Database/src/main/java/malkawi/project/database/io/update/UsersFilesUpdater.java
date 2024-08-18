package malkawi.project.database.io.update;

import com.google.gson.reflect.TypeToken;
import malkawi.project.data.Config;
import malkawi.project.database.components.users.User;
import malkawi.project.utilities.Utils;
import malkawi.project.utilities.io.JSONUtilities;

import java.io.File;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class UsersFilesUpdater {

    public void writeUsers(Map<Integer, User> users) {
        JSONUtilities.overwriteJSONFile(users, getUsersFile());
    }

    public Map<Integer, User> getStoredUsers() {
        File usersFile;
        Type type = new TypeToken<HashMap<Integer, User>>() {}.getType();
        return (usersFile = getUsersFile()).exists() ?
                JSONUtilities.readJSONFile(usersFile.getPath(), type)
                :
                new HashMap<>();
    }

    private File getUsersFile() {
        return new File(Utils.buildPath(Config.get().getDatabasesRootPath(), "users"));
    }

}

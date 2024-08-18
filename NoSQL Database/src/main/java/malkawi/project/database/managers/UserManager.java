package malkawi.project.database.managers;

import lombok.Getter;
import malkawi.project.database.io.update.UsersFilesUpdater;
import malkawi.project.database.components.users.User;
import malkawi.project.net.cluster.Cluster;
import malkawi.project.utilities.interfaces.Filter;
import malkawi.project.utilities.io.console.Console;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UserManager {

    private final @Getter Map<Integer, User> users;

    private final UsersFilesUpdater updater;

    private final User rootUser = new User(-1, "root", "root", User.Role.ROOT);

    public UserManager() {
        updater = new UsersFilesUpdater();
        users = updater.getStoredUsers();
    }

    public void addUser(User user) {
        if(user == null)
            return;
        users.put(user.getId(), user);
        updater.writeUsers(users);
        Console.info("[UserManager] Added and wrote new user: (" + user.getUsername() + ")");
        Cluster.get().getCurrentNodeData().updateTime().updateDataFile();
    }

    public void addUsers(User[] usersToAdd) {
        List<User> filteredUsersToAdd = Arrays.stream(usersToAdd).filter(u -> {
            User user = users.get(u.getId());
            return user == null || !user.equals(u);
        }).collect(Collectors.toList());
        filteredUsersToAdd.forEach(u -> {
            users.put(u.getId(), u);
            Console.info("[UserManager] Added user: " + u.getUsername());
        });
        updater.writeUsers(users);
        Console.info("[UserManager] wrote (" + filteredUsersToAdd.size() + ") users.");
    }

    public User getUser(int index) {
        return users.get(index);
    }

    public User getUser(Filter<User> userFilter) {
        return users.values()
                .stream()
                .filter(userFilter::verify)
                .findFirst()
                .orElse(null);
    }

    public List<User> getUsers(Filter<User> userFilter) {
        return users.values()
                .stream()
                .filter(userFilter::verify)
                .collect(Collectors.toList());
    }

    public boolean verifyRole(User user, User.Role roleRequired) {
        if(isRoot(user))
            return true;
        User fetchedUser = getUser(user.getId());
        if(fetchedUser == null)
            return false;
        return fetchedUser.equals(user) && fetchedUser.getRole().equals(roleRequired);
    }

    public boolean verifyUser(User user) {
        if(isRoot(user)) {
            user.setRole(User.Role.ROOT);
            return true;
        }
        User fetchedUser = getUser(user.getId());
        if(fetchedUser == null)
            return false;
        return fetchedUser.equals(user);
    }

    public boolean isRoot(User user) {
        return user.equals(rootUser);
    }

}

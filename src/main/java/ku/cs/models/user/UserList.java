package ku.cs.models.user;

import ku.cs.services.UserListFileDatasource;

import java.util.ArrayList;
import java.util.List;

public class UserList {

    private List<User> users;

    public UserList() {
        this.users = new ArrayList<>();
    }

    public void addUser(User user) {
        users.add(user);
    }

    public List<User> getUsers() {
        return users;
    }

    public User findUserByUsername(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }


    public void writeData(UserListFileDatasource datasource) {
        datasource.writeData(this);
    }

    public boolean loadData(UserListFileDatasource datasource) {
        UserList loadedData = datasource.readData();
        if (loadedData != null) {
            this.users = loadedData.getUsers();
            return true;
        }
        return false;
    }


}

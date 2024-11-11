package ku.cs.models.user;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FacultyUserList {
    private List<FacultyUser> facultyUsers;

    public FacultyUserList() {
        this.facultyUsers = new ArrayList<>();
    }

    public void addFacultyUser(FacultyUser facultyUser) {
        this.facultyUsers.add(facultyUser);
    }

    public List<FacultyUser> getFacultyUsers() {
        return facultyUsers;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (FacultyUser facultyUser : facultyUsers) {
            sb.append(facultyUser.getFacultyId()).append(" - ").append(facultyUser.getFacultyName()).append("\n");
        }
        return sb.toString();
    }
}

package ku.cs.models.user;

import java.util.ArrayList;
import java.util.List;

public class MajorUserList {
    private List<MajorUser> majorUsers;

    public MajorUserList() {
        this.majorUsers = new ArrayList<>();
    }

    public void addMajorUser(MajorUser majorUser) {
        this.majorUsers.add(majorUser);
    }

    public List<MajorUser> getMajorUsers() {
        return majorUsers;
    }

    public void clearMajorUsers() {
        this.majorUsers.clear();
    }

    public MajorUser findMajorUserByMajorId(String majorId) {
        for (MajorUser majorUser : majorUsers) {
            if (majorUser.getMajorId().equals(majorId)) {
                return majorUser;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (MajorUser majorUser : majorUsers) {
            sb.append(majorUser.getMajorId()).append(" - ")
                    .append(majorUser.getMajorName()).append(" - ")
                    .append(majorUser.getFacultyName()).append("\n");
        }
        return sb.toString();
    }
}

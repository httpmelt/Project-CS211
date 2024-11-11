package ku.cs.models.user;

import java.time.LocalDateTime;

public class Staff extends User {
    private String teacherId;
    private String majorId;


    public Staff(String defaultProfilePic, String role, String name, String surname, String username, String password, String facultyId, String majorId, String teacherId, boolean register, LocalDateTime time) {
        super(defaultProfilePic, role, name, surname, username, password, facultyId, register,time);
        this.teacherId = teacherId;
        this.majorId = majorId;
    }

    public Staff(String username, String name, String role, String facultyId) {
        super(username, name, role, facultyId);
    }

    public Staff(String username, String name, String role, String facultyId, String profilePic) {
        super(username, name, role, facultyId,profilePic);
    }


    public String getTeacherID() {
        return teacherId;
    }

    public void setTeacherID(String teacherID) {
        this.teacherId = teacherID;
    }

    public String getMajorID() {
        return majorId;
    }

    public void setMajorID(String majorID) {
        this.majorId = majorID;
    }

    @Override
    public String toString() {
        return "Staff{" +
                "teacherID='" + teacherId + '\'' +
                ", majorID='" + majorId + '\'' +
                ", defaultProfilePic='" + defaultProfilePic + '\'' +
                ", role='" + role + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", facultyId='" + facultyId + '\'' +
                ", register=" + register +
                '}';
    }
}

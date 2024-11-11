package ku.cs.models.teacher;

import ku.cs.models.user.User;

public class Teacher extends User {
/*
    extend จาก class User ละเพิ่ม field
    1. majorId
    2. teacherId
 */
    private String majorId;
    private String teacherId;

    public Teacher(String defaultProfilePic, String role, String name, String surname, String username, String password, String facultyId, String majorId, String teacherId) {
        super(defaultProfilePic,role, name, surname, username, password, facultyId);
        this.majorId = majorId;
        this.teacherId = teacherId;
    }

    public String getMajorId() {
        return majorId;
    }

    public void setMajorId(String majorId) {
        this.majorId = majorId;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "majorId='" + majorId + '\'' +
                ", teacherId='" + teacherId + '\'' +
                ", defaultProfilePic='" + defaultProfilePic + '\'' +
                ", role='" + role + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", facultyId='" + facultyId + '\'' +
                '}';
    }
}

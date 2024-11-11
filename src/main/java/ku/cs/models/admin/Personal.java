package ku.cs.models.admin;

import ku.cs.models.user.User;

import java.time.LocalDateTime;

public class Personal extends User {

    private String majorId;
    private String teacherId;


    public Personal(String defaultProfilePic, String role, String name, String surname, String username,
                    String password, String facultyId, String majorId, String teacherId, boolean register, LocalDateTime dateTime){
        super(defaultProfilePic, role, name, surname, username,
                password, facultyId, majorId, teacherId, register, dateTime);
        this.majorId = majorId;
        this.teacherId = teacherId;

    }



    public boolean isUsername(String username){
       return getUsername().equals(username);
    }

    public String getMajorId() {
        return majorId;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setMajorId(String majorId) {
        this.majorId = majorId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }


}

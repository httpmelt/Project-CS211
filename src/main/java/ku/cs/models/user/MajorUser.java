package ku.cs.models.user;


public class MajorUser extends User {
    private String majorId;
    private String majorName;
    private String facultyName;

    public MajorUser(String defaultProfilePic, String role, String name, String surname, String username, String password, String majorId, String majorName, String facultyName) {
        super(defaultProfilePic,role, name, surname, username, password);
        this.majorId = majorId;
        this.majorName = majorName;
        this.facultyName = facultyName;
    }

    public String getFacultyName() {
        return facultyName;
    }

    public void setFacultyName(String facultyName) {
        this.facultyName = facultyName;
    }

    public String getMajorName() {
        return majorName;
    }

    public void setMajorName(String department) {
        this.majorName = majorName;
    }


    public String getMajorId() {
        return majorId;
    }

    public void setMajorId(String majorId) {
        this.majorId = majorId;
    }
}

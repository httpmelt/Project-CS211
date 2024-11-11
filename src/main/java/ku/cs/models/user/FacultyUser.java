package ku.cs.models.user;

public class FacultyUser extends User {
    private String facultyName;

    public FacultyUser(String defaultProfilePic, String role, String name, String surname, String username, String password, String facultyId, String facultyName) {
        super(defaultProfilePic,role, name, surname, username, password, facultyId);
        this.facultyName = facultyName;
    }


    public String getFacultyId() {
        return facultyId;
    }

    public String getFacultyName() {
        return facultyName;
    }


}

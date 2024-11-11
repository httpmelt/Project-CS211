package ku.cs.models.admin;

public class Section {
    private String facultyName;
    private String facultyId;


    public Section(String facultyId, String facultyName) {
        this.facultyName = facultyName;
        this.facultyId = facultyId;
    }

    public boolean isFaculty(String facultyName) {
        return this.facultyName.equals(facultyName);
    }

    public String getFacultyName() {
        return facultyName;
    }

    public String getFacultyId() {
        return facultyId;
    }

    public void setFacultyName(String facultyName) {
        this.facultyName = facultyName;
    }

    public void setFacultyId(String facultyId) {
        this.facultyId = facultyId;
    }



}

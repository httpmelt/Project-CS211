package ku.cs.models.admin;

public class SectionTwo {
    private String majorName;
    private String facultyName;
    private String majorId;

    public SectionTwo(String majorId, String majorName, String facultyName) {
        this.majorName = majorName;
        this.facultyName = facultyName;
        this.majorId = majorId;
    }

    public String getMajor() {
        return majorName;
    }

    public boolean isMajor(String major) {
        return this.majorName.equals(major);
    }

    public String getFaculty() {
        return facultyName;
    }

    public String getMajorId() {
        return majorId;
    }

    public void setMajor(String majorName) {
        this.majorName = majorName;
    }

    public void setFaculty(String facultyName) {
        this.facultyName = facultyName;
    }

    public void setMajorId(String majorId) {
        this.majorId = majorId;
    }


}

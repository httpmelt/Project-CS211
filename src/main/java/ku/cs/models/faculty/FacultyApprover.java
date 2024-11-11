package ku.cs.models.faculty;

import javafx.beans.value.ObservableValue;

public class FacultyApprover {
    private String name;
    private String role;
    private String facultyName;

    public FacultyApprover(String name, String role, String facultyName) {
        this.name = name;
        this.role = role;
        this.facultyName = facultyName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getFacultyName() {
        return facultyName;
    }

    public void setFacultyName(String facultyName) {
        this.facultyName = facultyName;
    }

    @Override
    public String toString() {
        return
                "ชื่อ : " + name + ' ' +
                        " ตำแหน่ง : " + role +
                        "คณะ" + facultyName + ' ' ;
    }

}
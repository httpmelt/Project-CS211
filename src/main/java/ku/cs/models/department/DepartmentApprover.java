package ku.cs.models.department;

public class DepartmentApprover {
    private String name;
    private String surname;
    private String role;

    public DepartmentApprover(String name, String surname, String role) {
        this.name = name;
        this.surname = surname;
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "ชื่อ : " + name + " นามสกุล : " + surname + " ตำแหน่ง : " + role;
    }
}

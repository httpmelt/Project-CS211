package ku.cs.models.student;
/* เจ้าหน้าที่ภาควิชาเป็นคนทำ */
public class Student {
    private String studentId;
    private String name;
    private String surname;
    private String email;
    private String teacherId;

    public Student(String studentId, String name, String surname, String email, String teacherId) {
        this.studentId = studentId;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.teacherId = teacherId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    @Override
    public String toString() {
        return "Student{" +
                "studentId='" + studentId + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", email='" + email + '\'' +
                ", teacherId='" + teacherId + '\'' +
                '}';
    }
}

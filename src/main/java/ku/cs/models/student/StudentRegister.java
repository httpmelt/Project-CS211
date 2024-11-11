package ku.cs.models.student;

public class StudentRegister {
    private String username;
    private String password;
    private String studentId;

    public StudentRegister(String username, String password, String studentId) {
        this.username = username;
        this.password = password;
        this.studentId = studentId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStudentId() {return studentId;}

    public void setStudentId(String studentId) {this.studentId = studentId;}



    @Override
    public String toString() {
        return "StudentRegister{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", studentId='" + studentId + '\'' +
                '}';
    }
}

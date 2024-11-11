package ku.cs.models.user;

import java.time.LocalDateTime;

public class User {
    protected String defaultProfilePic;
    protected String role;
    protected String name;
    protected String surname;
    protected String username;
    protected String password;
    public String facultyId;
    protected boolean register;
    private String fullName;
    private boolean access;
    private LocalDateTime time;



    // teacher ใช้
    public User(String defaultProfilePic, String role, String name, String surname, String username, String password, String facultyId) {
        this.defaultProfilePic = defaultProfilePic;
        this.role = role;
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.password = password;
        this.facultyId = facultyId;
    }

    // faculty staff, department.csv staff
    public User(String name, String surname, String username, String password) {
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.password = password;
    }

    public User(String defaultProfilePic, String role, String name, String surname, String username, String password, String facultyId, String majorId, String teacherId, boolean register) {
        this.defaultProfilePic = defaultProfilePic;
        this.role = role;
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.password = password;
        this.facultyId = facultyId;
        this.register = register;

    }


    // Staff Login
    public User(String defaultProfilePic, String role, String name, String surname, String username, String password, String facultyId, boolean register, LocalDateTime time) {
        this.defaultProfilePic = defaultProfilePic;
        this.role = role;
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.password = password;
        this.facultyId = facultyId;
        this.register = register;
        this.time = time;
    }

    public User(String username, String name, String role, String facultyId, String defaultProfilePic) {
        this.username = username;
        this.name = name;
        this.role = role;
        this.facultyId = facultyId;
        this.defaultProfilePic = defaultProfilePic;
    }



////    // All User
////    public User(String defaultProfilePic, String role, String name, String surname,String username, LocalDateTime time){
////        this.defaultProfilePic = defaultProfilePic;
////        this.role = role;
////        this.name = name;
////        this.surname = surname;
////        this.username = username;
////        this.time = time;
//    }

    public User(String defaultProfilePic, String role, String name, String surname, String username, String password) {
        this.defaultProfilePic = defaultProfilePic;
        this.role = role;
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.password = password;
    }

    public User(String defaultProfilePic, String role, String name, String surname, String username, LocalDateTime time) {
        this.defaultProfilePic = defaultProfilePic;
        this.role = role;
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.time = time;
    }

    public User(String defaultProfilePic, String role, String name, String surname, String username, String password, String facultyId, String majorId, String teacherId, boolean register, LocalDateTime dateTime) {
        this.defaultProfilePic = defaultProfilePic;
        this.role = role;
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.password = password;
        this.facultyId = facultyId;
        this.register = register;
        this.time = dateTime;
    }


    public boolean isAccess() {
        return access;
    }

    public void setAccess(boolean access) {
        this.access = access;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }


    public boolean isRegister() {
        return register;
    }

    public void setRegister(boolean register) {
        this.register = register;
    }

    public String getDefaultProfilePic() {
        return defaultProfilePic;
    }

    public void setDefaultProfilePic(String defaultProfilePic) {
        this.defaultProfilePic = defaultProfilePic;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public String getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(String facultyId) {
        this.facultyId = facultyId;
    }

    public void setPassword(String newPassword) {
        this.password = newPassword;
    }





    @Override
    public String toString() {
        return "AllOfficer{" +
                "role='" + role + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", facultyId='" + facultyId + '\'' +
                '}';
    }

}
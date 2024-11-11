package ku.cs.models.admin;

import java.time.LocalDateTime;

public class Data {
    private String profile;
    private String role;
    private String name;
    private String surname;
    private String username;
    private LocalDateTime time;

    public Data(String profile, String role,String name, String surname, String username, LocalDateTime time) {
        this.profile = profile;
        this.role = role;
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.time = time;
    }


    public boolean isData(String user){
        return this.username.equals(user);
    }


    public String getProfile() {
        return profile;
    }

    public String getRole() {
        return role;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getUsername() {
        return username;
    }


    public LocalDateTime getTime() {
        return time;
    }



    public void setProfile(String profile) {
        this.profile = profile;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public void setTime(LocalDateTime time) {
        this.time = time;
    }


}

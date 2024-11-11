package ku.cs.controllers.student;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import ku.cs.models.student.*;
import ku.cs.models.user.User;
import ku.cs.models.user.UserList;
import ku.cs.services.*;
import ku.cs.services.student.StudentDatasource;
import ku.cs.services.student.StudentRegisterDatasource;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class StudentLoginController {

    private User user;
    private UserList userList;
    private UserListFileDatasource userListFileDatasource;

    private StudentList studentList;

    private String studentId = new String();

    @FXML
    private ImageView KULogoView, sampleImageView;

    @FXML
    private TextField username;

    @FXML private PasswordField password;

    @FXML
    private Label usernameErrorLabel, passwordErrorLabel;



    @FXML
    public void onNextToCheckStatusClick() {
        if (validLogin(username.getText(), String.valueOf(Objects.hashCode(password.getText())))) {

            try {
                Map<String, Object> data = new HashMap<>();
                user = userList.findUserByUsername(username.getText());
                data.put("username", username.getText());
                data.put("name", user.getName() + " " + user.getSurname());
                data.put("profilePic", user.getDefaultProfilePic());
                data.put("role","นิสิต");
                data.put("studentId",studentId);
                FXRouter.goTo("ApplicationStatus", data);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            passwordErrorLabel.setText("Invalid username or password.");
        }
    }

    @FXML
    public void onNextToRegistrationClick() {
        try {
            FXRouter.goTo("StudentRegister");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void onBackButton() {
        try {
            FXRouter.goTo("homepage");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void initialize() {

        userListFileDatasource = new UserListFileDatasource("data/user", "Allusers.csv");
        userList = userListFileDatasource.readData();

        StudentDatasource studentListDatasource = new StudentDatasource("data/student", "student.csv");
        studentList = studentListDatasource.readData();

        usernameErrorLabel.setText("");
        passwordErrorLabel.setText("");


        Image KU_Logo = new Image(getClass().getResource("/images/ku_logo1.png").toString());
        KULogoView.setImage(KU_Logo);

        Image image = new Image(getClass().getResourceAsStream("/images/login_page.jpg"));
        sampleImageView.setImage(image);
    }

    private boolean validLogin(String username, String password) {
        StudentRegisterDatasource datasource = new StudentRegisterDatasource("data/student", "studentRegister.csv");
        StudentRegisterList studentRegisterList = datasource.readData();

        for (StudentRegister studentRegister : studentRegisterList.getStudentRegisters()) {
            if (studentRegister.getUsername().equals(username)
                    && studentRegister.getPassword().equals(password)) {

                studentId = studentRegister.getStudentId();

               User user = userList.findUserByUsername(username);
                if (user != null) {
                    user.setTime(LocalDateTime.now());
                    userListFileDatasource.writeData(userList);
                }

                return true;
            }
        }
        return false;
    }


}

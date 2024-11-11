package ku.cs.controllers.student;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import ku.cs.models.student.*;
import ku.cs.models.user.User;
import ku.cs.models.user.User;
import ku.cs.models.user.UserList;
import ku.cs.services.*;
import ku.cs.services.student.StudentDatasource;
import ku.cs.services.student.StudentRegisterDatasource;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;

public class StudentRegistrationController {
    @FXML
    private ImageView KULogoView, KUView1;

    @FXML
    private TextField username, password, confirmPassword, name, surname, studentId, email;

    @FXML
    private Label userWarning, passWarning, confirmPassWarning;

    @FXML
    public void initialize() {
        Image KU_Logo = new Image(getClass().getResource("/images/ku_logo1.png").toString());
        KULogoView.setImage(KU_Logo);

        Image KU_Pic1 = new Image(getClass().getResource("/images/ku-pic1.png").toString());
        KUView1.setImage(KU_Pic1);

        userWarning.setVisible(false);
        passWarning.setVisible(false);
        confirmPassWarning.setVisible(false);

    }

    @FXML
    public void onBackToLoginPageClick() {
        try {
            FXRouter.goTo("StudentLogin");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @FXML
    public void onSubmitRegistrationClick() {

        boolean validUsernamePassword = true;

        if (username.getText().trim().length() < 3) {
            userWarning.setVisible(true);
            validUsernamePassword = false;
        } else {
            userWarning.setVisible(false);
        }

        if (password.getText().trim().length() < 8) {
            passWarning.setVisible(true);
            validUsernamePassword = false;
        } else {
            passWarning.setVisible(false);
        }

        if (!(confirmPassword.getText().trim().equals(password.getText().trim()))) {
            confirmPassWarning.setVisible(true);
            validUsernamePassword = false;
        } else {
            confirmPassWarning.setVisible(false);
        }

        //การยืนยันตัวตน
        if (validUsernamePassword) {
            if (verifyStudentInformation(name.getText().trim(), surname.getText().trim(), email.getText().trim(), studentId.getText().trim())) {


              if  (addStudentRegister(username.getText().trim(), password.getText().trim(), name.getText().trim(), surname.getText().trim(), email.getText().trim(), studentId.getText().trim())){
                  addStudentUser("default-profile-pic.png" ,
                          "นิสิต",
                          name.getText().trim(),
                          surname.getText().trim(),
                          username.getText().trim(),
                          password.getText().trim(),
                          "ลงทะเบียน");
                  infoBox("การยืนยันตัวตน", "บันทึกข้อมูลสำเร็จ");
                  try {
                      FXRouter.goTo("StudentLogin");
                  } catch (IOException e) {
                      throw new RuntimeException(e);
                  }
              } else {
                  msgBox("การยืนยันตัวตน","username already use");
              }


            } else {
                msgBox("การยืนยันตัวตน", "ไม่ถูกต้อง");
            }
        }
    }

    private void msgBox(String title, String header) {

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.showAndWait();

    }

    private void infoBox(String title, String header) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.showAndWait();

    }

    private boolean verifyStudentInformation(String name, String surname, String email, String studentId) {
        Datasource datasource1 = new StudentDatasource("data/student", "student.csv");
        StudentList studentList = (StudentList) datasource1.readData();

        for (Student student : studentList.getStudents()) {
            if ((student.getName().equals(name))
                    && student.getSurname().equals(surname)
                    && student.getEmail().equals(email)
                    && student.getStudentId().equals(studentId)) {
                return true;
            }
        }
        return false;
    }

    private boolean addStudentRegister(String username, String password, String name, String surname, String email, String studentId) {
        StudentRegisterDatasource datasource = new StudentRegisterDatasource("data/student", "studentRegister.csv");
        StudentRegisterList studentRegisterList = datasource.readData();

        for (StudentRegister studentRegister : studentRegisterList.getStudentRegisters()) {
            if (studentRegister.getUsername().equals(username)) {
                return false;
            }
        }
        studentRegisterList.addNewStudentRegister(username, String.valueOf(Objects.hashCode(password)), studentId);
        datasource.writeData(studentRegisterList);
        return true;
    }

    private void addStudentUser(String defaultProfilePic, String role, String name, String surname, String username, String password, String facultyId) {
        User user;
        UserList userList;
        UserListFileDatasource userListFileDatasource;

        userListFileDatasource = new UserListFileDatasource("data/user", "Allusers.csv");
        userList = userListFileDatasource.readData();
        User loginUser = new User(defaultProfilePic, role, name, surname, username, password, facultyId);
        loginUser.setTime(LocalDateTime.now());
        userList.addUser(loginUser);
        userListFileDatasource.writeData(userList);
    }

}
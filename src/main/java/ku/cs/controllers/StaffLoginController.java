package ku.cs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import ku.cs.models.user.*;
import ku.cs.services.FXRouter;
import ku.cs.services.StaffListFileDatasource;
import ku.cs.services.UserListFileDatasource;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class StaffLoginController {
    @FXML private ImageView sampleImageView;
    @FXML private ImageView sampleImageView2;

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;


    private StaffList staffList;
    private StaffListFileDatasource staffListFileDatasource;

    private User user;
    private UserList userList;
    private UserListFileDatasource userListFileDatasource;


    @FXML
    public void initialize() {
        Image image = new Image(getClass().getResourceAsStream("/images/login_page.jpg"));
        sampleImageView.setImage(image);

        Image image2 = new Image(getClass().getResourceAsStream("/images/ku_logo1.png"));
        sampleImageView2.setImage(image2);

        staffListFileDatasource = new StaffListFileDatasource("data/user", "Staffuser.csv");
        staffList = staffListFileDatasource.readData();

        userListFileDatasource = new UserListFileDatasource("data/user", "Allusers.csv");
        userList = userListFileDatasource.readData();
    }

    @FXML
    public void onLoginButtonClick() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        Staff staff = staffList.findStaffByUsername(username);

        if (staff == null) {
            showAlert("ไม่พบผู้ใช้งานในระบบ");
            return;
        }

        if (!staff.isRegister()) {
            if (password.equals(staff.getPassword())) {
                updateLoginTime(staff);
                saveData();
                goToOption(staff);
            } else {
                showAlert("ชื่อผู้ใช้งานหรือรหัสผ่านไม่ถูกต้อง");
            }
        } else {
            if (BCrypt.checkpw(password, staff.getPassword())) {
                updateLoginTime(staff);
                saveData();
                goToRolePage(staff);
            } else {
                showAlert("ชื่อผู้ใช้งานหรือรหัสผ่านไม่ถูกต้อง");
            }
        }
    }

    private void updateLoginTime(Staff staff) {
        staff.setTime(LocalDateTime.now());

        User user = userList.findUserByUsername(staff.getUsername());
        if (user != null) {
            user.setTime(LocalDateTime.now());

            boolean isSaved = false;
            userListFileDatasource.writeData(userList);
            if (isSaved) {
                System.out.println("บันทึกข้อมูลเรียบร้อยแล้ว");
            }
        }
    }



    private void saveData() {
        staffListFileDatasource.writeData(staffList);
        userListFileDatasource.writeData(userList);
    }


    private void goToRolePage(Staff staff) {
        String role = staff.getRole();

        try {
            switch (role) {
                case "ผู้ดูแลระบบ":
                    FXRouter.goTo("admin", staff);
                    break;
                case "เจ้าหน้าที่คณะ":
                    Map<String, Object> forFaculty = new HashMap<>();
                    forFaculty.put("facultyId", staff.getFacultyId());
                    forFaculty.put("name", staff.getName() + " " + staff.getSurname());
                    forFaculty.put("username", staff.getUsername());
                    forFaculty.put("profilePic", staff.getDefaultProfilePic());
                    forFaculty.put("role", staff.getRole());
                    FXRouter.goTo("faculty-staff-show-table", forFaculty);
                    break;
                case "เจ้าหน้าที่ภาควิชา":
                    Map<String, Object> forDepartment = new HashMap<>();
                    forDepartment.put("majorId", staff.getMajorID());
                    forDepartment.put("name", staff.getName() + " " + staff.getSurname());
                    forDepartment.put("username", staff.getUsername());
                    forDepartment.put("profilePic", staff.getDefaultProfilePic());
                    forDepartment.put("role", staff.getRole());
                    FXRouter.goTo("faculty-staff-list", forDepartment);
                    break;
                case "อาจารย์ที่ปรึกษา":
                    Map<String, Object> forTeacher = new HashMap<>();
                    forTeacher.put("name", staff.getName() + " " + staff.getSurname());
                    forTeacher.put("username", staff.getUsername());
                    forTeacher.put("profilePic", staff.getDefaultProfilePic());
                    forTeacher.put("role", staff.getRole());
                    forTeacher.put("teacherId", staff.getTeacherID());
                    FXRouter.goTo("Teacher_StudentTableList", forTeacher);
                    break;
                default:
                    showAlert("Unknown role");
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("ชื่อผู้ใช้งานหรือรหัสผ่านไม่ถูกต้อง");
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("เกิดข้อผิดพลาด");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void goToOption(Staff staff) {
        try {
            Map<String, Object> forOption = new HashMap<>();
            forOption.put("majorId", staff.getMajorID());
            forOption.put("username", staff.getUsername());
            forOption.put("name", staff.getName() + " " + staff.getSurname());
            forOption.put("profilePic", staff.getDefaultProfilePic());
            forOption.put("role", staff.getRole());
            FXRouter.goTo("change-password", forOption);
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
}

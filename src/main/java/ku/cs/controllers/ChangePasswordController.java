package ku.cs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import ku.cs.models.user.Staff;
import ku.cs.models.user.StaffList;
import ku.cs.models.user.User;
import ku.cs.models.user.UserList;
import ku.cs.services.FXRouter;
import ku.cs.services.StaffListFileDatasource;
import ku.cs.services.UserListFileDatasource;
import org.mindrot.jbcrypt.BCrypt;

import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class ChangePasswordController {
    @FXML
    private ImageView KUView, profilePicOption;
    @FXML
    private TextField confirmNewPassword, newPassword, oldPassword;
    @FXML
    private Label roleLabel, usernameLabel, fullnameLabel;

    private StaffList staffList;
    private StaffListFileDatasource staffListFileDatasource;

    private UserList userList;
    private UserListFileDatasource userListFileDatasource;

    private String username, name, role, profilePic;
    private Staff currentUser;

    @FXML
    public void initialize() {
        Image KU_Pic1 = new Image(getClass().getResource("/images/ku-pic1.png").toString());
        KUView.setImage(KU_Pic1);

        staffListFileDatasource = new StaffListFileDatasource("data/user", "Staffuser.csv");
        staffList = staffListFileDatasource.readData();

        userListFileDatasource = new UserListFileDatasource("data/user", "Allusers.csv");
        userList = userListFileDatasource.readData();

        Map<String, Object> data = (Map<String, Object>) FXRouter.getData();
        name = (String) data.get("name");
        username = (String) data.get("username");
        profilePic = (String) data.get("profilePic");
        role = (String) data.get("role");

        if (username != null) {
            usernameLabel.setText(username);
            roleLabel.setText(role);
            fullnameLabel.setText(name);

            String profilePicPath = profilePic != null ? profilePic : "default-profile-pic.png";
            Path profilePicFullPath = Paths.get("data/profile-pic/", profilePicPath);
            File profilePicFile = profilePicFullPath.toFile();
            if (profilePicFile.exists()) {
                Image profileImage = new Image(profilePicFile.toURI().toString());
                profilePicOption.setImage(profileImage);
            } else {
                Image defaultImage = new Image(getClass().getResource("/images/default-profile-pic.png").toString());
                profilePicOption.setImage(defaultImage);
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "User data not received correctly.");
        }

        currentUser = staffList.findStaffByUsername(username);
    }

    @FXML
    public void onSubmitButton() {
        String oldPasswordInput = oldPassword.getText().trim();
        String newPasswordInput = newPassword.getText().trim();
        String confirmNewPasswordInput = confirmNewPassword.getText().trim();

        if (oldPasswordInput.isEmpty() || newPasswordInput.isEmpty() || confirmNewPasswordInput.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "ดำเนินการไม่สำเร็จ", "กรอกข้อมูลไม่ครบ");
            return;
        }

        if (newPasswordInput.length() < 8) {
            showAlert(Alert.AlertType.ERROR, "ดำเนินการไม่สำเร็จ", "รหัสผ่านใหม่ต้องมีความยาวอย่างน้อย 8 ตัวอักษร");
            return;
        }

        boolean isPasswordCorrect = currentUser.isRegister() ?
                BCrypt.checkpw(oldPasswordInput, currentUser.getPassword()) :
                oldPasswordInput.equals(currentUser.getPassword());

        if (isPasswordCorrect) {
            if (newPasswordInput.equals(confirmNewPasswordInput)) {
                updatePassword(newPasswordInput);
                goToLoginButton();
            } else {
                showAlert(Alert.AlertType.ERROR, "ดำเนินการไม่สำเร็จ", "รหัสผ่านใหม่ไม่ตรงกัน");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "ดำเนินการไม่สำเร็จ", "รหัสผ่านเดิมไม่ถูกต้อง");
        }
    }

    private void updatePassword(String newPassword) {
        String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
        currentUser.setPassword(hashedPassword);
        currentUser.setRegister(true);

        for (Staff member : staffList.getAllOfficers()) {
            if (member.getUsername().equals(currentUser.getUsername())) {
                member.setPassword(hashedPassword);
                member.setRegister(true);
                break;
            }
        }

        saveStaffList();
        saveAllUser();
    }

    private void updateProfilePic(String newProfilePicFilename) {
        currentUser.setDefaultProfilePic(newProfilePicFilename);

        for (Staff member : staffList.getAllOfficers()) {
            if (member.getUsername().equals(currentUser.getUsername())) {
                member.setDefaultProfilePic(newProfilePicFilename);
                break;
            }
        }
        saveStaffList();

        for (User user : userList.getUsers()) {
            if (user.getUsername().equals(currentUser.getUsername())) {
                user.setDefaultProfilePic(newProfilePicFilename);
                break;
            }
        }
        saveAllUser();
    }

    private void saveStaffList() {
        try {
            staffListFileDatasource.writeData(staffList);
            showAlert(Alert.AlertType.INFORMATION, "ดำเนินการสำเร็จ", "บันทึกข้อมูลใน Staffuser.csv เรียบร้อย");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "ดำเนินการไม่สำเร็จ", "ไม่สามารถบันทึกข้อมูลลงไฟล์ Staffuser.csv ได้");
            e.printStackTrace();
        }
    }

    private void saveAllUser() {
        try {
            userListFileDatasource.writeData(userList);
            showAlert(Alert.AlertType.INFORMATION, "ดำเนินการสำเร็จ", "บันทึกข้อมูลใน Allusers.csv เรียบร้อย");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "ดำเนินการไม่สำเร็จ", "ไม่สามารถบันทึกข้อมูลลงไฟล์ Allusers.csv ได้");
            e.printStackTrace();
        }
    }

    private String uploadProfilePic(File file) throws IOException {
        Path destinationDirectory = Paths.get("data/profile-pic/");
        Files.createDirectories(destinationDirectory);

        String uniqueFileName = System.currentTimeMillis() + "_" + file.getName();
        Path destinationFile = destinationDirectory.resolve(uniqueFileName);
        Files.copy(file.toPath(), destinationFile, StandardCopyOption.REPLACE_EXISTING);

        return destinationFile.getFileName().toString();
    }

    @FXML
    private void uploadPicButton() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image files (*.jpg, *.png, *.jpeg)", "*.jpg", "*.png", "*.jpeg"));
        File file = fileChooser.showOpenDialog(profilePicOption.getScene().getWindow());

        if (file != null) {
            try {
                String newProfilePicFilename = uploadProfilePic(file);
                Image newProfileImage = new Image(Paths.get("data/profile-pic/", newProfilePicFilename).toUri().toString());
                profilePicOption.setImage(newProfileImage);

                updateProfilePic(newProfilePicFilename);
            } catch (IOException e) {
                showAlert(Alert.AlertType.ERROR, "Upload Failed", "Could not upload the profile picture.");
                e.printStackTrace();
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Upload Failed", "No file selected.");
        }
    }

    @FXML
    private void goToLoginButton() {
        try {
            FXRouter.goTo("staff-login");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void goToLoginPageButton() {
        try {
            FXRouter.goTo("homepage");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
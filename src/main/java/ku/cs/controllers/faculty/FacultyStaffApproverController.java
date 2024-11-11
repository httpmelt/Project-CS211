package ku.cs.controllers.faculty;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import ku.cs.models.faculty.FacultyApprover;
import ku.cs.models.faculty.FacultyApproverList;
import ku.cs.services.FXRouter;
import ku.cs.services.faculty.FacultyApproverListDatasource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class FacultyStaffApproverController {

    @FXML
    private ListView<FacultyApprover> approverListView;

    @FXML
    private TextField facultyTextField;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField roleTextField;

    @FXML
    private Label nameLabel;

    @FXML
    private ImageView profilePicLabel;

    private FacultyApproverListDatasource datasource;
    private FacultyApproverList approverList;
    private ObservableList<FacultyApprover> observableApprovers;

    private String username, name, profilePic,facultyId, role;

    @FXML
    public void initialize() {
        Map<String, Object> data = (Map<String, Object>) FXRouter.getData();
        name = (String) data.get("name");
        username = (String) data.get("username");
        profilePic = (String) data.get("profilePic");
        facultyId = (String) data.get("facultyId");
        role = (String) data.get("role");

        nameLabel.setText(name);

        if (profilePic != null && !profilePic.isEmpty()) {

            Path profilePicPath = Paths.get("data/profile-pic/", profilePic);
            File profilePicFile = profilePicPath.toFile();
            if (profilePicFile.exists()) {
                Image userImage = new Image(profilePicFile.toURI().toString());
                profilePicLabel.setImage(userImage);
            } else {
                Image defaultPic = new Image(getClass().getResource("data/profile-pic/default-profile-pic.png").toString());
                profilePicLabel.setImage(defaultPic);
            }
        } else {
            Image defaultPic = new Image(getClass().getResource("/default-profile-pic.png").toString());
            profilePicLabel.setImage(defaultPic);
        }


        datasource = new FacultyApproverListDatasource("data/staff", "FacultyApprover.csv");
        approverList = datasource.readData();

        observableApprovers = FXCollections.observableArrayList(approverList.getAllFacultyApprovers());
        approverListView.setItems(observableApprovers);

        approverListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                nameTextField.setText(newValue.getName());
                roleTextField.setText(newValue.getRole());
                facultyTextField.setText(newValue.getFacultyName());
            }
        });
    }

    @FXML
    void onUpdateApproverButton() {
        String name = nameTextField.getText().trim();
        String role = roleTextField.getText().trim();
        String faculty = facultyTextField.getText().trim();

        if (name.isEmpty() || role.isEmpty() || faculty.isEmpty()) {
            showAlert(AlertType.ERROR, "เพิ่มผู้อนุมัติไม่สำเร็จ", "กรุณากรอกข้อมูลให้ครบถ้วน");
            return;
        }

        approverList.addFacultyApprover(name, role, faculty);
        observableApprovers.setAll(approverList.getAllFacultyApprovers());
        datasource.writeData(approverList);
        nameTextField.clear();
        roleTextField.clear();
        facultyTextField.clear();

        showAlert(AlertType.INFORMATION, "เพิ่มผู้อนุมัติสำเร็จ", "เพิ่มผู้อนุมัติในระบบเรียบร้อย");
    }

    @FXML
    void onEditApproverButton() {
        FacultyApprover selectedApprover = approverListView.getSelectionModel().getSelectedItem();

        if (selectedApprover == null) {
            showAlert(AlertType.ERROR, "แก้ไขไม่สำเร็จ", "กรุณาเลือกผู้อนุมัติที่ต้องการแก้ไข");
            return;
        }

        String name = nameTextField.getText().trim();
        String role = roleTextField.getText().trim();
        String faculty = facultyTextField.getText().trim();

        if (name.isEmpty() || role.isEmpty() || faculty.isEmpty()) {
            showAlert(AlertType.ERROR, "แก้ไขไม่สำเร็จ", "กรุณากรอกข้อมูลให้ครบถ้วน");
            return;
        }

        selectedApprover.setName(name);
        selectedApprover.setRole(role);
        selectedApprover.setFacultyName(faculty);

        observableApprovers.setAll(approverList.getAllFacultyApprovers());
        datasource.writeData(approverList);

        nameTextField.clear();
        roleTextField.clear();
        facultyTextField.clear();

        showAlert(AlertType.INFORMATION, "แก้ไขสำเร็จ", "แก้ไขข้อมูลผู้อนุมัติเรียบร้อยแล้ว");
    }

    @FXML
    void onFacultyStaffManagementTableButtonClick() {
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("name", nameLabel.getText());
            data.put("username", username);
            data.put("profilePic", profilePic);
            data.put("facultyId", facultyId);
            FXRouter.goTo("faculty-staff-management-table",data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void showAlert(AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    void onFacultyStaffShowTableButtonClick() {
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("name", name);
            data.put("username", username);
            data.put("profilePic", profilePic);
            data.put("role", role);
            data.put("facultyId", facultyId);
            FXRouter.goTo("faculty-staff-show-table",data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onLogoutButtonClick() {
        try {
            FXRouter.goTo("homepage");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    void goToOptionButton() {
        try {
            Map<String, Object> forOption = new HashMap<>();
            forOption.put("username", username);
            forOption.put("name", name);
            forOption.put("profilePic", profilePic);
            forOption.put("role", "เจ้าหน้าที่คณะ");
            FXRouter.goTo("change-password", forOption);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
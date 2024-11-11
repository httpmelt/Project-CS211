package ku.cs.controllers.student;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import ku.cs.models.student.Student;
import ku.cs.models.student.StudentList;
import ku.cs.models.student.StudentReq;
import ku.cs.services.FXRouter;
import ku.cs.services.student.StudentDatasource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

public class StudentApplicationFormTrackingTableController extends StudentApplicationListController {
    private String studentId = new String();
    private String teacherId = new String();

    // Convert StudentReqList to ObservableList
    ObservableList<StudentReq> observableList;

    @FXML
    private ImageView profilePicLabel;

    @FXML
    private TextField searchField;

    @FXML
    private TableView<StudentReq> studentApplicationFormTableView;

    private String name, username, profilePic, role;

    @FXML
    private Label nameLabel;

    @FXML
    public void onKeyChange(){
        FilteredList<StudentReq> filteredData = new FilteredList<>(observableList, b -> true);
        String newValue = searchField.getText();
        if(newValue.trim().isEmpty()){
            showStudentReqList(studentApplicationFormTableView, studentId);
        } else {
            filteredData.setPredicate(req -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (req.getReqStatus().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (req.getProcessStatus().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (req.getFormName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
            studentApplicationFormTableView.setItems(filteredData);

        }


    }

    @FXML
    public void onBackToAddFormClick() {
        openNewReqForm(studentId, "-1");
    }

    @FXML
    public void onToAddOrDropSubjectFormClick() {
        openNewReqForm(studentId, "-2");
    }

    @FXML
    public void onToLeaveOfAbsenceFormClick() {
        openNewReqForm(studentId, "-3");
    }

    @FXML
    public void goToStudentOption() {
        try {
            Map<String, Object> forOption = new HashMap<>();
            forOption.put("username", username);
            forOption.put("name", name);
            forOption.put("profilePic", profilePic);
            forOption.put("role", "นิสิต");
            FXRouter.goTo("student-change-password", forOption);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onLogoutClick() {
        try {
            FXRouter.goTo("homepage");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void teacherBackPage() {
        try {
            FXRouter.goTo("Teacher_StudentTableList", teacherId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void downloadApprovedFile() {
        StudentReq selectedRequest = studentApplicationFormTableView.getSelectionModel().getSelectedItem();
        downloadFile(selectedRequest);
    }


    public void downloadFile(StudentReq selectedRequest) {
        if (selectedRequest == null) {
            msgBox("Error", "Please select a request to download the approved file.");
            return;
        }

        String reqId = String.valueOf(selectedRequest.getReqId());
        String formName = selectedRequest.getFormName();
        String studentId = selectedRequest.getStudentId();

        String fileName = "ใบอนุมัติคำร้อง_ใบคำร้องที่_" + reqId + "_" + formName + "_" + studentId + ".pdf";

        Path folderPath = Paths.get("data/ApprovedByStaff");

        Path filePath = folderPath.resolve(fileName);

        if (Files.exists(filePath)) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialFileName(fileName);
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));

            File fileToSave = fileChooser.showSaveDialog(null);
            if (fileToSave != null) {
                try {
                    Files.copy(filePath, fileToSave.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    showAlert(Alert.AlertType.INFORMATION, "ดาวน์โหลดไฟล์สำเร็จ", "ดาวน์โหลดไฟล์ " + fileName + " เรียบร้อยแล้ว");
                } catch (IOException e) {
                    showAlert(Alert.AlertType.ERROR, "ดาวน์โหลดไฟล์ไม่สำเร็จ", "ไม่สามารถดาวน์โหลดไฟล์ได้");
                    e.printStackTrace();
                }
            } else {
                //showAlert(Alert.AlertType.ERROR, "ดาวน์โหลดไฟล์ไม่สำเร็จ", "คำร้องที่เลือกยังไม่ได้รับการอนุมัติหรือคำร้องที่เลือกถูกปฏิเสธ");
            }
        } else {
            // Show an error if the file does not exist
            showAlert(Alert.AlertType.ERROR, "ดาวน์โหลดไฟล์ไม่สำเร็จ", "คำร้องที่เลือกยังไม่ได้รับการอนุมัติหรือคำร้องที่เลือกถูกปฏิเสธ");
        }
    }


    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }


    @FXML
    public void initialize() {
        Map<String, Object> data = (Map<String, Object>) FXRouter.getData();
        name = (String) data.get("name");
        username = (String) data.get("username");
        profilePic = (String) data.get("profilePic");
        role = (String) data.get("role");
        studentId = (String) data.get("studentId");

        nameLabel.setText(name);
        setProfilePicture();

        showStudentReqList(studentApplicationFormTableView, studentId);

        observableList = FXCollections.observableArrayList(showStudentReqList.getStudentReqs());

        studentApplicationFormTableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // ตรวจสอบว่าคลิกสองครั้ง
                // รับข้อมูลจากแถวที่ถูกดับเบิลคลิก
                StudentReq selectedItem = studentApplicationFormTableView.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    if (getStudentTeacherId(studentId).equals("")) {
                        msgBox("ไม่สามารถดูรายการคำร้องได้", "ยังไม่มีอาจารย์ที่ปรึกษา");
                    } else {
                        try {
                            FXRouter.goTo("ReqFormDetail", String.join(",", "ApplicationStatus", String.valueOf(selectedItem.getReqId())));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        });

    }

    private void setProfilePicture() {
        if (profilePic != null && !profilePic.isEmpty()) {
            Path profilePicPath = Paths.get("data/profile-pic/", profilePic);
            File profilePicFile = profilePicPath.toFile();
            if (profilePicFile.exists()) {
                Image userImage = new Image(profilePicFile.toURI().toString());
                profilePicLabel.setImage(userImage);
            } else {
                Image defaultPic = new Image(getClass().getResource("/default-profile-pic.png").toString());
                profilePicLabel.setImage(defaultPic);
            }
        } else {
            Image defaultPic = new Image(getClass().getResource("/default-profile-pic.png").toString());
            profilePicLabel.setImage(defaultPic);
        }
    }

    public void openNewReqForm(String studentId, String reqFormId) {
        if (getStudentTeacherId(studentId).equals("")) {
            msgBox("ไม่สามารถสร้างคำร้องได้", "ยังไม่มีอาจารย์ที่ปรึกษา");
        } else {
            try {
                FXRouter.goTo("ReqFormDetail", String.join(",", "ApplicationStatus", reqFormId, studentId));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
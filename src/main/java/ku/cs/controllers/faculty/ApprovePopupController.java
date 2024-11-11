package ku.cs.controllers.faculty;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ku.cs.controllers.ApplicationApprovalController;
import ku.cs.models.faculty.FacultyApprover;
import ku.cs.models.faculty.FacultyApproverList;
import ku.cs.services.faculty.FacultyApproverListDatasource;
import ku.cs.services.FXRouter;
import ku.cs.services.faculty.FacultyUserListFileDatasource;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ApprovePopupController extends ApplicationApprovalController {

    @FXML
    private AnchorPane approveAnchor;

    @FXML
    private ComboBox<String> approverComboBox;

    @FXML
    private AnchorPane rejectAnchor;

    @FXML
    private TextArea rejectTextField;

    @FXML
    private Label fileNameLabel;

    @FXML
    private CheckBox approveCheckBox;

    @FXML
    private CheckBox rejectCheckBox;

    private String facultyId, name, username, profilePic, role, formName, studentId;
    private int reqId, reqProcessId;

    private String rejectReason;
    private String selectedApprover;
    private String attachedFileName;

    private FacultyApproverListDatasource approverListDatasource;
    private FacultyApproverList approverList;
    private FacultyUserListFileDatasource userListFileDatasource;

    private Map<String, String> facultyIdToNameMap = new HashMap<>();

    public void setData(String facultyId, String name, String username, String profilePic, String role, int reqId, int reqProcessId, String formName, String studentId) {
        this.facultyId = facultyId;
        this.name = name;
        this.username = username;
        this.profilePic = profilePic;
        this.role = role;
        this.reqId = reqId;
        this.reqProcessId = reqProcessId;
        this.formName = formName;
        this.studentId = studentId;
    }

    @FXML
    public void initialize() {
        Map<String, Object> data = (Map<String, Object>) FXRouter.getData();
        facultyId = (String) data.get("facultyId");
        name = (String) data.get("name");
        username = (String) data.get("username");
        profilePic = (String) data.get("profilePic");
        role = (String) data.get("role");
        formName = (String) data.get("formName");
        studentId = (String) data.get("studentId");
        reqId = (int) data.get("requestId");
        reqProcessId = (int) data.get("reqProcessId");


        approveAnchor.setVisible(false);
        rejectAnchor.setVisible(false);

        userListFileDatasource = new FacultyUserListFileDatasource("data/user", "faculty.csv");

        facultyIdToNameMap = userListFileDatasource.readFacultyIdToNameMap();
        String facultyName = facultyIdToNameMap.get(facultyId);

        approverListDatasource = new FacultyApproverListDatasource("data/staff", "FacultyApprover.csv");
        approverList = approverListDatasource.readData();

        populateApproverComboBox(facultyName);
    }

    private void populateApproverComboBox(String facultyName) {
        List<String> approversInFaculty = approverList.getFacultyApprovers().stream()
                .filter(approver -> approver.getFacultyName().equals(facultyName))
                .map(FacultyApprover::getName)
                .collect(Collectors.toList());

        approverComboBox.getItems().clear();
        approverComboBox.getItems().addAll(approversInFaculty);
    }


    @FXML
    public void onApproveCheckBoxAction() {
        if (approveCheckBox.isSelected()) {
            approveAnchor.setVisible(true);
            rejectCheckBox.setSelected(false);
            rejectAnchor.setVisible(false);
            rejectTextField.clear();
        } else {
            approveAnchor.setVisible(false);
        }
    }

    @FXML
    public void onAttachfile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            try {
                Path directoryPath = Paths.get("data/ApprovedByStaff");
                if (!Files.exists(directoryPath)) {
                    Files.createDirectories(directoryPath);
                }

                String requestNumber = String.valueOf(reqId);
                String fileExtension = ".pdf";
                Path destinationPath = directoryPath.resolve("ใบอนุมัติคำร้อง_ใบคำร้องที่_" + requestNumber + "_" + formName + "_" + studentId + fileExtension);

                Files.copy(selectedFile.toPath(), destinationPath);

                fileNameLabel.setText(selectedFile.getName());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void onRejectCheckBoxAction() {
        if (rejectCheckBox.isSelected()) {
            rejectAnchor.setVisible(true);
            approveCheckBox.setSelected(false);
            approveAnchor.setVisible(false);
        } else {
            rejectAnchor.setVisible(false);
        }
    }

    public boolean validateRejectReason() {
        rejectReason = rejectTextField.getText().trim();
        if (rejectReason.isEmpty()) {
            rejectTextField.setPromptText("Please provide a reason for rejection.");
            return false;
        }
        return true;
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void onSubmitStatusButton() {
        selectedApprover = approverComboBox.getSelectionModel().getSelectedItem();
        if (approveCheckBox.isSelected() && selectedApprover != null && fileNameLabel != null) {
            approveSubmit(reqId, 7);

        } else if (rejectCheckBox.isSelected() && validateRejectReason()) {
            rejectSubmit(reqId, 8, rejectReason);
            System.out.println("Rejection reason: " + rejectReason);
        } else {
            showAlert(Alert.AlertType.ERROR, "ไม่สามารถดำเนินการคำร้องได้", "กรุณาใส่ข้อมูลให้ครบ");
        }

        try {
            Map<String, Object> data = new HashMap<>();
            data.put("name", name);
            data.put("username", username);
            data.put("profilePic", profilePic);
            data.put("role", role);
            data.put("facultyId", facultyId);
            FXRouter.goTo("faculty-staff-show-table", data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        closePopup();
    }

    private void closePopup() {
        Stage stage = (Stage) approveAnchor.getScene().getWindow();
        stage.close();
    }
}

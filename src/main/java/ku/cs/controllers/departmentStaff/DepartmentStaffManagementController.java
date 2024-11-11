package ku.cs.controllers.departmentStaff;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ku.cs.services.FXRouter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DepartmentStaffManagementController {

    @FXML
    private AnchorPane FeeAndLeaveAnchor;

    @FXML
    private AnchorPane addDropReq;

    @FXML
    private AnchorPane feeReq;

    @FXML
    private AnchorPane leaveReq;

    @FXML
    private Label curriculumLabel;

    @FXML
    private Label degreeLabel;

    @FXML
    private Label formNameLabel;

    @FXML
    private Label studentProcessDateLabel;

    @FXML
    private Label studentIdLabel;

    @FXML
    private Label teacherProcessDateLabel;

    @FXML
    private Label educationGradeLabel;

    @FXML
    private Label extensionDueDateLabel;

    @FXML
    private TextArea reasonTextArea;

    @FXML
    private Label educationYearLabel;

    @FXML
    private Label educationTermLabel;

    @FXML
    private Label facultyLabel;

    @FXML
    private Label feeAmountLabel;

    @FXML
    private Label fileNameLabel;

    @FXML
    private Label majorLabel;

    @FXML
    private Label nameLabel;

    @FXML
    private Label optionAddOrDropLabel;

    @FXML
    private Label processStatusLabel;

    @FXML
    private ImageView profilePicLabel;

    @FXML
    private Label reqStatusLabel;

    @FXML
    private Label studentNameLabel;

    @FXML
    private Label subjectIdLabel;

    @FXML
    private Label subjectLabel;

    private String departmentId, name, username, profilePic, role;
    private String formName, studentId, reqStatus, processStatus, studentName, majorId, majorIdFromTeacher;
    private LocalDateTime studentProcessDate, teacherProcessDate;
    private int educationGrade, educationTerm, educationYear, feeAmount, reqId, reqProcessId;
    private Date extensionDueDate;
    private String optionAddOrDrop, subject, subjectId, degree, curriculum, reason;

    @FXML
    public void initialize() {
        Map<String, Object> data = (Map<String, Object>) FXRouter.getData();

        // ตรวจสอบข้อมูลที่ถูกส่งมา
        if (data == null) {
            showErrorMessage("Data is null");
            return;
        }

        // แสดงข้อมูลพื้นฐาน
        departmentId = (String) data.getOrDefault("majorIdFromTeacher", "N/A");
        name = (String) data.getOrDefault("name", "N/A");
        username = (String) data.getOrDefault("username", "N/A");
        profilePic = (String) data.getOrDefault("profilePic", "N/A");
        role = (String) data.getOrDefault("role", "N/A");
        majorId = (String) data.getOrDefault("majorId", "N/A");
        if (majorId.equals("N/A")) {
            showErrorMessage("Major ID not found.");
            return;  // หยุดการทำงานถ้าไม่มี majorId
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd (HH:mm:ss)");
        nameLabel.setText(name);

        // แสดงข้อมูลที่เกี่ยวข้องกับคำร้อง
        reqId = data.get("requestId") != null ? (int) data.get("requestId") : 0;
        reqProcessId = data.get("reqProcessId") != null ? (int) data.get("reqProcessId") : 0;
        formName = (String) data.getOrDefault("formName", "N/A");
        formNameLabel.setText(formName != null ? formName : "N/A");
        studentId = (String) data.getOrDefault("studentId", "N/A");
        studentIdLabel.setText(studentId != null ? studentId : "N/A");

        studentName = (String) data.getOrDefault("studentName", "N/A");
        studentNameLabel.setText(studentName != null ? studentName : "N/A");

        reqStatus = (String) data.getOrDefault("reqStatus", "N/A");
        reqStatusLabel.setText(reqStatus != null ? reqStatus : "N/A");
        processStatus = (String) data.getOrDefault("processStatus", "N/A");
        processStatusLabel.setText(processStatus != null ? processStatus : "N/A");

        studentProcessDate = (LocalDateTime) data.get("studentProcessDate");
        teacherProcessDate = (LocalDateTime) data.get("teacherProcessDate");

        studentProcessDateLabel.setText(studentProcessDate != null ? studentProcessDate.format(formatter) : "N/A");
        teacherProcessDateLabel.setText(teacherProcessDate != null ? teacherProcessDate.format(formatter) : "N/A");

        educationGrade = data.get("educationGrade") != null ? (int) data.get("educationGrade") : 0;
        educationTerm = data.get("educationTerm") != null ? (int) data.get("educationTerm") : 0;
        educationYear = data.get("educationYear") != null ? (int) data.get("educationYear") : 0;
        feeAmount = data.get("feeAmount") != null ? (int) data.get("feeAmount") : 0;

        educationGradeLabel.setText(String.valueOf(educationGrade));
        educationTermLabel.setText(String.valueOf(educationTerm));
        educationYearLabel.setText(String.valueOf(educationYear));
        feeAmountLabel.setText(String.valueOf(feeAmount));

        extensionDueDate = (Date) data.get("extensionDueDate");
        if (extensionDueDate != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            extensionDueDateLabel.setText(dateFormat.format(extensionDueDate));
        } else {
            extensionDueDateLabel.setText("N/A");
        }

        optionAddOrDrop = (String) data.get("addOrDrop");
        optionAddOrDropLabel.setText(optionAddOrDrop);

        subject = (String) data.get("subject");
        subjectId = (String) data.get("subjectId");
        degree = (String) data.get("degree");
        curriculum = (String) data.get("curriculum");

        subjectLabel.setText(subject);
        subjectIdLabel.setText(subjectId );
        degreeLabel.setText(degree);
        curriculumLabel.setText(curriculum);

        reason = (String) data.get("reason");
        reasonTextArea.setText(reason);

        // จัดการการแสดงผลตามประเภทคำร้อง
        if (formName.equals("ขอผ่อนผันค่าเทอม")) {
            FeeAndLeaveAnchor.setVisible(true);
            feeReq.setVisible(true);
            addDropReq.setVisible(false);
            leaveReq.setVisible(false);
        } else if (formName.equals("ขอเพิ่ม-ถอนรายวิชา")) {
            addDropReq.setVisible(true);
            FeeAndLeaveAnchor.setVisible(false);
            feeReq.setVisible(false);
            leaveReq.setVisible(false);
        } else if (formName.equals("ขอพักการศึกษา")) {
            FeeAndLeaveAnchor.setVisible(true);
            leaveReq.setVisible(true);
            addDropReq.setVisible(false);
            feeReq.setVisible(false);
        } else {
            FeeAndLeaveAnchor.setVisible(false);
            feeReq.setVisible(false);
            addDropReq.setVisible(false);
            leaveReq.setVisible(false);
        }
    }

    @FXML
    public void onNextButtonClick() {
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("majorIdFromTeacher", majorId);
            data.put("name", nameLabel.getText());
            data.put("username", username);
            data.put("profilePic", profilePic);
            data.put("role", "เจ้าหน้าที่ภาควิชา");
            FXRouter.goTo("manage", data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    private void showErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void onManagePopupButton() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ku/cs/views/departmentApprove.fxml"));
            System.out.println(getClass().getResource("/ku/cs/views/departmentApprove.fxml"));

            AnchorPane popupRoot = fxmlLoader.load();

            DepartmentApprovePopupController approvePopupController = fxmlLoader.getController();

            if (approvePopupController == null) {
                showErrorMessage("Failed to load the popup controller.");
                return;
            }

            approvePopupController.setData(
                    departmentId,
                    name,
                    username,
                    profilePic,
                    role,
                    reqId,
                    reqProcessId,
                    formName
            );


            Stage popupStage = new Stage();
            popupStage.setTitle("ดำเนินการคำร้อง");
            popupStage.initModality(Modality.APPLICATION_MODAL);
            Scene scene = new Scene(popupRoot, 400, 350);
            popupStage.setScene(scene);

            popupStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            showErrorMessage("Error loading the popup: " + e.getMessage());
        }
    }

}

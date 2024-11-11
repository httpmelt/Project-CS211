package ku.cs.controllers.faculty;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ku.cs.services.FXRouter;
import ku.cs.services.faculty.FacultyUserListFileDatasource;
import ku.cs.services.major.MajorUserListFileDatasource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FacultyStaffManagementController {

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
    private Label educationGradeLabel;

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
    private Label formNameLabel;

    @FXML
    private Label majorLabel;

    @FXML
    private Label majorProcessDateLabel;

    @FXML
    private Label nameLabel;

    @FXML
    private Label optionAddOrDropLabel;

    @FXML
    private Label processStatusLabel;

    @FXML
    private TextArea reasonTextArea;

    @FXML
    private Label reqStatusLabel;

    @FXML
    private Label studentIdLabel;

    @FXML
    private Label studentNameLabel;

    @FXML
    private Label studentProcessDateLabel;

    @FXML
    private Label subjectIdLabel;

    @FXML
    private Label subjectLabel;

    @FXML
    private Label teacherProcessDateLabel;

    @FXML
    private Label extensionDueDateLabel;

    @FXML
    private ImageView profilePicLabel;

    private String facultyId, name, username, profilePic, role;
    private String formName, studentId, reqStatus, processStatus, studentName;
    private LocalDateTime studentProcessDate, teacherProcessDate, majorProcessDate;
    private int educationGrade, educationTerm, educationYear, feeAmount, reqId, reqProcessId;
    private Date extensionDueDate;
    private String optionAddOrDrop, subject, subjectId, degree, curriculum, reason, major, faculty;

    @FXML
    public void initialize() {
        Map<String, Object> data = (Map<String, Object>) FXRouter.getData();
        facultyId = (String) data.get("facultyId");
        name = (String) data.get("name");
        username = (String) data.get("username");
        profilePic = (String) data.get("profilePic");
        role = (String) data.get("role");



        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd (HH:mm:ss)");

        reqId = (int) data.get("requestId");
        reqProcessId = (int) data.get("reqProcessId");
        formName = (String) data.get("formName");
        studentId = (String) data.get("studentId");
        studentName = (String) data.get("studentName");
        reqStatus = (String) data.get("reqStatus");
        major = (String) data.get("major");
        faculty = (String) data.get("faculty");
        processStatus = (String) data.get("processStatus");
        studentProcessDate = (LocalDateTime) data.get("studentProcessDate");
        teacherProcessDate = (LocalDateTime) data.get("teacherProcessDate");
        majorProcessDate = (LocalDateTime) data.get("majorProcessDate");
        educationGrade = (int) data.get("educationGrade");
        educationTerm = (int) data.get("educationTerm");
        educationYear = (int) data.get("educationYear");
        feeAmount = (int) data.get("feeAmount");
        extensionDueDate = (Date) data.get("extensionDueDate");
        if (extensionDueDate != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            extensionDueDateLabel.setText(dateFormat.format(extensionDueDate));
        } else {
            extensionDueDateLabel.setText("N/A");
        }
        optionAddOrDrop = (String) data.get("optionAddOrDrop");
        subject = (String) data.get("subject");
        subjectId = (String) data.get("subjectId");
        degree = (String) data.get("degree");
        curriculum = (String) data.get("curriculum");
        reason = (String) data.get("reason");

        nameLabel.setText(name);
        studentIdLabel.setText(studentId);
        studentNameLabel.setText(studentName);
        formNameLabel.setText(formName);
        majorLabel.setText(major);
        facultyLabel.setText(faculty);
        reqStatusLabel.setText(reqStatus);
        processStatusLabel.setText(processStatus);
        studentProcessDateLabel.setText(studentProcessDate != null ? studentProcessDate.format(formatter) : "N/A");
        teacherProcessDateLabel.setText(teacherProcessDate != null ? teacherProcessDate.format(formatter) : "N/A");
        majorProcessDateLabel.setText(majorProcessDate != null ? majorProcessDate.format(formatter) : "N/A");
        educationGradeLabel.setText(String.valueOf(educationGrade));
        educationTermLabel.setText(String.valueOf(educationTerm));
        educationYearLabel.setText(String.valueOf(educationYear));
        feeAmountLabel.setText(String.valueOf(feeAmount));
        optionAddOrDropLabel.setText(optionAddOrDrop);
        subjectLabel.setText(subject);
        subjectIdLabel.setText(subjectId);
        degreeLabel.setText(degree);
        curriculumLabel.setText(curriculum);
        reasonTextArea.setText(reason != null ? reason : "N/A");

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


    @FXML
    public void onBackButton() {
        try {
            Map<String, Object> forFaculty = new HashMap<>();
            forFaculty.put("facultyId", facultyId);
            forFaculty.put("name", name);
            forFaculty.put("username", username);
            forFaculty.put("profilePic", profilePic);
            forFaculty.put("role", "เจ้าหน้าที่คณะ");
            FXRouter.goTo("faculty-staff-show-table", forFaculty);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onManagePopupButton() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ku/cs/views/approvePopup.fxml"));
            AnchorPane popupRoot = fxmlLoader.load();
            ApprovePopupController approvePopupController = fxmlLoader.getController();
            approvePopupController.setData(facultyId, name, username, profilePic, role, reqId, reqProcessId, formName, studentId);
            Stage popupStage = new Stage();
            popupStage.setTitle("ดำเนินการคำร้อง");
            popupStage.initModality(Modality.APPLICATION_MODAL);

            Scene scene = new Scene(popupRoot, 400, 350);
            popupStage.setScene(scene);

            popupStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package ku.cs.controllers.faculty;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import ku.cs.controllers.student.StudentApplicationListController;
import ku.cs.models.student.Student;
import ku.cs.models.student.StudentList;
import ku.cs.models.student.StudentReq;
import ku.cs.models.student.StudentReqList;
import ku.cs.models.user.Staff;
import ku.cs.services.FXRouter;
import ku.cs.services.student.StudentDatasource;
import ku.cs.services.student.StudentReqListFileDatasource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class FacultyStaffManagementTableController {

    @FXML
    private TableView<StudentReq> requestFormTableView;

    @FXML
    private ImageView profilePicLabel;

    @FXML
    private Label nameLabel;

    private StudentDatasource studentDatasource;
    private StudentReqListFileDatasource studentReqDatasource;
    private StudentReqList requestFormList;

    private StudentList studentList;

    private String facultyId, username, name, profilePic, role;

    @FXML
    public void initialize() {
        Map<String, Object> data = (Map<String, Object>) FXRouter.getData();
        name = (String) data.get("name");
        username = (String) data.get("username");
        profilePic = (String) data.get("profilePic");
        facultyId = (String) data.get("facultyId");
        role = (String) data.get("role");

        nameLabel.setText(name);

        setProfilePicture();

        studentDatasource = new StudentDatasource("data/student", "student.csv");
        studentList = studentDatasource.readData();

        requestFormList = loadRequestForms();
        showTable(studentList);

        requestFormTableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                StudentReq selectedRequest = requestFormTableView.getSelectionModel().getSelectedItem();
                if (selectedRequest != null) {
                    goToManagementPage(selectedRequest);
                }
            }
        });
    }

    private void goToManagementPage(StudentReq selectedRequest) {
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("facultyId", facultyId);
            data.put("name", nameLabel.getText());
            data.put("username", username);
            data.put("profilePic", profilePic);
            data.put("role", role);

            data.put("requestId", selectedRequest.getReqId());
            data.put("major", selectedRequest.getMajor());
            data.put("faculty", selectedRequest.getFaculty());
            data.put("formName", selectedRequest.getFormName());
            data.put("studentId", selectedRequest.getStudentId());
            data.put("reqProcessId", selectedRequest.getReqProcessId());
            data.put("reqStatus", selectedRequest.getReqStatus());
            data.put("processStatus", selectedRequest.getProcessStatus());
            data.put("studentProcessDate", selectedRequest.getStudentProcessDate());
            data.put("teacherProcessDate", selectedRequest.getTeacherProcessDate());
            data.put("majorProcessDate", selectedRequest.getMajorProcessDate());
            data.put("facultyProcessDate", selectedRequest.getFacultyProcessDate());
            data.put("educationGrade", selectedRequest.getEducationGrade());
            data.put("educationTerm", selectedRequest.getEducationTerm());
            data.put("educationYear", selectedRequest.getEducationYear());
            data.put("extensionDueDate", selectedRequest.getExtensionDueDate());
            data.put("feeAmount", selectedRequest.getFeeAmount());
            data.put("optionAddOrDrop", selectedRequest.getOptionAddOrDrop());
            data.put("subject", selectedRequest.getSubject());
            data.put("subjectId", selectedRequest.getSubjectId());
            data.put("degree", selectedRequest.getDegree());
            data.put("curriculum", selectedRequest.getCurriculum());
            data.put("reason", selectedRequest.getReason());
            data.put("rejectReason", selectedRequest.getRejectReason());
            data.put("updatedDate", selectedRequest.getUpdatedDate());

            Student student = studentList.findStudentById(selectedRequest.getStudentId());
            if (student != null) {
                String fullName = student.getName() + " " + student.getSurname();
                data.put("studentName", fullName);
            }

            FXRouter.goTo("faculty-staff-management", data);
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR,"เกิดข้อผิดพลาด","ส่งค่าข้อมูลไม่ถูกต้อง");
            e.printStackTrace(); // Optional: Print the stack trace for debugging
        }
    }

    private StudentReqList loadRequestForms() {
        studentReqDatasource = new StudentReqListFileDatasource("data/student", "studentReq.csv");
        return studentReqDatasource.readData();
    }

    private void showTable(StudentList studentList) {
        if (facultyId == null || facultyId.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "เกิดข้อผิดพลาด", "ไม่พบรหัสคณะ");
            return;
        }

        TableColumn<StudentReq, String> reqStatusColumn = new TableColumn<>("สถานะใบคำร้อง");
        reqStatusColumn.setCellValueFactory(new PropertyValueFactory<>("reqStatus"));
        reqStatusColumn.setMinWidth(210);
        reqStatusColumn.setStyle("-fx-font-family: 'Fahkwang'; -fx-font-size: 14px;");

        TableColumn<StudentReq, String> processStatusColumn = new TableColumn<>("ขั้นตอนการดำเนินการ");
        processStatusColumn.setCellValueFactory(new PropertyValueFactory<>("processStatus"));
        processStatusColumn.setMinWidth(250);
        processStatusColumn.setStyle("-fx-font-family: 'Fahkwang'; -fx-font-size: 14px;");

        TableColumn<StudentReq, String> formNameColumn = new TableColumn<>("ประเภทใบคำร้อง");
        formNameColumn.setCellValueFactory(new PropertyValueFactory<>("formName"));
        formNameColumn.setStyle("-fx-font-family: 'Fahkwang'; -fx-font-size: 14px;");
        formNameColumn.setMinWidth(170);

        TableColumn<StudentReq, String> studentIdColumn = new TableColumn<>("รหัสนิสิต");
        studentIdColumn.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        studentIdColumn.setStyle("-fx-font-family: 'Fahkwang'; -fx-font-size: 14px;");
        studentIdColumn.setMinWidth(130);

        TableColumn<StudentReq, String> departmentColumn = new TableColumn<>("ภาควิชา");
        departmentColumn.setCellValueFactory(new PropertyValueFactory<>("major"));
        departmentColumn.setStyle("-fx-font-family: 'Fahkwang'; -fx-font-size: 14px;");
        departmentColumn.setMinWidth(130);

        TableColumn<StudentReq, String> nameColumn = new TableColumn<>("ชื่อ-นามสกุลนิสิต");
        nameColumn.setMinWidth(170);
        nameColumn.setCellValueFactory(cellData -> {
            StudentReq req = cellData.getValue();
            Student student = studentList.findStudentById(req.getStudentId());
            if (student != null) {
                return new ReadOnlyStringWrapper(student.getName() + "  " + student.getSurname());
            }
            return new ReadOnlyStringWrapper("");
        });
        nameColumn.setStyle("-fx-font-family: 'Fahkwang'; -fx-font-size: 14px;");


        // Clear any existing columns and add the newly defined ones
        requestFormTableView.getColumns().clear();
        requestFormTableView.getColumns().addAll(reqStatusColumn, processStatusColumn, formNameColumn, studentIdColumn, departmentColumn,nameColumn);

        requestFormTableView.getItems().clear();

        StudentReqList filteredList = new StudentReqList();
        for (StudentReq req : requestFormList.getStudentReqs()) {
            Student student = studentList.findStudentById(req.getStudentId());
            if (student != null) {
                String teacherId = student.getTeacherId();
                if (teacherId.startsWith(facultyId) && req.getReqProcessId() == 5) {
                    filteredList.addRequestForm(req);
                }
            }
        }

        requestFormTableView.getItems().clear();
        requestFormTableView.getItems().addAll(filteredList.getStudentReqs());

        if (filteredList.getStudentReqs().isEmpty()) {
            showAlert(Alert.AlertType.INFORMATION, "รายละเอียด", "ไม่มีคำร้องที่ต้องอนุมัติ");
        }

        requestFormTableView.sort();

    }


    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
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
    public void onLogoutButtonClick() {
        try {
            FXRouter.goTo("homepage");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onFacultyStaffShowTableButtonClick() {
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
    }

    @FXML
    public void onFacultyStaffApproverButtonClick() {
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("name", name);
            data.put("username", username);
            data.put("profilePic", profilePic);
            data.put("role", "เจ้าหน้าที่คณะ");
            data.put("facultyId", facultyId);
            FXRouter.goTo("faculty-staff-approver", data);
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

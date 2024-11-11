package ku.cs.controllers.departmentStaff;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import ku.cs.models.student.Student;
import ku.cs.models.student.StudentList;
import ku.cs.models.student.StudentReq;
import ku.cs.models.student.StudentReqList;
import ku.cs.services.FXRouter;
import ku.cs.services.student.StudentDatasource;
import ku.cs.services.student.StudentReqListFileDatasource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class DepartmentStaffManagementTableController {

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

    private String departmentId, username, name, profilePic, role , majorIdFromTeacher, majorId;

    @FXML
    public void initialize() {
        Map<String, Object> data = (Map<String, Object>) FXRouter.getData();

        if (data == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "No data received.");
            return;
        }

        majorIdFromTeacher = (String) data.get("majorIdFromTeacher");
        name = (String) data.get("name");
        username = (String) data.get("username");
        profilePic = (String) data.get("profilePic");
        role = (String) data.get("role");

        nameLabel.setText(name);
        setProfilePicture();

        studentDatasource = new StudentDatasource("data/student", "student.csv");
        studentList = studentDatasource.readData();

        requestFormList = loadRequestForms();
        showTable(studentList);
    }

    private void showTable(StudentList studentList) {
        if (majorIdFromTeacher == null || majorIdFromTeacher.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Major ID not found.");
            return;
        }

        TableColumn<StudentReq, String> reqStatusColumn = new TableColumn<>("สถานะใบคำร้อง");
        reqStatusColumn.setCellValueFactory(new PropertyValueFactory<>("reqStatus"));
        reqStatusColumn.setMinWidth(210);

        TableColumn<StudentReq, String> processStatusColumn = new TableColumn<>("ขั้นตอนการดำเนินการ");
        processStatusColumn.setCellValueFactory(new PropertyValueFactory<>("processStatus"));
        processStatusColumn.setMinWidth(250);

        TableColumn<StudentReq, String> formNameColumn = new TableColumn<>("ประเภทใบคำร้อง");
        formNameColumn.setCellValueFactory(new PropertyValueFactory<>("formName"));
        formNameColumn.setMinWidth(170);

        TableColumn<StudentReq, String> studentIdColumn = new TableColumn<>("รหัสนิสิต");
        studentIdColumn.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        studentIdColumn.setMinWidth(130);

        TableColumn<StudentReq, String> nameColumn = new TableColumn<>("ชื่อ-นามสกุลนิสิต");
        nameColumn.setMinWidth(170);
        nameColumn.setCellValueFactory(cellData -> {
            StudentReq req = cellData.getValue();
            Student student = studentList.findStudentById(req.getStudentId());
            if (student != null) {
                return new ReadOnlyStringWrapper(student.getName() + " " + student.getSurname());
            }
            return new ReadOnlyStringWrapper("");
        });

        requestFormTableView.getColumns().clear();
        requestFormTableView.getColumns().addAll(reqStatusColumn, processStatusColumn, formNameColumn, studentIdColumn, nameColumn);

        for (StudentReq studentReq : requestFormList.getStudentReqs()) {
            Student student = studentList.findStudentById(studentReq.getStudentId());
            if (student != null && student.getTeacherId().startsWith(majorIdFromTeacher)) {
                requestFormTableView.getItems().add(studentReq);
            }
        }

        if (requestFormTableView.getItems().isEmpty()) {
            showAlert(Alert.AlertType.INFORMATION, "Information", "No requests found for this department.");
        }

        requestFormTableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Check for double click
                StudentReq selectedRequest = requestFormTableView.getSelectionModel().getSelectedItem();
                if (selectedRequest != null) {
                    goToManagementPage(selectedRequest);
                }
            }
        });
    }

    @FXML
    private void goToManagementPage(StudentReq selectedRequest) {
        try {
            Map<String, Object> data = new HashMap<>();

            data.put("name", name);
            data.put("username", username);
            data.put("profilePic", profilePic);
            data.put("role", role);


            data.put("requestId", selectedRequest.getReqId());
            data.put("formName", selectedRequest.getFormName());
            data.put("studentId", selectedRequest.getStudentId());
            data.put("reqProcessId", selectedRequest.getReqProcessId());
            data.put("reqStatus", selectedRequest.getReqStatus());
            data.put("processStatus", selectedRequest.getProcessStatus());
            data.put("studentProcessDate", selectedRequest.getStudentProcessDate());
            data.put("teacherProcessDate", selectedRequest.getTeacherProcessDate());
            data.put("facultyProcessDate", selectedRequest.getFacultyProcessDate());
            data.put("educationGrade", selectedRequest.getEducationGrade());
            data.put("educationTerm", selectedRequest.getEducationTerm());
            data.put("educationYear", selectedRequest.getEducationYear());
            data.put("extensionDueDate", selectedRequest.getExtensionDueDate());
            data.put("subject", selectedRequest.getSubject());
            data.put("subjectId", selectedRequest.getSubjectId());
            data.put("reason", selectedRequest.getReason());
            data.put("addOrDrop", selectedRequest.getOptionAddOrDrop());
            data.put("degree", selectedRequest.getDegree());
            data.put("curriculum", selectedRequest.getCurriculum());

            data.put("majorId", majorIdFromTeacher);
            System.out.println("Major ID being sent: " + majorIdFromTeacher);


            Student student = studentList.findStudentById(selectedRequest.getStudentId());
            if (student != null) {
                String fullName = student.getName() + " " + student.getSurname();
                data.put("studentName", fullName);  // Add student's full name to data
            }

            System.out.println("Data being sent to DepartmentStaffManagement:");
            data.forEach((key, value) -> System.out.println(key + ": " + value));

            FXRouter.goTo("DepartmentStaffManagement", data);
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to pass the data.");
            e.printStackTrace();
        }
    }

    private StudentReqList loadRequestForms() {
        studentReqDatasource = new StudentReqListFileDatasource("data/student", "studentReq.csv");
        return studentReqDatasource.readData();
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
            // หากผู้ใช้มีรูปโปรไฟล์ ให้แสดงรูปนั้น
            Path profilePicPath = Paths.get("data/profile-pic/", profilePic);
            File profilePicFile = profilePicPath.toFile();
            if (profilePicFile.exists()) {
                Image userImage = new Image(profilePicFile.toURI().toString());
                profilePicLabel.setImage(userImage);
            } else {
                // หากหาไฟล์รูปโปรไฟล์ไม่เจอ ให้ใช้ default แทน
                Image defaultPic = new Image(getClass().getResource("data/profile-pic/default-profile-pic.png").toString());
                profilePicLabel.setImage(defaultPic);
            }
        } else {
            // หากผู้ใช้ไม่มีรูปโปรไฟล์ ให้ใช้รูป default
            Image defaultPic = new Image(getClass().getResource("data/profile-pic/default-profile-pic.png").toString());
            profilePicLabel.setImage(defaultPic);
        }
    }


    @FXML
    public void nextButtonClick(){
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("name", name);
            data.put("username", username);
            data.put("profilePic", profilePic);
            data.put("role", "เจ้าหน้าที่ภาควิชา");
            data.put("majorIdFromTeacher", majorIdFromTeacher);
            FXRouter.goTo("edit-name-list", data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    public void goNextClick(){
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("name", name);
            data.put("username", username);
            data.put("profilePic", profilePic);
            data.put("role", "เจ้าหน้าที่ภาควิชา");
            data.put("majorId", majorIdFromTeacher);
            FXRouter.goTo("faculty-staff-list", data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }





    @FXML
    public void goingToNextClick(){
        try {
            Map<String, Object> forDepartment = new HashMap<>();
            forDepartment.put("majorIdFromTeacher", majorId);
            forDepartment.put("name", nameLabel.getText());
            forDepartment.put("username", username);
            forDepartment.put("profilePic", profilePic);
            forDepartment.put("role", "เจ้าหน้าที่ภาควิชา");
            FXRouter.goTo("manage-student", forDepartment);
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
    void goOptionButton() {
        try {
            Map<String, Object> forOption = new HashMap<>();
            forOption.put("username", username);
            forOption.put("name", name);
            forOption.put("profilePic", profilePic);
            forOption.put("role", role);
            FXRouter.goTo("change-password", forOption);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

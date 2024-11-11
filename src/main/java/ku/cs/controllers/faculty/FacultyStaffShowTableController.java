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
import ku.cs.models.student.StudentList;
import ku.cs.models.student.StudentReq;
import ku.cs.models.student.StudentReqList;
import ku.cs.models.user.FacultyUser;
import ku.cs.services.FXRouter;
import ku.cs.services.faculty.FacultyUserListFileDatasource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ku.cs.models.student.Student;
import ku.cs.services.major.MajorUserListFileDatasource;
import ku.cs.services.student.StudentDatasource;
import ku.cs.services.student.StudentReqListFileDatasource;

public class FacultyStaffShowTableController {

    @FXML
    private TableView<StudentReq> requestFormTableView;

    @FXML
    private ImageView profilePicLabel;

    @FXML
    private Label nameLabel;

    private StudentReqList requestFormList;
    private String facultyId, username, name, profilePic, role, facultyIdFromTeacher;

    private StudentDatasource studentDatasource;
    private StudentReqListFileDatasource studentReqDatasource;


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
        StudentList studentList = studentDatasource.readData();

        requestFormList = loadRequestForms();
        showTable(studentList);
    }

    private StudentReqList loadRequestForms() {
        studentReqDatasource = new StudentReqListFileDatasource("data/student", "studentReq.csv");
        return studentReqDatasource.readData();
    }

    private void showTable(StudentList studentList) {

        if (facultyId == null || facultyId.isEmpty()) {
            showErrorMessage("Faculty ID is not set.");
            return;
        }

        TableColumn<StudentReq, String> dateColumn = new TableColumn<>("วันที่ดำเนินการล่าสุด");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("updatedDate"));
        dateColumn.setStyle("-fx-font-family: 'Fahkwang'; -fx-font-size: 14px;");
        dateColumn.setMinWidth(230);


        TableColumn<StudentReq, String> formNameColumn = new TableColumn<>("ประเภทใบคำร้อง");
        formNameColumn.setCellValueFactory(new PropertyValueFactory<>("formName"));
        formNameColumn.setStyle("-fx-font-family: 'Fahkwang'; -fx-font-size: 14px;");
        formNameColumn.setMinWidth(170);

        TableColumn<StudentReq, String> departmentColumn = new TableColumn<>("ภาควิชา");
        departmentColumn.setCellValueFactory(new PropertyValueFactory<>("major"));
        departmentColumn.setStyle("-fx-font-family: 'Fahkwang'; -fx-font-size: 14px;");
        departmentColumn.setMinWidth(170);

        TableColumn<StudentReq, String> studentIdColumn = new TableColumn<>("รหัสนิสิต");
        studentIdColumn.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        studentIdColumn.setStyle("-fx-font-family: 'Fahkwang'; -fx-font-size: 14px;");
        studentIdColumn.setMinWidth(130);


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

        TableColumn<StudentReq, String> reqStatusColumn = new TableColumn<>("สถานะใบคำร้อง");
        reqStatusColumn.setCellValueFactory(new PropertyValueFactory<>("reqStatus"));
        reqStatusColumn.setMinWidth(210);
        reqStatusColumn.setStyle("-fx-font-family: 'Fahkwang'; -fx-font-size: 14px;");

        TableColumn<StudentReq, String> processStatusColumn = new TableColumn<>("ขั้นตอนการดำเนินการ");
        processStatusColumn.setCellValueFactory(new PropertyValueFactory<>("processStatus"));
        processStatusColumn.setMinWidth(250);
        processStatusColumn.setStyle("-fx-font-family: 'Fahkwang'; -fx-font-size: 14px;");


        requestFormTableView.getColumns().clear();
        requestFormTableView.getColumns().addAll(dateColumn, formNameColumn, studentIdColumn,departmentColumn,nameColumn , reqStatusColumn, processStatusColumn);

        StudentReqList filteredList = new StudentReqList();

        // Map studentId with teacherId from student.csv
        for (StudentReq req : requestFormList.getStudentReqs()) {
            String studentId = req.getStudentId();

            Student student = studentList.findStudentById(studentId);
            if (student == null) {
                continue;
            }

            String teacherId = student.getTeacherId();
            facultyIdFromTeacher = teacherId.substring(0, 1);


            if (facultyIdFromTeacher.equals(facultyId)) {
                filteredList.addRequestForm(req);
            }

        }

        requestFormTableView.getItems().clear();
        requestFormTableView.getItems().addAll(filteredList.getStudentReqs());

        dateColumn.setSortType(TableColumn.SortType.DESCENDING);
        requestFormTableView.getSortOrder().add(dateColumn);

        if (filteredList.getStudentReqs().isEmpty()) {
            showErrorMessage("ไม่พบคำร้องในคณะของคุณ");
        }
    }

    private void showErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
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
    public void onFacultyStaffApproverButtonClick() {
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("name", name);
            data.put("username", username);
            data.put("profilePic", profilePic);
            data.put("role", "เจ้าหน้าที่คณะ");
            data.put("facultyId", facultyId);
            data.put("facultyIdFromTeacher", facultyIdFromTeacher);
            FXRouter.goTo("faculty-staff-approver", data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onFacultyStaffManageTableButtonClick() {
        try {
            Map<String, Object> forFaculty = new HashMap<>();
            forFaculty.put("facultyId", facultyId);
            forFaculty.put("name", nameLabel.getText());
            forFaculty.put("username", username);
            forFaculty.put("profilePic", profilePic);
            forFaculty.put("role", role);
            forFaculty.put("facultyIdFromTeacher", facultyIdFromTeacher);
            FXRouter.goTo("faculty-staff-management-table", forFaculty);
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

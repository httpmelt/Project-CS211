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
import ku.cs.models.student.StudentList;
import ku.cs.models.student.StudentReq;
import ku.cs.models.student.StudentReqList;
import ku.cs.models.user.FacultyUser;
import ku.cs.services.FXRouter;
import ku.cs.services.department.DepartmentUserListFileDatasource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import ku.cs.models.student.Student;
import ku.cs.services.student.StudentDatasource;
import ku.cs.services.student.StudentReqListFileDatasource;
import ku.cs.services.major.MajorUserListFileDatasource;

public class FacultyStaffListController {

    @FXML
    private TableView<StudentReq> requestFormTableView;

    @FXML
    private ImageView profilePicLabel;

    @FXML
    private Label nameLabel;

    private StudentReqList requestFormList;
    private FacultyUser departmentUser;

    private String majorId, username, name, profilePic, role;
    private Map<String, String> majorIdToNameMap;

    private StudentDatasource studentDatasource;
    private StudentReqListFileDatasource studentReqDatasource;

    public void initialize() {
        Map<String, Object> data = (Map<String, Object>) FXRouter.getData();
        name = (String) data.get("name");
        username = (String) data.get("username");
        profilePic = (String) data.get("profilePic");
        majorId = (String) data.get("majorId");
        role = (String) data.get("role");


        nameLabel.setText(name);
        setProfilePicture();

        // โหลดข้อมูล majorId และชื่อสาขา
        majorIdToNameMap = loadMajorIdToNameMap();

        studentDatasource = new StudentDatasource("data/student", "student.csv");
        StudentList studentList = studentDatasource.readData();

        requestFormList = loadRequestForms();
        showTable(studentList);
    }

    // โหลดข้อมูล majorId และชื่อสาขาจากไฟล์ CSV
    private Map<String, String> loadMajorIdToNameMap() {
        MajorUserListFileDatasource majorDatasource = new MajorUserListFileDatasource("data/user", "department.csv");
        return majorDatasource.readMajorIdToNameMap();
    }

    private StudentReqList loadRequestForms() {
        studentReqDatasource = new StudentReqListFileDatasource("data/student", "studentReq.csv");
        return studentReqDatasource.readData();
    }

    private void showTable(StudentList studentList) {
        if (majorId == null || majorId.isEmpty()) {
            showErrorMessage("Major ID is not set.");
            return;
        }

        // แปลง majorId เป็นชื่อสาขาที่ตรงกัน
        String majorName = majorIdToNameMap.get(majorId);
        if (majorName == null) {
            showErrorMessage("Major Name not found for Major ID: " + majorId);
            return;
        }


        // สร้างคอลัมน์ของตาราง
        TableColumn<StudentReq, String> formNameColumn = new TableColumn<>("ประเภทใบคำร้อง");
        formNameColumn.setCellValueFactory(new PropertyValueFactory<>("formName"));
        formNameColumn.setStyle("-fx-font-family: 'Fahkwang'; -fx-font-size: 14px;");
        formNameColumn.setMinWidth(170);

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
        requestFormTableView.getColumns().addAll(formNameColumn, studentIdColumn, nameColumn, reqStatusColumn, processStatusColumn);

        // กรองคำร้องตามชื่อสาขา
        StudentReqList filteredList = new StudentReqList();
        for (StudentReq req : requestFormList.getStudentReqs()) {
            String requestMajorName = req.getMajor(); // Assuming majorName is stored in the request CSV

            if (majorName.equals(requestMajorName)) {
                filteredList.addRequestForm(req);
            }
        }

        requestFormTableView.getItems().clear();
        requestFormTableView.getItems().addAll(filteredList.getStudentReqs());

        if (filteredList.getStudentReqs().isEmpty()) {
            showErrorMessage("ไม่พบคำร้องในภาควิชาของคุณ");
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
    public void nextButtonClick() {
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("name", name);
            data.put("username", username);
            data.put("profilePic", profilePic);
            data.put("role", "เจ้าหน้าที่ภาควิชา");
            data.put("majorIdFromTeacher", majorId);
            FXRouter.goTo("edit-name-list", data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onNextButtonClick() {
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("name", name);
            data.put("username", username);
            data.put("profilePic", profilePic);
            data.put("majorIdFromTeacher", majorId);
            FXRouter.goTo("manage", data);
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


    @FXML
    public void goingToNextClick() {
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("name", name);
            data.put("username", username);
            data.put("profilePic", profilePic);
            data.put("role", "เจ้าหน้าที่ภาควิชา");
            data.put("majorIdFromTeacher", majorId);
            FXRouter.goTo("manage-student",data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

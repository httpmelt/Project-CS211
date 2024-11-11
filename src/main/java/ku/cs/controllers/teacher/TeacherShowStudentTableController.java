package ku.cs.controllers.teacher;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import ku.cs.models.student.*;
import ku.cs.models.teacher.Teacher;
import ku.cs.models.teacher.TeacherList;
import ku.cs.services.Datasource;
import ku.cs.services.FXRouter;
import ku.cs.services.StaffListFileDatasource;
import ku.cs.services.student.StudentDatasource;
import ku.cs.services.student.StudentRegisterDatasource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;


public class TeacherShowStudentTableController {

    // Convert StudentReqList to ObservableList
    ObservableList<Student> observableList;


    @FXML
    private ImageView profilePicLabel;

    @FXML
    private Label nameLabel;

    @FXML
    private TextField searchField;

    private String name, username, role, teacherId, profilePic;


    @FXML
    public void onToStudentTableListButtonClick() {
        try {Map<String, Object> data = new HashMap<>();
            data.put("username", username);
            data.put("name", name);
            data.put("profilePic", profilePic);
            data.put("role", role);
            data.put("teacherId", teacherId);
            FXRouter.goTo("Teacher_StudentTableList",data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onToCheckStudentApplicationHistoryButtonClick() {
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("username", username);
            data.put("name", name);
            data.put("profilePic", profilePic);
            data.put("role", role);
            data.put("teacherId", teacherId);
            FXRouter.goTo("TeacherManagement", data);
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
            forOption.put("role", role);
            FXRouter.goTo("change-password", forOption);
        } catch (IOException e) {
            throw new RuntimeException(e);
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
    public void initialize() {
        Map<String, Object> data = (Map<String, Object>) FXRouter.getData();
        name = (String) data.get("name");
        username = (String) data.get("username");
        profilePic = (String) data.get("profilePic");
        teacherId = (String) data.get("teacherId");
        role = (String) data.get("role");

        setProfilePicture();

       nameLabel.setText(name);

        showTable(teacherId);

        studentListTableView.setOnMouseClicked(event -> {
            String parameters = new String();
            if (event.getClickCount() == 2) {
                Student selectedItem = studentListTableView.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    try {
                        Map<String, Object> forTracking = new HashMap<>();
                        forTracking.put("username", username);
                        forTracking.put("name", name);
                        forTracking.put("profilePic", profilePic);
                        forTracking.put("role", role);
                        forTracking.put("teacherId", teacherId);
                        forTracking.put("studentId", selectedItem.getStudentId());
                        System.out.println(selectedItem.getStudentId());
                        FXRouter.goTo("TeacherApplicationTracking", forTracking);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        FilteredList<Student> filteredData = new FilteredList<>(observableList, b -> true);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(student -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (student.getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (student.getSurname().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (student.getStudentId().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });
        studentListTableView.setItems(filteredData);
    }

    @FXML
    private TableView<Student> studentListTableView;

    private StudentList studentList;

    private Datasource<StudentList> datasource;

    private void showTable(String teacherId) {

        // โหลดข้อมูลจากไฟล์ CSV
        datasource = new StudentDatasource("data/student", "student.csv");
        studentList = datasource.readData();

        StudentList studentListSearch = new StudentList();

        TableColumn<Student, String> studentIdColumn = new TableColumn<>("รหัสนิสิต");
        studentIdColumn.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        studentIdColumn.setPrefWidth(135);
        studentIdColumn.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 14px;");

        TableColumn<Student, String> nameColumn = new TableColumn<>("ชื่อ");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setPrefWidth(135);
        nameColumn.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 14px;");

        TableColumn<Student, String> surnameColumn = new TableColumn<>("นามสกุล");
        surnameColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));
        surnameColumn.setPrefWidth(139);
        surnameColumn.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 14px;");

        studentListTableView.getColumns().clear();
        studentListTableView.getColumns().add(studentIdColumn);
        studentListTableView.getColumns().add(nameColumn);
        studentListTableView.getColumns().add(surnameColumn);

        studentListTableView.getItems().clear();

        for (Student student : studentList.getStudents()) {
            if (student.getTeacherId().equals(teacherId)) {
               studentListTableView.getItems().add(student);
               studentListSearch.addNewStudent(student.getStudentId(), student.getName(), student.getSurname(), student.getEmail(), student.getTeacherId());
            }
        }
        observableList = FXCollections.observableArrayList(studentListSearch.getStudents());
    }

}

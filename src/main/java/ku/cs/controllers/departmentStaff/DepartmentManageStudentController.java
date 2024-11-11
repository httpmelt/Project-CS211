package ku.cs.controllers.departmentStaff;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ku.cs.models.student.Student;
import ku.cs.services.FXRouter;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class DepartmentManageStudentController {

    @FXML
    private ListView<Student> studentListView;
    @FXML
    private Label teacherIdLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label nameLabel;
    @FXML
    private Label lastnameLabel;
    @FXML
    private Label idLabel;

    private ObservableList<Student> studentList = FXCollections.observableArrayList();
    private final String FILE_PATH = "data/student/student.csv";
    private String majorId, username, name, profilePic, role, majorIdFromTeacher, departmentId;

    @FXML
    public void initialize() {
        Object receivedObject = FXRouter.getData();
        if (receivedObject instanceof Map) {
            Map<String, Object> data = (Map<String, Object>) receivedObject;
            Student student = (Student) data.get("student");
            Boolean isNew = (Boolean) data.get("isNew");

            if (student != null) {
                if (isNew) {
                    studentList.add(student);
                } else {
                    int index = (int) data.get("index");
                    studentList.set(index, student);
                }

                studentListView.refresh();
                saveDataToFile();
            }

            name = (String) data.get("name");
            username = (String) data.get("username");
            profilePic = (String) data.get("profilePic");
            majorId = (String) data.get("majorId");
            role = (String) data.get("role");
            majorIdFromTeacher = (String) data.get("majorIdFromTeacher");
            departmentId = (String) data.get("departmentId");
        }

        studentListView.setItems(studentList);
        loadDataFromFile();

        studentListView.setCellFactory(lv -> new ListCell<Student>() {
            @Override
            protected void updateItem(Student student, boolean empty) {
                super.updateItem(student, empty);
                if (empty || student == null) {
                    setText(null);
                } else {
                    setText(student.getStudentId() + " - " + student.getName());
                }
            }
        });

        studentListView.setOnMouseClicked(event -> showStudentDetails());
    }

    private void saveDataToFile() {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(FILE_PATH), StandardCharsets.UTF_8))) {
            for (Student student : studentList) {
                writer.write(student.getStudentId() + "," +
                        student.getName() + "," +
                        student.getSurname() + "," +
                        student.getEmail() + "," +
                        student.getTeacherId());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showStudentDetails() {
        Student selectedStudent = studentListView.getSelectionModel().getSelectedItem();
        if (selectedStudent != null) {
            nameLabel.setText(selectedStudent.getName());
            lastnameLabel.setText(selectedStudent.getSurname());
            emailLabel.setText(selectedStudent.getEmail());
            idLabel.setText(selectedStudent.getStudentId());
            teacherIdLabel.setText(selectedStudent.getTeacherId());
        } else {
            clearLabels();
        }
    }

    private void clearLabels() {
        nameLabel.setText("");
        lastnameLabel.setText("");
        emailLabel.setText("");
        idLabel.setText("");
        teacherIdLabel.setText("");
    }

    private void loadDataFromFile() {
        File file = new File(FILE_PATH);
        if (file.exists()) {
            try (Scanner scanner = new Scanner(file, StandardCharsets.UTF_8)) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    String[] data = line.split(",");
                    if (data.length == 5) {
                        Student student = new Student(data[0], data[1], data[2], data[3], data[4]);
                        studentList.add(student);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleAddButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ku/cs/views/addpopup.fxml"));
            AnchorPane pane = loader.load();

            DepartmentAddStudentController controller = loader.getController();
            controller.setParentController(this);

            Stage stage = new Stage();
            stage.setTitle("Add Student");
            stage.initModality(Modality.APPLICATION_MODAL);
            Scene scene = new Scene(pane);
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleEditButton() {
        Student selectedStudent = studentListView.getSelectionModel().getSelectedItem();
        int selectedIndex = studentListView.getSelectionModel().getSelectedIndex();

        if (selectedStudent != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ku/cs/views/editpopup.fxml"));
                AnchorPane pane = loader.load();

                EditInfoStudentController controller = loader.getController();
                controller.setData(this, selectedStudent, selectedIndex);

                Stage stage = new Stage();
                stage.setTitle("Edit Student");
                stage.initModality(Modality.APPLICATION_MODAL);
                Scene scene = new Scene(pane);
                stage.setScene(scene);
                stage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void addStudentToList(Student student) {
        studentList.add(student);
        studentListView.refresh();
        saveDataToFile();
    }

    public void updateStudentInList(Student student, int index) {
        studentList.set(index, student);
        studentListView.refresh();
        saveDataToFile();
    }


    @FXML
    public void onNextButtonClick() {
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("name", name);
            data.put("username", username);
            data.put("profilePic", profilePic);
            data.put("majorIdFromTeacher", majorIdFromTeacher);
            FXRouter.goTo("manage", data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void nextButtonClick() {
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("name", name);
            data.put("username", username);
            data.put("profilePic", profilePic);
            data.put("majorIdFromTeacher", majorIdFromTeacher);
            FXRouter.goTo("name-list", data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void goNextClick() {
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("name", name);
            data.put("username", username);
            data.put("profilePic", profilePic);
            data.put("majorId", majorIdFromTeacher);
            FXRouter.goTo("faculty-staff-list", data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void goingToNextClick() {
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("name", name);
            data.put("username", username);
            data.put("profilePic", profilePic);
            data.put("majorIdFromTeacher", majorIdFromTeacher);
            FXRouter.goTo("department-manage-student", data);
        } catch (IOException e) {
            e.printStackTrace();
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


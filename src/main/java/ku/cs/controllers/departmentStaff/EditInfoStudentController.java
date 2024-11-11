package ku.cs.controllers.departmentStaff;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ku.cs.models.student.Student;

public class EditInfoStudentController {

    @FXML
    private TextField studentIdField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField surnameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField teacherIdField;

    private DepartmentManageStudentController parentController;
    private Student currentStudent;
    private int studentIndex;

    public void setData(DepartmentManageStudentController controller, Student student, int index) {
        this.parentController = controller;
        this.currentStudent = student;
        this.studentIndex = index;

        studentIdField.setText(student.getStudentId());
        nameField.setText(student.getName());
        surnameField.setText(student.getSurname());
        emailField.setText(student.getEmail());
        teacherIdField.setText(student.getTeacherId());
    }

    @FXML
    public void handleEditButton() {
        String studentId = studentIdField.getText().trim();
        String name = nameField.getText().trim();
        String surname = surnameField.getText().trim();
        String email = emailField.getText().trim();
        String teacherId = teacherIdField.getText().trim();

        if (studentId.isEmpty() || name.isEmpty() || surname.isEmpty() || email.isEmpty() || teacherId.isEmpty()) {
            showAlert("Warning", "Please fill all fields", Alert.AlertType.WARNING);
            return;
        }

        Student updatedStudent = new Student(studentId, name, surname, email, teacherId);

        if (parentController != null) {
            parentController.updateStudentInList(updatedStudent, studentIndex);
        }

        closePopup();
    }

    private void closePopup() {
        Stage stage = (Stage) studentIdField.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

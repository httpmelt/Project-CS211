package ku.cs.controllers.teacher;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import ku.cs.controllers.student.StudentApplicationListController;
import ku.cs.models.student.Student;
import ku.cs.models.student.StudentList;
import ku.cs.models.student.StudentReq;
import ku.cs.services.FXRouter;
import ku.cs.services.student.StudentDatasource;

import java.io.IOException;

public class TeacherApplicationFormTrackingTableController extends StudentApplicationListController {
    private String studentId;
    @FXML
    private Label info;

    @FXML
    private TableView<StudentReq> studentApplicationFormTableView;

    @FXML
    public void teacherBackPage() {
        try {
            FXRouter.goTo("Teacher_StudentTableList");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void initialize() {
        String parameters = FXRouter.getData().toString();
        String[] params = parameters.split(",");

       String notSub_studentId = params[0];
       String teacherId = params[2];

       studentId = notSub_studentId.substring(11,21);

        showStudentInfo(studentId);
        showStudentReqList(studentApplicationFormTableView, studentId);

        studentApplicationFormTableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                StudentReq selectedItem = studentApplicationFormTableView.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    try {
                        FXRouter.goTo("ReqFormDetail", String.join(",", "TeacherApplicationTracking", String.valueOf(selectedItem.getReqId())));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    private void showStudentInfo(String studentId) {

        StudentDatasource datasource = new StudentDatasource("data/student", "student.csv");
        StudentList studentList = datasource.readData();

        for (Student student : studentList.getStudents()) {
            if (student.getStudentId().equals(studentId)) {
                info.setText(": " + student.getName() + " " + student.getSurname() + " " + student.getStudentId());
                break;
            }
        }

    }
}

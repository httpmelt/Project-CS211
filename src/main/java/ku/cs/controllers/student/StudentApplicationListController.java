package ku.cs.controllers.student;

import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import ku.cs.models.student.Student;
import ku.cs.models.student.StudentList;
import ku.cs.models.student.StudentReq;
import ku.cs.models.student.StudentReqList;
import ku.cs.services.Datasource;
import ku.cs.services.student.StudentDatasource;
import ku.cs.services.student.StudentReqListFileDatasource;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;


public class StudentApplicationListController {

   public StudentReqList showStudentReqList = new StudentReqList();

    private void setTableView(TableView<StudentReq> studentApplicationFormTableView) {

        TableColumn<StudentReq, String> formNameColumn = new TableColumn<>("ประเภทใบคำร้อง");
        formNameColumn.setCellValueFactory(new PropertyValueFactory<>("formName"));
        formNameColumn.setStyle("-fx-font-family: 'Fahkwang'; -fx-font-size: 14px;");

        TableColumn<StudentReq, String> statusColumn = new TableColumn<>("สถานะใบคำร้อง");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("reqStatus"));
        statusColumn.setStyle("-fx-font-family: 'Fahkwang'; -fx-font-size: 14px;");

        TableColumn<StudentReq, String> reqDateColumn = new TableColumn<>("ขั้นตอนการดำเนินการ");
        reqDateColumn.setCellValueFactory(new PropertyValueFactory<>("processStatus"));
        reqDateColumn.setStyle("-fx-font-family: 'Fahkwang'; -fx-font-size: 14px;");

        TableColumn<StudentReq, String> dateColumn = new TableColumn<>("วันทีสถานะใบคำร้อง");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("updatedDate"));
        dateColumn.setStyle("-fx-font-family: 'Fahkwang'; -fx-font-size: 14px;");

        // Add all columns to the TableView
        studentApplicationFormTableView.getColumns().clear();
        studentApplicationFormTableView.getColumns().add(formNameColumn);
        studentApplicationFormTableView.getColumns().add(statusColumn);
        studentApplicationFormTableView.getColumns().add(reqDateColumn);
        studentApplicationFormTableView.getColumns().add(dateColumn);

        dateColumn.setSortType(TableColumn.SortType.DESCENDING);
        studentApplicationFormTableView.getSortOrder().add(dateColumn);

        studentApplicationFormTableView.getItems().clear();
    }

    public void showStudentReqList(TableView<StudentReq> studentApplicationFormTableView, String studentId){


        TableColumn<StudentReq, String> formNameColumn = new TableColumn<>("ประเภทใบคำร้อง");
        formNameColumn.setCellValueFactory(new PropertyValueFactory<>("formName"));
        formNameColumn.setStyle("-fx-font-family: 'Fahkwang'; -fx-font-size: 14px;");

        TableColumn<StudentReq, String> statusColumn = new TableColumn<>("สถานะใบคำร้อง");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("reqStatus"));
        statusColumn.setStyle("-fx-font-family: 'Fahkwang'; -fx-font-size: 14px;");

        TableColumn<StudentReq, String> reqDateColumn = new TableColumn<>("ขั้นตอนการดำเนินการ");
        reqDateColumn.setCellValueFactory(new PropertyValueFactory<>("processStatus"));
        reqDateColumn.setStyle("-fx-font-family: 'Fahkwang'; -fx-font-size: 14px;");

        TableColumn<StudentReq, String> dateColumn = new TableColumn<>("วันทีสถานะใบคำร้อง");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("updatedDate"));
        dateColumn.setStyle("-fx-font-family: 'Fahkwang'; -fx-font-size: 14px;");

        // Add all columns to the TableView
        studentApplicationFormTableView.getColumns().clear();
        studentApplicationFormTableView.getColumns().add(formNameColumn);
        studentApplicationFormTableView.getColumns().add(statusColumn);
        studentApplicationFormTableView.getColumns().add(reqDateColumn);
        studentApplicationFormTableView.getColumns().add(dateColumn);

        studentApplicationFormTableView.getItems().clear();

        StudentReqList studentReqList;
        Datasource<StudentReqList> datasource;

        datasource = new StudentReqListFileDatasource("data/student", "studentReq.csv");
        studentReqList = datasource.readData();

        for (StudentReq studentReq : studentReqList.getStudentReqs()) {
            if (studentReq.getStudentId().equals(studentId)) {
                studentApplicationFormTableView.getItems().add(studentReq);
                showStudentReqList.addNewStudentReq(studentReq.getReqId(), studentReq.getReqFormId(), studentReq.getFormName(), studentReq.getStudentId(), studentReq.getReqProcessId(), studentReq.getReqStatus(), studentReq.getProcessStatus(), studentReq.getStudentProcessDate(), studentReq.getTeacherProcessDate(), studentReq.getMajorProcessDate(), studentReq.getFacultyProcessDate(), studentReq.getTeacherId(), studentReq.getFaculty(), studentReq.getMajor(), studentReq.getEducationGrade(), studentReq.getEducationTerm(), studentReq.getEducationYear(), studentReq.getFeeAmount(), studentReq.getExtensionDueDate(), studentReq.getOptionAddOrDrop(), studentReq.getSubject(), studentReq.getSubjectId(), studentReq.getDegree(), studentReq.getCurriculum(), studentReq.getReason(), studentReq.getRejectReason(), studentReq.getUpdatedDate());
            }
        }
        dateColumn.setSortType(TableColumn.SortType.DESCENDING);
        studentApplicationFormTableView.getSortOrder().add(dateColumn);
    }

    public void showStudentReqListByTeacherId(TableView<StudentReq> studentApplicationFormTableView, String teacherId) {
        setTableView(studentApplicationFormTableView);
        StudentReqList studentReqList;
        Datasource<StudentReqList> datasource;

        // Load data from the file
        datasource = new StudentReqListFileDatasource("data/student", "studentReq.csv");
        studentReqList = datasource.readData();

        // Add all StudentReq objects to the TableView
        for (StudentReq studentReq : studentReqList.getStudentReqs()) {
            if (studentReq.getTeacherId().equals(teacherId) && studentReq.getReqProcessId() == 1) {
                studentApplicationFormTableView.getItems().add(studentReq);
            }
        }
        studentApplicationFormTableView.sort();
    }


    public String getStudentTeacherId(String studentId) {

        Datasource datasource = new StudentDatasource("data/student", "student.csv");
        StudentList studentList = (StudentList) datasource.readData();

        for (Student student : studentList.getStudents()) {
            if (student.getStudentId().equals(studentId)) {
                return student.getTeacherId();
            }
        }
        return "";
    }

    public void showStudentReqListByFacultyId(TableView<StudentReq> studentApplicationFormTableView, String studentId) {
        setTableView(studentApplicationFormTableView);
        StudentReqList studentReqList;
        Datasource<StudentReqList> datasource;
        Datasource<StudentReqList> studentDatasource;

        // Load student data to map studentId to teacherId
        studentDatasource = new StudentReqListFileDatasource("data/student", "student.csv");
        studentReqList = studentDatasource.readData();

        // Find the teacherId for the given studentId
        String teacherId = null;
        for (StudentReq studentReq : studentReqList.getStudentReqs()) {
            if (studentReq.getStudentId().equals(studentId)) {
                teacherId = studentReq.getTeacherId();
                break;
            }
        }

        // Extract the facultyId (first character of teacherId)
        if (teacherId != null && teacherId.length() > 0) {
            String facultyId = teacherId.substring(0, 1); // Get the first character

            // Load the request data from the file
            datasource = new StudentReqListFileDatasource("data/student", "studentReq.csv");
            studentReqList = datasource.readData();

            // Add requests with reqProcessId == 5 and matching facultyId to the TableView
            for (StudentReq studentReq : studentReqList.getStudentReqs()) {
                if (studentReq.getReqProcessId() == 5 && studentReq.getTeacherId().startsWith(facultyId)) {
                    studentApplicationFormTableView.getItems().add(studentReq);
                }
            }

            studentApplicationFormTableView.sort();
        } else {
            // Handle the case where the teacherId is not found
            msgBox("Error", "Teacher ID not found for the given student.");
        }
    }

    public void msgBox(String title, String header) {

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        //alert.setContentText(content);
        alert.showAndWait();

    }

}


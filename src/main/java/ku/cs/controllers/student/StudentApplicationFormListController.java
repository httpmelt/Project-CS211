package ku.cs.controllers.student;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import ku.cs.controllers.ApplicationApprovalController;
import ku.cs.models.student.Student;
import ku.cs.models.student.StudentList;
import ku.cs.models.student.StudentReq;
import ku.cs.models.student.StudentReqList;
import ku.cs.services.FXRouter;
import ku.cs.services.Datasource;
import ku.cs.services.student.StudentDatasource;
import ku.cs.services.student.StudentReqListFileDatasource;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZoneId;
import java.util.Date;
import java.time.LocalDateTime;

public class StudentApplicationFormListController extends ApplicationApprovalController {

    private String studentId = new String();
    private String formId = new String();
    private String teacherId = new String();
    private int reqId;
    private String backRoutelabel = new String();


    @FXML
    private Label studentNameLabel, studentSurnameLabel, studentIdLabel, formName, reqStatus, processStatus, faculty, major, role;
    @FXML
    private TextArea reason, rejectReasonTextArea;
    @FXML
    private TextField subject, subjectId, educationYearF001, feeAmount, educationYearF003;
    @FXML
    private DatePicker extensionDueDate;
    @FXML
    private Pane F001, F002, F003, status, approvalPane;
    @FXML
    private Button btSubmit;
    @FXML
    private ChoiceBox<String> educationGrade, optionAddOrDrop, educationTermF001, educationTermF003, degree, curriculum;
    @FXML
    private AnchorPane applicationPane;

    @FXML
    public void initialize() {

        String parameters = FXRouter.getData().toString();
        String[] params = parameters.split(",");
        backRoutelabel = params[0];
        reqId = Integer.valueOf(params[1]);

        btSubmit.setVisible(false);

        if (params.length > 2) {
            studentId = params[2];
            showStudent(studentId);
        }

        if(backRoutelabel.equals("TeacherManagement")){
            role.setText("อาจารย์ที่ปรึกษา");
            role.setLayoutX(680);
            role.setLayoutY(15);
            role.setStyle("-fx-font-family: 'Fahkwang'; -fx-font-size: 12px;");
        }
        if(backRoutelabel.equals("TeacherApplicationTracking")){
            role.setText("อาจารย์ที่ปรึกษา");
            role.setLayoutX(680);
            role.setLayoutY(15);
            role.setStyle("-fx-font-family: 'Fahkwang'; -fx-font-size: 12px;");
        }

        if (reqId == -1) {
            formId = "F001";
            newForm(formId);
        } else if (reqId == -2) {
            formId = "F002";
            newForm(formId);
        } else if (reqId == -3) {
            formId = "F003";
            newForm(formId);
        } else {
            showDetail(reqId);
        }

        showApproval(backRoutelabel);

    }

    @FXML
    public void onToAddEduFeeFormClick() {
        try {
            FXRouter.goTo("EduFeeForm");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onToAddOrDropSubjectFormClick() {
        try {
            FXRouter.goTo("AddOrDropSubjectForm");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onToLeaveOfAbsenceFormClick() {
        try {
            FXRouter.goTo("LeaveOfAbsenceForm");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onNextToCheckStatusClick() {
        navBack(backRoutelabel);
    }

    @FXML
    public void onSubmitButtonClick() {
        updateDetail(formId, studentId);
        navBack(backRoutelabel);
    }

    @FXML
    public void onApproveBtClick() {
        approveSubmit(reqId, 2);
        navBack(backRoutelabel);
    }

    @FXML
    public void onRejectBtClick() {
        if (rejectReasonTextArea.getText().trim().equals("")) {
            msgBox("คำร้องถูกปฏิเสธ", "กรุณาระบุเหตุผลการปฏิเสธ");
        } else {
            rejectSubmit(reqId, 3, rejectReasonTextArea.getText());
            navBack(backRoutelabel);
        }
    }

    private void navBack(String backRoutelabel) {
        try {
            FXRouter.goTo(backRoutelabel);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void newForm(String formId) {
        approvalPane.setVisible(false);
        btSubmit.setVisible(true);

        applicationPane.getChildren().remove(status);
        educationGrade.getItems().addAll("1", "2", "3", "4");
        optionAddOrDrop.getItems().addAll("เพิ่มรายวิชา", "ถอนรายวิชา");
        educationTermF001.getItems().addAll("1", "2");
        educationTermF003.getItems().addAll("1", "2");
        degree.getItems().addAll("ระดับปริญญาตรี", "ระดับปริญญาโท", "ระดับปริญญาเอก");
        curriculum.getItems().addAll("ภาคปกติ", "ภาคพิเศษ", "ภาคอินเตอร์");

        faculty.setText(searchDepartmentInfo(searchTeacherInfo(teacherId, 7),2));
        major.setText(searchDepartmentInfo(searchTeacherInfo(teacherId, 7),1));

        showFormId(formId);
    }

    private void showApproval(String backRoutelabel) {
        if (backRoutelabel.equals("TeacherManagement")) {
            approvalPane.setVisible(true);
        } else {
            approvalPane.setVisible(false);
        }
    }

    private void showDetail(int reqId) {

        Datasource datasource = new StudentReqListFileDatasource("data/student", "studentReq.csv");
        StudentReqList studentReqList = (StudentReqList) datasource.readData();

        for (StudentReq studentReq : studentReqList.getStudentReqs()) {
            if (studentReq.getReqId() == reqId) {
                reason.setText(studentReq.getReason());
                subject.setText(studentReq.getSubject());
                subjectId.setText(studentReq.getSubjectId());
                faculty.setText(studentReq.getFaculty());
                major.setText(studentReq.getMajor());
                educationGrade.setValue(String.valueOf(studentReq.getEducationGrade()));
                optionAddOrDrop.setValue((studentReq.getOptionAddOrDrop()));
                formName.setText(studentReq.getFormName());
                reqStatus.setText(studentReq.getReqStatus());
                processStatus.setText(studentReq.getProcessStatus());
                educationTermF001.setValue(String.valueOf((studentReq.getEducationTerm())));
                educationYearF001.setText(String.valueOf(studentReq.getEducationYear()));
                feeAmount.setText(String.valueOf(studentReq.getFeeAmount()));
                degree.setValue((studentReq.getDegree()));
                curriculum.setValue((studentReq.getCurriculum()));
                educationTermF003.setValue(String.valueOf((studentReq.getEducationTerm())));
                educationYearF003.setText(String.valueOf(studentReq.getEducationYear()));
                extensionDueDate.setValue(studentReq.getExtensionDueDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());

                educationYearF001.setEditable(false);
                feeAmount.setEditable(false);
                extensionDueDate.setEditable(false); //ยังแก้ไม่ได้
                subject.setEditable(false);
                subjectId.setEditable(false);
                educationYearF003.setEditable(false);
                reason.setEditable(false);

                showStudent(studentReq.getStudentId());
                showFormId(studentReq.getReqFormId());

                break;
            }
        }
    }

    private void showFormId(String reqFormId) {

        //  จัดหน้า
        F001.setLayoutX(F002.getLayoutX());
        F001.setLayoutY(F002.getLayoutY());
        F003.setLayoutX(F002.getLayoutX());
        F003.setLayoutY(F002.getLayoutY());

        switch (reqFormId) {
            case "F001":
                formName.setText("ขอผ่อนผันค่าเทอม");
                F001.setVisible(true);
                F002.setVisible(false);
                F003.setVisible(false);
                break; // ออกจาก switch
            case "F002":
                formName.setText("ขอเพิ่ม-ถอนรายวิชา");
                F001.setVisible(false);
                F002.setVisible(true);
                F003.setVisible(false);
                break; // ออกจาก switch
            case "F003":
                formName.setText("ขอพักการศึกษา");
                F001.setVisible(false);
                F002.setVisible(false);
                F003.setVisible(true);
                break; // ออกจาก switch
            default:
                Alert alert = new Alert(Alert.AlertType.INFORMATION);  // ประเภทของ Alert (ข้อมูล, คำเตือน, ข้อผิดพลาด)
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Wrong Form ID");
                alert.showAndWait();
                break; // ออกจาก switch
        }
    }

    //update ของนิสิต
    private void updateDetail(String newFormId, String studentId) {

        Datasource datasource = new StudentReqListFileDatasource("data/student", "studentReq.csv");
        StudentReqList studentReqList = (StudentReqList) datasource.readData();

        int educationTermVal = 1;
        int educationYearVal = 1;
        int educationGradeVal;

        // Get current date and time
        Date extensionDueDate = new Date();
        LocalDateTime studentProcessDate = LocalDateTime.now();
        LocalDateTime teacherProcessDate = LocalDateTime.now();
        LocalDateTime majorProcessDate = LocalDateTime.now();
        LocalDateTime facultyProcessDate = LocalDateTime.now();
        LocalDateTime updatedDate = LocalDateTime.now();
        int reqProcessId = 1;
        String reqStatus = "ใบคำร้องใหม่";
        String processStatus = "คำร้องส่งต่อให้อาจารย์ที่ปรึกษา";
        int reqId = studentReqList.getStudentReqs().size() + 1;
        int feeAmountVal = 0;
        String rejectReason = "";

        educationGradeVal = Integer.parseInt(educationGrade.getValue());

        switch (newFormId) {
            case "F001":
                educationTermVal = Integer.parseInt(educationTermF001.getValue());
                educationYearVal = Integer.parseInt(educationYearF001.getText());
                feeAmountVal = Integer.parseInt(feeAmount.getText());
                break;
            case "F002":
                break;
            case "F003":
                educationTermVal = Integer.parseInt(educationTermF003.getValue());
                educationYearVal = Integer.parseInt(educationYearF003.getText());
                break;
            default:
                break; // ออกจาก switch
        }
        studentReqList.addNewStudentReq(
                reqId,
                newFormId,
                formName.getText(),
                studentId,
                reqProcessId,
                reqStatus,
                processStatus,
                studentProcessDate,
                teacherProcessDate,
                majorProcessDate,
                facultyProcessDate,
                teacherId,
                faculty.getText(),
                major.getText(),
                educationGradeVal,
                educationTermVal,
                educationYearVal,
                feeAmountVal,
                extensionDueDate,
                optionAddOrDrop.getValue(),
                subject.getText(),
                subjectId.getText(),
                degree.getValue(),
                curriculum.getValue(),
                reason.getText(),
                rejectReason,
                updatedDate
        );
        datasource.writeData(studentReqList);
    }

    private void showStudent(String studentId) {

        Datasource datasource = new StudentDatasource("data/student", "student.csv");
        StudentList studentList = (StudentList) datasource.readData();

        for (Student student : studentList.getStudents()) {
            if (student.getStudentId().equals(studentId)) {
                studentNameLabel.setText(student.getName());
                studentSurnameLabel.setText(student.getSurname());
                studentIdLabel.setText(student.getStudentId());
                teacherId = student.getTeacherId();
                break;
            }
        }
    }

    private void msgBox(String title, String header) {

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.showAndWait();

    }

    private String searchDepartmentInfo(String majorId, int index) {
        String csvFile = "data/user/department.csv";
        String searchValue = majorId;
        String line;
        String csvSeparator = ",";

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile, StandardCharsets.UTF_8))) {
            while ((line = br.readLine()) != null) {
                String[] values = line.split(csvSeparator);
                if (values[0].trim().equals(searchValue)) {
                    return values[index];
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "ไม่พบข้อมูลการค้นหาด้วย" + searchValue;
    }

    private String searchTeacherInfo(String teacherId, int index) {
        String csvFile = "data/user/Staffuser.csv";
        String line;
        String csvSeparator = ",";

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile, StandardCharsets.UTF_8))) {
            while ((line = br.readLine()) != null) {
                String[] values = line.split(csvSeparator);
                if (values[8].trim().equals(teacherId)) {
                    return values[index];

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}





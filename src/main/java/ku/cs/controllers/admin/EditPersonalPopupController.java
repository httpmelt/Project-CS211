package ku.cs.controllers.admin;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ku.cs.models.admin.*;
import ku.cs.models.student.Student;
import ku.cs.models.student.StudentList;
import ku.cs.models.student.StudentReq;
import ku.cs.models.student.StudentReqList;
import ku.cs.services.Datasource;
import ku.cs.services.admin.DataListCodeFileDatasource;
import ku.cs.services.admin.SectionListCodeFileDatasource;
import ku.cs.services.admin.SectionTwoListCodeFileDatasource;
import ku.cs.services.student.StudentDatasource;
import ku.cs.services.student.StudentReqListFileDatasource;

public class EditPersonalPopupController {

    @FXML private TextField usernameField;
    @FXML private TextField nameField;
    @FXML private TextField surnameField;
    @FXML private ComboBox<String> majorComboBox;
    @FXML private ComboBox<String> facultyComboBox;
    @FXML private TextField idField;
    @FXML private TextField passwordField;
    @FXML private Label idLabel;
    @FXML private Label majorLabel;
    @FXML private Label roleLabel;
    @FXML private Label NameFacultyLabel;
    @FXML private Label NameMajorLabel;

    private Personal personal;
    private Datasource<PersonalList> datasource;
    private Datasource<DataList> dataListDatasource;
    private Datasource<StudentReqList> dataListStudentDatasource;
    private DataList dataCurrentList;
    private PersonalList currentList;
    private StudentReqList studentReqList;
    private Datasource<StudentList> studentDatasource;
    private StudentList currentStudentList;

    @FXML private Label errorUsernameLabel;
    @FXML private Label errorIdLabel;
    @FXML private Label errorNameLabel;
    @FXML private Label errorSurnameLabel;
    @FXML private Label errorPasswordLabel;

    String oldUsername;
    String oldName;
    String oldSurname;
    String oldTeacherId;

    @FXML
    public void initialize() {
        populateFacultyComboBox();

        dataListDatasource = new DataListCodeFileDatasource("data/user", "Allusers.csv");
        dataCurrentList = dataListDatasource.readData();

        dataListStudentDatasource = new StudentReqListFileDatasource("data/student", "studentReq.csv");
        studentReqList = dataListStudentDatasource.readData();

        studentDatasource = new StudentDatasource("data/student", "student.csv");
        currentStudentList = studentDatasource.readData();

        facultyComboBox.setOnAction(event -> updateMajorComboBox());


        majorComboBox.setOnAction(event -> {
            updateNameMajorLabel();
        });
    }

    private void populateFacultyComboBox() {
        SectionList sectionList = new SectionListCodeFileDatasource("data/user", "faculty.csv").readData();

        facultyComboBox.getItems().clear();
        for (Section section : sectionList.getSections()) {
            if (!section.getFacultyId().equals("facultyId") && !section.getFacultyName().equals("facultyName")) {
                facultyComboBox.getItems().add(section.getFacultyId());
            }
        }

        if (personal != null && personal.getFacultyId() != null) {
            facultyComboBox.setValue(personal.getFacultyId());
        }

        updateMajorComboBox();
        NameMajorLabel.setText(majorComboBox.getValue());
    }

    private void updateMajorComboBox() {
        SectionList sectionList = new SectionListCodeFileDatasource("data/user", "faculty.csv").readData();
        String facultyValue = facultyComboBox.getValue();
        for (Section section : sectionList.getSections()) {
            if (section.getFacultyId().equals(facultyValue)) {
                facultyValue = section.getFacultyName();
                break;
            }
        }
        NameFacultyLabel.setText(facultyValue);
        majorComboBox.getItems().clear();
        NameMajorLabel.setStyle("-fx-text-fill: Black;");
        NameFacultyLabel.setStyle("-fx-text-fill: Black;");
        String firstMajorName = null;

        SectionTwoList sectionTwoList = new SectionTwoListCodeFileDatasource("data/user", "department.csv").readData();
        for (SectionTwo sectionTwo : sectionTwoList.getSectionTwoList()) {
            if (sectionTwo.getFaculty().equals(facultyValue)) {
                majorComboBox.getItems().add(sectionTwo.getMajorId());
                if (firstMajorName == null) {
                    firstMajorName = sectionTwo.getMajor();
                }
            }
        }

        String majorValue = "";
        if (personal != null && personal.getMajorId() != null) {
            for (SectionTwo sectionTwo : sectionTwoList.getSectionTwoList()) {
                if (sectionTwo.getMajorId().equals(personal.getMajorId())) {
                    majorValue = sectionTwo.getMajor();
                    majorComboBox.setValue(sectionTwo.getMajorId());
                    break;
                }
            }

            if (!personal.getRole().equals("เจ้าหน้าที่คณะ")) {
                if (majorComboBox.getValue().equals(personal.getMajorId()) && !facultyComboBox.getValue().equals(personal.getFacultyId())) {
                    if (!majorComboBox.getItems().isEmpty()) {
                        majorComboBox.setValue(majorComboBox.getItems().get(0));
                        majorValue = majorComboBox.getItems().get(0);
                        for (SectionTwo sectionTwo : sectionTwoList.getSectionTwoList()) {
                            if (sectionTwo.getMajorId().equals(majorValue)) {
                                majorValue = sectionTwo.getMajor();
                                break;
                            }
                        }
                    }
                }
            }
        }
        NameMajorLabel.setText(majorValue);
    }

    private void updateNameMajorLabel() {
        String selectedMajorId = majorComboBox.getValue();
        boolean majorExists = false;

        if (selectedMajorId != null) {
            SectionTwoList sectionTwoList = new SectionTwoListCodeFileDatasource("data/user", "department.csv").readData();
            for (SectionTwo sectionTwo : sectionTwoList.getSectionTwoList()) {
                if (sectionTwo.getMajorId().equals(selectedMajorId)) {
                    NameMajorLabel.setText(sectionTwo.getMajor());
                    majorExists = true;
                    break;
                }
            }
        }

        if (!majorExists) {
            NameMajorLabel.setText("Major does not exist.");
        }
    }

    public void setDatasource(Datasource<PersonalList> datasource) {
        this.datasource = datasource;
    }

    public void setCurrentList(PersonalList currentList) {
        SectionList sectionList = new SectionListCodeFileDatasource("data/user", "faculty.csv").readData();
        SectionTwoList sectionTwoList = new SectionTwoListCodeFileDatasource("data", "department.csv").readData();
        boolean FacultyExists = sectionList.getSections().stream().anyMatch(s -> s.getFacultyId().equals(personal.getFacultyId()));
        boolean MajorExists = sectionTwoList.getSectionTwoList().stream().anyMatch(sectionTwo -> sectionTwo.getMajorId().equals(personal.getMajorId()));
        this.currentList = currentList;
        errorSurnameLabel.setText("");
        errorNameLabel.setText("");
        errorIdLabel.setText("");
        errorPasswordLabel.setText("");
        errorUsernameLabel.setText("");
        errorNameLabel.setStyle("-fx-text-fill: red;");
        errorUsernameLabel.setStyle("-fx-text-fill: red;");
        errorPasswordLabel.setStyle("-fx-text-fill: red;");
        errorIdLabel.setStyle("-fx-text-fill: red;");
        errorSurnameLabel.setStyle("-fx-text-fill: red;");

        if (!FacultyExists) {
            NameFacultyLabel.setText("Please select a new Faculty");
            if (!personal.getRole().equals("เจ้าหน้าที่คณะ")) {
                NameMajorLabel.setText("Please select a new Major");
            }
            NameFacultyLabel.setStyle("-fx-text-fill: red;");
            NameMajorLabel.setStyle("-fx-text-fill: red;");
        }

        if (!MajorExists && !FacultyExists) {
            NameMajorLabel.setStyle("-fx-text-fill: red;");
            if (!personal.getRole().equals("เจ้าหน้าที่คณะ")) {
                NameMajorLabel.setText("Please select a new Major");
            }
        }
    }

    public void setPersonal(Personal personal) {
        this.personal = personal;

        usernameField.setText(personal.getUsername());
        nameField.setText(personal.getName());
        surnameField.setText(personal.getSurname());
        passwordField.setText(personal.getPassword());
        roleLabel.setText(personal.getRole());

        oldTeacherId = personal.getTeacherId();
        oldName = personal.getName();
        oldSurname = personal.getSurname();
        oldUsername = personal.getUsername();

        populateFacultyComboBox();


        if (personal.getRole().equals("เจ้าหน้าที่คณะ")) {
            idField.setVisible(false);
            majorComboBox.setVisible(false);
            NameMajorLabel.setVisible(false);
            idLabel.setText("");
            majorLabel.setText("");
            NameMajorLabel.setText("");
        } else {
            idField.setText(personal.getTeacherId());
            majorComboBox.setVisible(true);
            majorLabel.setText("Major:");
            idLabel.setText("Teacher ID:");
        }

        if (personal.getRole().equals("เจ้าหน้าที่ภาควิชา")) {
            idField.setVisible(false);
            idLabel.setText("");
        } else {
            idField.setText(personal.getTeacherId());
        }
    }




    @FXML
    private void handleOkButton() {

        boolean dataUsernameExists = dataCurrentList.getDataList().stream()
                .anyMatch(data -> data.getUsername().equals(usernameField.getText()));
        boolean personalUsernameExists = currentList.getPersonalsList().stream()
                .anyMatch(personal -> personal.getUsername().equals(usernameField.getText()));
        boolean idExists = currentList.getPersonalsList().stream()
                .anyMatch(personal -> personal.getTeacherId().equals(idField.getText()));

        boolean errorPassword = true;

        String newTeacherId = idField.getText();

        errorIdLabel.setText("");
        errorUsernameLabel.setText("");
        errorNameLabel.setText("");
        errorSurnameLabel.setText("");
        errorPasswordLabel.setText("");



        if (usernameField.getText().isEmpty()) {
            errorUsernameLabel.setText("Username cannot be empty.");
        }


        if (nameField.getText().isEmpty()) {
            errorNameLabel.setText("Name cannot be empty.");
        }





        if (surnameField.getText().isEmpty()) {
            errorSurnameLabel.setText("Surname cannot be empty.");
        }


        if (personalUsernameExists && !usernameField.getText().equals(oldUsername)) {
            errorUsernameLabel.setText("Username already exists.");
        }
        if(dataUsernameExists && !usernameField.getText().equals(oldUsername)) {
            errorUsernameLabel.setText("Username already exists.");
        }


        if(idField.getText().isEmpty() && personal.getRole().equals("อาจารย์ที่ปรึกษา")) {
            errorIdLabel.setText("ID cannot be empty.");
        } else if (idExists && !idField.getText().equals(personal.getTeacherId()) && personal.getRole().equals("อาจารย์ที่ปรึกษา")) {
            errorIdLabel.setText("Teacher ID already exists.");
        }

        if (passwordField.getText().isEmpty()) {
            errorPasswordLabel.setText("Password cannot be empty.");
            errorPassword = false;
        }

        if(usernameField.getText().equals(personal.getUsername())) {
            errorUsernameLabel.setText("");
        }

        if(idField.getText().equals(personal.getTeacherId())) {
            errorIdLabel.setText("");
        }

        if(nameField.getText().equals(personal.getTeacherId())) {
            errorNameLabel.setText("");
        }

        if(surnameField.getText().equals(personal.getTeacherId())) {
            errorSurnameLabel.setText("");
        }


        if(passwordField.getText().equals(personal.getPassword())) {
            errorPasswordLabel.setText("");
        }


        if (!errorIdLabel.getText().isEmpty() || !errorUsernameLabel.getText().isEmpty() ||
                !errorNameLabel.getText().isEmpty() || !errorSurnameLabel.getText().isEmpty()) {
            return;
        }

        if(!errorPassword) {
            return;
        }

        for (Data data : dataCurrentList.getDataList()) {
            if(data.getUsername().equals(oldUsername)){
                data.setProfile(personal.getDefaultProfilePic());
                data.setRole(personal.getRole());
                data.setUsername(usernameField.getText());
                data.setName(nameField.getText());
                data.setSurname(surnameField.getText());
                data.setTime(personal.getTime());
            }

        }

        for(StudentReq studentReq : studentReqList.getStudentReqs()){
            if(studentReq.getTeacherId().equals(oldTeacherId)){
                studentReq.setTeacherId(newTeacherId);
            }
        }

        for(Student student : currentStudentList.getStudents()){
            if(student.getTeacherId().equals(oldTeacherId)){
                student.setTeacherId(newTeacherId);
            }
        }


        personal.setUsername(usernameField.getText());
        personal.setName(nameField.getText());
        personal.setSurname(surnameField.getText());
        personal.setMajorId(majorComboBox.getValue() != null ? majorComboBox.getValue() : "");
        personal.setFacultyId(facultyComboBox.getValue());
        personal.setTeacherId(idField.getText());
        personal.setPassword(passwordField.getText());

        currentList.updatePersonal(personal);



        datasource.writeData(currentList);
        dataListDatasource.writeData(dataCurrentList);
        dataListStudentDatasource.writeData(studentReqList);
        studentDatasource.writeData(currentStudentList);


        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.close();
    }



}

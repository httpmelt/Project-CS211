package ku.cs.controllers.admin;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ku.cs.models.admin.*;
import ku.cs.models.student.StudentReq;
import ku.cs.models.student.StudentReqList;
import ku.cs.services.Datasource;
import ku.cs.services.admin.PersonalListCodeFileDatasource;
import ku.cs.services.admin.SectionTwoListCodeFileDatasource;
import ku.cs.services.student.StudentReqListFileDatasource;

public class EditSectionPopupController {
    @FXML
    private TextField facultyName;
    @FXML
    private TextField sectionId;

    private Section section;

    private Datasource<SectionList> datasource;
    private Datasource<SectionTwoList> datasourceTwo;
    private Datasource<PersonalList> datasourcePersonal;


    private SectionList currentList;
    private SectionTwoList currentTwoList;
    private PersonalList currentpersonalList;

    private Datasource<StudentReqList> datasourceStudentReq;
    private StudentReqList currentStudentReq;

    private String oldFacultyName;
    private String oldFacultyId;


    public void initialize() {
        datasourceTwo = new SectionTwoListCodeFileDatasource("data/user", "department.csv");
        currentTwoList = datasourceTwo.readData();


        datasourcePersonal = new PersonalListCodeFileDatasource("data/user", "Staffuser.csv");
        currentpersonalList = datasourcePersonal.readData();

        datasourceStudentReq = new StudentReqListFileDatasource("data/student", "studentReq.csv");
        currentStudentReq = datasourceStudentReq.readData();
    }

    public void setDatasource(Datasource<SectionList> datasource) {
        this.datasource = datasource;
    }

    public void setCurrentList(SectionList currentList) {
        this.currentList = currentList;
    }

    public void setSection(Section section) {
        this.section = section;
        facultyName.setText(section.getFacultyName());
        sectionId.setText(section.getFacultyId());


        oldFacultyName = section.getFacultyName();
        oldFacultyId = section.getFacultyId();
    }


    @FXML
    public void handleOkButton() {
        String newFacultyName = facultyName.getText();
        String facultyId = sectionId.getText();

        if (newFacultyName.isEmpty() || facultyId.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Faculty Name and Faculty ID cannot be empty.");
            alert.setTitle("Input Error");
            alert.showAndWait();
            return;
        }

        boolean idExists = currentList.getSections().stream().anyMatch(section -> section.getFacultyId().equals(facultyId));
        boolean nameExists = currentList.getSections().stream().anyMatch(section -> section.getFacultyName().equals(newFacultyName));

        if (newFacultyName.equals(section.getFacultyName()) && facultyId.equals(section.getFacultyId())) {
            Stage stage = (Stage) facultyName.getScene().getWindow();
            stage.close();
            return;
        }

        if (idExists && !facultyId.equals(section.getFacultyId())) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "The faculty ID already exists.");
            alert.setTitle("Duplicate ID");
            alert.showAndWait();
            return;
        } else if (nameExists && !newFacultyName.equals(section.getFacultyName())) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "The faculty name already exists.");
            alert.setTitle("Duplicate Faculty Name");
            alert.showAndWait();
            return;
        }


        for (Personal personal : currentpersonalList.getPersonalsList()) {
            if (personal.getFacultyId().equals(oldFacultyId)) {
                personal.setFacultyId(facultyId);
            }
        }

        for (SectionTwo sectionTwo : currentTwoList.getSectionTwoList()) {
            if (sectionTwo.getFaculty().trim().equalsIgnoreCase(oldFacultyName.trim())) {
                sectionTwo.setFaculty(newFacultyName);
            }
        }

        for(StudentReq studentReq : currentStudentReq.getStudentReqs()){
            if(studentReq.getFaculty().trim().equalsIgnoreCase(oldFacultyName.trim())){
                studentReq.setFaculty(newFacultyName);
            }
        }


        section.setFacultyName(newFacultyName);
        section.setFacultyId(facultyId);
        currentList.updateSectionList(section);


        datasource.writeData(currentList);
        datasourceTwo.writeData(currentTwoList);
        datasourcePersonal.writeData(currentpersonalList);
        datasourceStudentReq.writeData(currentStudentReq);


        Stage stage = (Stage) facultyName.getScene().getWindow();
        stage.close();
    }

}


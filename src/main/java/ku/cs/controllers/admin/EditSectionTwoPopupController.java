package ku.cs.controllers.admin;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ku.cs.models.admin.*;
import ku.cs.models.student.StudentReq;
import ku.cs.models.student.StudentReqList;
import ku.cs.services.Datasource;
import ku.cs.services.admin.PersonalListCodeFileDatasource;
import ku.cs.services.admin.SectionListCodeFileDatasource;
import ku.cs.services.student.StudentReqListFileDatasource;

public class EditSectionTwoPopupController {
    @FXML private TextField majorId;
    @FXML private TextField majorName;
    @FXML private ComboBox<String> facultyList;

    private SectionTwo sectionTwo;
    private Datasource<SectionTwoList> datasource;
    private SectionTwoList currentList;
    private Datasource<PersonalList> personalDatasource;
    private PersonalList currentPersonalList;
    private Datasource<StudentReqList> requestDatasource;
    private StudentReqList currentStudentReqList;


    String oldMajorId;
    String oldMajorName;


    public void initialize(){
        personalDatasource =  new PersonalListCodeFileDatasource("data/user", "Staffuser.csv");
        currentPersonalList = personalDatasource.readData();

        requestDatasource = new StudentReqListFileDatasource("data/student", "studentReq.csv");
        currentStudentReqList = requestDatasource.readData();

        populateFacultyList();
    }

    private void populateFacultyList() {
        SectionList sectionList = new SectionListCodeFileDatasource("data/user", "faculty.csv").readData();

        for(Section section : sectionList.getSections()){
            if(!section.getFacultyId().equals("facultyId")){
                facultyList.getItems().add(section.getFacultyName());
            }
        }
    }

    public void setSectionTwo(SectionTwo selected) {
        this.sectionTwo = selected;
        facultyList.setValue(sectionTwo.getFaculty());
        majorId.setText(sectionTwo.getMajorId());
        majorName.setText(sectionTwo.getMajor());
        oldMajorId = sectionTwo.getMajorId();
        oldMajorName = sectionTwo.getMajor();

    }

    public void setDatasource(Datasource<SectionTwoList> datasource) {
        this.datasource = datasource;
    }

    public void setCurrentList(SectionTwoList sectionTwoList) {
        this.currentList = sectionTwoList;
    }


    @FXML
    public void handleSaveButtonClicked() {
        String newId = majorId.getText();
        String newMajor = majorName.getText();
        String newFaculty = facultyList.getValue();


        if (newId.isEmpty() || newMajor.isEmpty() || newFaculty == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Major ID, Major Name, and Faculty must not be empty.");
            alert.setTitle("Input Error");
            alert.showAndWait();
            return;
        }


        if (newMajor.equals(sectionTwo.getMajor()) && newId.equals(sectionTwo.getMajorId())) {
            Stage stage = (Stage) majorName.getScene().getWindow();

            sectionTwo.setMajorId(newId);
            sectionTwo.setMajor(newMajor);
            sectionTwo.setFaculty(newFaculty);

            currentList.updateSectionTwoList(sectionTwo);
            datasource.writeData(currentList);
            stage.close();
            return;
        }

        boolean majorExists = currentList.getSectionTwoList().stream().anyMatch(sectionTwo -> sectionTwo.getMajor().equals(newMajor));
        boolean majorIdExists = currentList.getSectionTwoList().stream().anyMatch(sectionTwo -> sectionTwo.getMajorId().equals(newId));

        if (majorExists && majorIdExists) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "The major ID and name already exist.");
            alert.setTitle("Duplicate Major Name and ID");
            alert.showAndWait();
        } else if (majorExists && !newFaculty.equals(sectionTwo.getFaculty())) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "The Major Name already exists.");
            alert.setTitle("Duplicate Major Name");
            alert.showAndWait();
        } else if (majorIdExists && !newId.equals(sectionTwo.getMajorId())) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "The Major ID already exists.");
            alert.setTitle("Duplicate Major ID");
            alert.showAndWait();
        } else {

            for(Personal personal : currentPersonalList.getPersonalsList()){
                if(personal.getMajorId().equals(oldMajorId)){
                    personal.setMajorId(newId);
                }
            }

            for(StudentReq studentReq : currentStudentReqList.getStudentReqs()){
                if(studentReq.getMajor().equals(oldMajorName)){
                    studentReq.setMajor(newMajor);
                }
            }


            sectionTwo.setMajorId(newId);
            sectionTwo.setMajor(newMajor);
            sectionTwo.setFaculty(newFaculty);

            currentList.updateSectionTwoList(sectionTwo);
            datasource.writeData(currentList);
            personalDatasource.writeData(currentPersonalList);
            requestDatasource.writeData(currentStudentReqList);

            Stage stage = (Stage) majorName.getScene().getWindow();
            stage.close();
        }
    }

}

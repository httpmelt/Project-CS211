package ku.cs.controllers.admin;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ku.cs.models.admin.Section;
import ku.cs.models.admin.SectionList;
import ku.cs.models.admin.SectionTwoList;
import ku.cs.services.Datasource;
import ku.cs.services.admin.SectionListCodeFileDatasource;

public class AddMoreSectionTwoPopupController {
    @FXML private ComboBox<String> facultyList;
    @FXML private TextField majorName;
    @FXML private TextField majorId;


    @FXML private Label errorNameText;
    @FXML private Label errorFacultyText;
    @FXML private Label errorIdText;

    private Datasource<SectionTwoList> datasource;
    private SectionTwoList currentList;

    public void initialize() {
        populateCombobox();
        facultyList.setValue("");
    }

    private void populateCombobox() {
        SectionList sectionList = new SectionListCodeFileDatasource("data/user", "faculty.csv").readData();
        for(Section section : sectionList.getSections()) {
            if(!section.getFacultyName().equals("facultyName")){
                facultyList.getItems().add(section.getFacultyName());
            };
        }
        facultyList.setPromptText("Please select faculty");
    }


    public void setDatasource(Datasource<SectionTwoList> datasource) {
        this.datasource = datasource;
    }

    public void setSectionTwoList(SectionTwoList sectionTwoList) {
        this.currentList = sectionTwoList;
        errorIdText.setText("");
        errorNameText.setText("");
        errorFacultyText.setText("");
    }


    public void onAddMoreButtonClicked() {
        String newMajorName = majorName.getText();
        String newFacultyName = facultyList.getValue();
        String newMajorId = majorId.getText();

        errorIdText.setStyle("-fx-text-fill: red");
        errorNameText.setStyle("-fx-text-fill: red");
        errorFacultyText.setStyle("-fx-text-fill: red");


        Stage stage = (Stage) majorName.getScene().getWindow();

        boolean MajorNameExists = currentList.getSectionTwoList().stream().anyMatch(sectionTwo -> sectionTwo.getMajor().equals(newMajorName));
        boolean MajorIdExists = currentList.getSectionTwoList().stream().anyMatch(sectionTwo -> sectionTwo.getMajorId().equals(newMajorId));

        if (newMajorName.equals("") && newMajorId.equals("") && newFacultyName.equals("")) {
            errorFacultyText.setText("Faculty name cannot be empty");
            errorNameText.setText("Major name cannot be empty");
            errorIdText.setText("Major id cannot be empty");

        }else if(newMajorName.equals("") && newMajorId.equals("")){
            errorNameText.setText("Major name cannot be empty");
            errorIdText.setText("Major id cannot be empty");
            errorFacultyText.setText("");
        }else if(newMajorId.equals("")){
            errorIdText.setText("Major id cannot be empty");
            if(MajorNameExists){
                errorNameText.setText("Major name already exists");
            }else{
                errorNameText.setText("");
            }
            errorFacultyText.setText("");
        }else if(newMajorName.equals("")){
            errorNameText.setText("Major name cannot be empty");
            if(MajorIdExists){
                errorIdText.setText("Major id already exists");
            }else{
                errorIdText.setText("");
            }
            errorFacultyText.setText("");
        }else{
            errorFacultyText.setText("");
            if(MajorNameExists && MajorIdExists) {
                errorNameText.setText("Major name already exists");
                errorIdText.setText("Major id already exists");
            }else if(MajorIdExists) {
                errorIdText.setText("Major id already exists");
                errorNameText.setText("");
            }else if(MajorNameExists) {
                errorNameText.setText("Major name already exists");
                errorIdText.setText("");
            }else{
                errorFacultyText.setText("");
                errorNameText.setText("");
                errorIdText.setText("");
                currentList.addSectionTwo(newMajorId, newMajorName, newFacultyName);
                datasource.writeData(currentList);
                stage.close();
            }
        }








    }
}

package ku.cs.controllers.admin;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ku.cs.models.admin.SectionList;
import ku.cs.services.Datasource;

public class AddMoreSectionPopupController {

    @FXML private TextField facultyName;
    @FXML private TextField facultyId;
    @FXML private Label errorFacultyName;
    @FXML private Label errorFacultyId;

    private Datasource<SectionList> datasource;
    private SectionList currentList;


    public void setDatasource(Datasource<SectionList> datasource) {
        this.datasource = datasource;
    }

    public void setCurrentList(SectionList sectionList) {
        this.currentList = sectionList;
        errorFacultyName.setText("");
        errorFacultyId.setText("");
    }

    public void onAddMoreButtonClicked() {
        String newFacultyName = facultyName.getText().trim();
        String newFacultyId = facultyId.getText().trim();

        errorFacultyId.setStyle("-fx-text-fill: red");
        errorFacultyName.setStyle("-fx-text-fill: red");

        boolean facultyNameExists = currentList.getSections().stream()
                .anyMatch(section -> section.getFacultyName().equals(newFacultyName));
        boolean facultyIdExists = currentList.getSections().stream()
                .anyMatch(section -> section.getFacultyId().equals(newFacultyId));

        Stage stage = (Stage) facultyName.getScene().getWindow();


        errorFacultyName.setText("");
        errorFacultyId.setText("");


        if (newFacultyId.isEmpty()) {
            errorFacultyId.setText("Faculty ID cannot be empty");
            return;
        }

        if (newFacultyName.isEmpty()) {
            errorFacultyName.setText("Faculty name cannot be empty");
            return;
        }


        if (facultyIdExists) {
            errorFacultyId.setText("Faculty ID already exists");
        }

        if (facultyNameExists) {
            errorFacultyName.setText("Faculty name already exists");
        }

        if (newFacultyName.isEmpty() && newFacultyId.isEmpty()) {
            errorFacultyId.setText("Faculty ID cannot be empty");
            errorFacultyName.setText("Faculty name cannot be empty");
            return;
        }

        if (!facultyIdExists && !facultyNameExists) {
                currentList.addSection(newFacultyName, newFacultyId);
                datasource.writeData(currentList);
                stage.close();

        }
    }

}

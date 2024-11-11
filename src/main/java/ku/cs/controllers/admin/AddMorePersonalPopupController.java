package ku.cs.controllers.admin;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ku.cs.models.admin.*;
import ku.cs.services.Datasource;
import ku.cs.services.admin.DataListCodeFileDatasource;
import ku.cs.services.admin.SectionListCodeFileDatasource;
import ku.cs.services.admin.SectionTwoListCodeFileDatasource;

import java.time.LocalDateTime;

public class AddMorePersonalPopupController {
    @FXML private ComboBox<String> staffComboBox;
    @FXML private ComboBox<String> facultyComboBox;
    @FXML private ComboBox<String> majorComboBox;

    @FXML private TextField nameField;
    @FXML private TextField surnameTextField;
    @FXML private TextField usernameField;
    @FXML private TextField passwordField;
    @FXML private TextField idField;

    @FXML private Label idLabel;
    @FXML private Label majorLabel;

    @FXML private Label errorUsername;
    @FXML private Label errorPassword;
    @FXML private Label errorId;
    @FXML private Label errorName;
    @FXML private Label errorSurname;



    private Datasource<PersonalList> datasource;
    private Datasource<DataList> dataListDatasource;

    private PersonalList currentList;
    private DataList currentDataList;
    private Personal personal;

    @FXML
    public void initialize() {

        dataListDatasource = new DataListCodeFileDatasource("data/user", "Allusers.csv");
        currentDataList = dataListDatasource.readData();
        populateStaffCombo();

        staffComboBox.setPromptText("Select Staff");

        // Add listener to staffComboBox
        staffComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(!staffComboBox.getValue().equals("")) {
                setScene(newValue);
                facultyComboBox.setVisible(true);
                nameField.setVisible(true);
                surnameTextField.setVisible(true);
                usernameField.setVisible(true);
                passwordField.setVisible(true);
                errorUsername.setVisible(true);
                errorPassword.setVisible(true);
                errorId.setVisible(true);
                errorName.setVisible(true);
                errorSurname.setVisible(true);

            }else{
                majorComboBox.setVisible(false);
                facultyComboBox.setVisible(false);
                nameField.setVisible(false);
                surnameTextField.setVisible(false);
                usernameField.setVisible(false);
                passwordField.setVisible(false);
                idField.setVisible(false);
                errorUsername.setVisible(false);
                errorPassword.setVisible(false);
                errorId.setVisible(false);
                errorName.setVisible(false);
                errorSurname.setVisible(false);
            }
            errorName.setText("");
            errorSurname.setText("");
            errorUsername.setText("");
            errorPassword.setText("");
            errorId.setText("");
            errorName.setText("");
            errorSurname.setText("");
        });

        populateFacultyComboBox();
        facultyComboBox.setOnAction(event -> updateMajorComboBox());

        errorUsername.setStyle("-fx-text-fill: red");
        errorPassword.setStyle("-fx-text-fill: red");
        errorId.setStyle("-fx-text-fill: red");
        errorName.setStyle("-fx-text-fill: red");
        errorSurname.setStyle("-fx-text-fill: red");
    }

    private void populateStaffCombo() {
        staffComboBox.getItems().addAll(
                "อาจารย์ที่ปรึกษา",
                "เจ้าหน้าที่คณะ",
                "เจ้าหน้าที่ภาควิชา"


        );

    }

    public void setScene(String value) {
        if (value.equals("เจ้าหน้าที่คณะ")) {
            idField.setVisible(false);
            majorComboBox.setVisible(false);
            idLabel.setVisible(false);
            majorLabel.setText("");
        } else{
            majorComboBox.setVisible(true);
            majorLabel.setText("Major:");
            idLabel.setText("Teacher ID:");
        }

        if (value.equals("เจ้าหน้าที่ภาควิชา")) {
            idField.setVisible(false);
            idLabel.setText("");
        }else{
            idLabel.setVisible(true);
        }

        if(value.equals("อาจารย์ที่ปรึกษา")){
            idField.setVisible(true);
        }else{
            idLabel.setVisible(false);
        }
    }

    public void setDatasource(Datasource<PersonalList> datasource) {
        this.datasource = datasource;
    }

    public void setCurrentList(PersonalList personalList) {
        this.currentList = personalList;
        errorUsername.setText("");
        errorPassword.setText("");
        errorId.setText("");
        errorName.setText("");
        errorSurname.setText("");
        staffComboBox.setValue("");
    }

    private void populateFacultyComboBox() {

        SectionList sectionList = new SectionListCodeFileDatasource("data/user", "faculty.csv").readData();
        facultyComboBox.getItems().clear();
        for (Section section : sectionList.getSections()) {
            if(!section.getFacultyId().equals("facultyId") && !section.getFacultyName().equals("facultyName")) {
                facultyComboBox.getItems().add(section.getFacultyId());
            }
        }


        if (!facultyComboBox.getItems().isEmpty()) {
            facultyComboBox.setValue(facultyComboBox.getItems().get(0));
            updateMajorComboBox();
        }
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


        SectionTwoList sectionTwoList = new SectionTwoListCodeFileDatasource("data/user", "department.csv").readData();
        for (SectionTwo sectionTwo : sectionTwoList.getSectionTwoList()) {
            if (sectionTwo.getFaculty().equals(facultyValue)) {
                if(!sectionTwo.getMajorId().equals("majorId") && !sectionTwo.getMajor().equals("majorName") && !sectionTwo.getMajorId().equals("MajorId")) {
                    majorComboBox.getItems().add(sectionTwo.getMajorId());
                }

            }
        }

        if(!majorComboBox.getItems().isEmpty()) {
            majorComboBox.setValue(majorComboBox.getItems().get(0));
        }


    }




    public void onSaveButtonClicked() {
        String name = nameField.getText();
        String surname = surnameTextField.getText();
        String username = usernameField.getText();
        String password = passwordField.getText();
        String id = idField.getText();
        String facultyId = facultyComboBox.getValue();
        String majorId = majorComboBox.getValue();
        String staff = staffComboBox.getValue();

        resetErrorMessages();

        boolean userNameExists = currentList.getPersonalsList().stream()
                .anyMatch(personal -> personal.getUsername().equals(username));
        boolean dataUserExists = currentDataList.getDataList().stream().anyMatch(data -> data.getUsername().equals(username));


        boolean hasErrors = false;

        if (userNameExists) {
            errorUsername.setText("Username already exists");
            hasErrors = true;
        }

        if(dataUserExists) {
            errorUsername.setText("Username user already exists");
            hasErrors = true;
        }

        hasErrors |= validateField(username, errorUsername, "Username cannot be empty");
        hasErrors |= validateField(name, errorName, "Name cannot be empty");
        hasErrors |= validateField(surname, errorSurname, "Surname cannot be empty");


        if (staff.equals("เจ้าหน้าที่คณะ")) {

        } else if (staff.equals("เจ้าหน้าที่ภาควิชา")) {

            hasErrors |= validateField(majorId, errorId, "Major cannot be empty");
        } else {
            // For "อาจารย์ที่ปรึกษา" both id and major need to be validated
            hasErrors |= validateField(id, errorId, "ID cannot be empty");
            hasErrors |= validateField(majorId, errorId, "Major cannot be empty");
        }

        hasErrors |= validateField(password, errorPassword, "Password cannot be empty");

        if (!hasErrors) {
            savePersonalData(name, surname, username, password, id, staff, facultyId, majorId);
        }
    }

    private void resetErrorMessages() {
        errorUsername.setText("");
        errorPassword.setText("");
        errorId.setText("");
        errorName.setText("");
        errorSurname.setText("");
    }

    private boolean validateField(String value, Label errorLabel, String errorMessage) {
        if (value.isEmpty()) {
            errorLabel.setText(errorMessage);
            return true;
        }
        return false;
    }

    private void savePersonalData(String name, String surname, String username, String password, String id, String staff, String facultyId, String majorId) {
        Stage stage = (Stage) staffComboBox.getScene().getWindow();
        LocalDateTime time = LocalDateTime.now();

        currentList.addPersonal("default-profile-pic.png", staff, name, surname, username, password, facultyId, majorId, id, false, time);
        datasource.writeData(currentList);

        currentDataList.addData("default-profile-pic.png", staff, name, surname, username, time);
        dataListDatasource.writeData(currentDataList);
        stage.close();
    }
}

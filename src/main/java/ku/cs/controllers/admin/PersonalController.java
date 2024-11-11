package ku.cs.controllers.admin;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ku.cs.models.admin.*;
import ku.cs.services.Datasource;
import ku.cs.services.FXRouter;
import ku.cs.services.admin.PersonalListCodeFileDatasource;
import ku.cs.services.admin.SectionListCodeFileDatasource;
import ku.cs.services.admin.SectionTwoListCodeFileDatasource;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PersonalController {

    @FXML
    private ImageView imagePersonal;

    @FXML
    private ImageView dashBoardImageView4;
    @FXML
    private ImageView DataImageView4;
    @FXML
    private ImageView DataClassImageView4;
    @FXML
    private ImageView PersonalImageView4;
    @FXML
    private ImageView loginImageView4;

    @FXML private TableView<Personal> PersonalTableView;

    @FXML private TextField searchTextField;


    private Section section;
    private PersonalList personalList;
    private Datasource<PersonalList> datasource;
    private Personal selectedPersonal;

    @FXML private Label adminnameLabel;

    @FXML private Label usernameLabel;
    @FXML private Label nameLabel;
    @FXML private Label majorLabel;
    @FXML private Label facultyLabel;
    @FXML private Label idLabel;
    @FXML private Label passwordLabel;
    @FXML private Label headMajorLabel;
    @FXML private Label headIdLabel;
    @FXML private Label headRole;

    @FXML private Label adminId;
    String profile;

    @FXML
    public void initialize() {


        clearPersonalInfo();
        datasource = new PersonalListCodeFileDatasource("data/user", "Staffuser.csv");
        personalList = datasource.readData();

        showAdminInformation();

        for(Personal personal: personalList.getPersonalsList()){
            if(personal.getRole().equals("ผู้ดูแลระบบ")){
                profile = personal.getDefaultProfilePic();
                break;
            }
        }


        Image image4 =  new Image(getClass().getResource("/images/default-profile-pic.png").toString());
        Image dataImage4 = new Image(getClass().getResource("/images/person.png").toString());
        Image dashBoardImage4 = new Image(getClass().getResource("/images/board.png").toString());
        Image dataClassImage4 = new Image(getClass().getResource("/images/class.png").toString());
        Image personalImage4 = new Image(getClass().getResource("/images/personal.png").toString());


        imagePersonal.setImage(image4);
        dashBoardImageView4.setImage(dashBoardImage4);
        DataImageView4.setImage(dataImage4);
        DataClassImageView4.setImage(dataClassImage4);
        PersonalImageView4.setImage(personalImage4);

        showTable(personalList);

        PersonalTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Personal>() {
            @Override
            public void changed(ObservableValue<? extends Personal> observable, Personal oldValue, Personal newValue) {
                if (newValue == null) {
                    clearPersonalInfo();
                    selectedPersonal = null;
                } else {
                    showPersonalInfo(newValue);
                    selectedPersonal = newValue;
                }
            }
        });


        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            onSearchChange(newValue);
        });
    }

    private void showAdminInformation() {
        for(Personal personal: personalList.getPersonalsList()){
            if(personal.getRole().equals("ผู้ดูแลระบบ")){
                adminnameLabel.setText(personal.getName() + " " + personal.getSurname());
                adminnameLabel.setStyle("-fx-text-fill: white;");
                adminId.setText(personal.getUsername());
                adminId.setStyle("-fx-text-fill: white;");
                String profilePicPath = "data/profile-pic/" + personal.getDefaultProfilePic();
                File profilePicFile = new File(profilePicPath);
                if (profilePicFile.exists()) {
                    Image profileImage = new Image(profilePicFile.toURI().toString());
                    loginImageView4.setImage(profileImage);
                } else {

                    loginImageView4.setImage(new Image(getClass().getResourceAsStream("/images/default-profile.png")));
                }
                break;
            }
        }
    }





    public void clearPersonalInfo() {
        usernameLabel.setText("");
        nameLabel.setText("");
        majorLabel.setText("");
        facultyLabel.setText("");
        idLabel.setText("");
        passwordLabel.setText("");


    }

    public void showPersonalInfo(Personal personal) {
        showMajor(personal.getMajorId());
        showFaculty(personal.getFacultyId());
        usernameLabel.setText(personal.getUsername());
        nameLabel.setText(personal.getName() + " " + personal.getSurname());
        idLabel.setText(personal.getTeacherId());
        passwordLabel.setText(personal.getPassword());
        headRole.setText(personal.getRole());

        if(personal.getMajorId().isEmpty()){
            headMajorLabel.setText("");
            majorLabel.setText("");
            idLabel.setText("");
        }else{
            headMajorLabel.setText("ภาควิชา");
        }
        if(personal.getTeacherId().isEmpty()){
            headIdLabel.setText("");
        }else{
            headIdLabel.setText("รหัสประจำตัว");
        }


        String profilePicPath = "data/profile-pic/" + personal.getDefaultProfilePic();
        File profilePicFile = new File(profilePicPath);
        if (profilePicFile.exists()) {
            Image profileImage = new Image(profilePicFile.toURI().toString());
            imagePersonal.setImage(profileImage);
        } else {

            imagePersonal.setImage(new Image(getClass().getResourceAsStream("/images/default-profile.png")));
        }


    }

    public void showMajor(String majorId) {
        SectionTwoList sectionTwoList = new SectionTwoListCodeFileDatasource("data/user", "department.csv").readData();
        boolean Exist = sectionTwoList.getSectionTwoList().stream().anyMatch(section -> section.getMajorId().equals(majorId));

        if(Exist){
            for(SectionTwo sectionTwo : sectionTwoList.getSectionTwoList()){
                if(sectionTwo.getMajorId().equals(majorId) ){
                    majorLabel.setStyle("-fx-text-fill: black;");
                    facultyLabel.setStyle("-fx-text-fill: black;");
                    facultyLabel.setText(sectionTwo.getFaculty());
                    majorLabel.setText(sectionTwo.getMajor());
                    break;
                }
            }
        }else{
            majorLabel.setStyle("-fx-text-fill: red;");
            majorLabel.setText("This Major does not exist. Please select a new Major.");
        }



    }

    public void showFaculty(String facultyId) {
        SectionList sectionList = new SectionListCodeFileDatasource("data/user", "faculty.csv").readData();
        boolean Exists = sectionList.getSections().stream().anyMatch(personal -> personal.getFacultyId().equals(facultyId));
        if(Exists){
            for(Section section : sectionList.getSections()){
                if(section.getFacultyId().equals(facultyId)){
                    facultyLabel.setText(section.getFacultyName());
                    facultyLabel.setStyle("-fx-text-fill: black;");
                    break;
                }
            }
        }else{

            facultyLabel.setText("This Faculty does not exist. Please select a new Faculty.");
            facultyLabel.setStyle("-fx-text-fill: red;");
            majorLabel.setText("This Major does not exist on Faculty. Please select a new Major.");
            majorLabel.setStyle("-fx-text-fill: red;");
        }
    }






    public void showTable(PersonalList personalList) {

        PersonalTableView.getColumns().clear();

        TableColumn<Personal, String> usernameColumn = new TableColumn<>("Username");
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<Personal, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Personal, String> surnameColumn = new TableColumn<>("Surname");
        surnameColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));

        SectionTwoList sectionTwoList = new SectionTwoListCodeFileDatasource("data/user", "department.csv").readData();
        SectionList sectionList = new SectionListCodeFileDatasource("data/user", "faculty.csv").readData();

        TableColumn<Personal, String> RoleColumn = new TableColumn<>("Role");
        RoleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));

        TableColumn<Personal, String> majorColumn = new TableColumn<>("Major Id");
        majorColumn.setCellValueFactory(cellData -> {
            Personal personal = cellData.getValue();
            String majorId = personal.getMajorId();
            String facultyId = personal.getFacultyId();

            if (majorId.isEmpty()) {
                return new SimpleStringProperty("");
            }

            boolean majorExists = sectionTwoList.getSectionTwoList().stream()
                    .anyMatch(section -> section.getMajorId().equals(majorId));
            boolean facultyExists = sectionList.getSections().stream()
                    .anyMatch(section -> section.getFacultyId().equals(facultyId));

            if (!majorExists) {
                return new SimpleStringProperty("Major Id does not exist on Faculty");
            }
            if (!facultyExists) {
                return new SimpleStringProperty("Faculty Id does not exist");
            }

            return new SimpleStringProperty(majorId);
        });

        majorColumn.setCellFactory(column -> new TableCell<Personal, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    if (item.equals("Major Id does not exist on Faculty") ||
                            item.equals("Faculty Id does not exist")) {
                        setTextFill(javafx.scene.paint.Color.RED); // Set text color to red
                    } else {
                        setTextFill(javafx.scene.paint.Color.BLACK); // Reset to default color
                    }
                }
            }
        });

        TableColumn<Personal, String> facultyColumn = new TableColumn<>("Faculty Id");
        facultyColumn.setCellValueFactory(cellData -> {
            Personal personal = cellData.getValue();
            String facultyId = personal.getFacultyId();

            boolean facultyExists = sectionList.getSections().stream()
                    .anyMatch(section -> section.getFacultyId().equals(facultyId));
            return new SimpleStringProperty(facultyExists ? facultyId : "Faculty Id does not exist");
        });

        facultyColumn.setCellFactory(column -> new TableCell<Personal, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    if (item.equals("Faculty Id does not exist")) {
                        setTextFill(javafx.scene.paint.Color.RED);
                    } else {
                        setTextFill(javafx.scene.paint.Color.BLACK); // Reset to default color
                    }
                }
            }
        });

        TableColumn<Personal, String> idColumn = new TableColumn<>("Teacher Id");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("teacherId"));

        TableColumn<Personal, String> passwordColumn = new TableColumn<>("Password");
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));

        PersonalTableView.getColumns().addAll(RoleColumn, usernameColumn, nameColumn, surnameColumn, majorColumn, facultyColumn, idColumn, passwordColumn);

        PersonalTableView.getItems().clear();

        for (Personal personal : personalList.getPersonalsList()) {
            if (!personal.getRole().equals("ผู้ดูแลระบบ") && !personal.getRole().equals("role")) {
                PersonalTableView.getItems().add(personal);
            }
        }
    }






    private TableCell<Personal, String> createColoredCell(String placeholder) {
        return new TableCell<Personal, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(item);
                    setTextFill(item.equals(placeholder) ? Color.RED : Color.BLACK);
                }
            }
        };
    }





    public void onSearchChange(String searchTerm) {

        PersonalTableView.getItems().clear();


        if (searchTerm.trim().isEmpty()) {
            showTable(personalList);
            return;
        }


        for (Personal personal : personalList.getPersonalsList()) {
            String username = personal.getUsername().toLowerCase();
            String name = personal.getName().toLowerCase();
            String surname = personal.getSurname().toLowerCase();
            String major = personal.getMajorId().toLowerCase();
            String faculty = personal.getFacultyId().toLowerCase();
            String teacherId = personal.getTeacherId().toLowerCase();


            if (name.contains(searchTerm) || username.contains(searchTerm) ||
                    major.contains(searchTerm) || faculty.contains(searchTerm) ||
                    teacherId.contains(searchTerm) || surname.contains(searchTerm)) {

                if (!personal.getRole().equals("ผู้ดูแลระบบ") && !personal.getRole().equals("role")) {
                    PersonalTableView.getItems().add(personal);
                }
            }
        }
    }

    @FXML
    public void AddMoreButtonClicked(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ku/cs/views/addPersonalPopup.fxml"));
            Parent root = loader.load();

            AddMorePersonalPopupController controller = loader.getController();
            controller.setDatasource(datasource);
            controller.setCurrentList(personalList);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Add More Staff");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            showTable(personalList);

        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    @FXML
    public void handleEditButtonClick() {
        Personal selected = PersonalTableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ku/cs/views/EditPersonalPopup.fxml"));
                Parent root = loader.load();

                EditPersonalPopupController controller = loader.getController();
                controller.setPersonal(selected);
                controller.setDatasource(datasource);
                controller.setCurrentList(personalList);

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Edit Personal Info");
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait();


                showTable(personalList);

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please select a personal entry to edit.");
            alert.showAndWait();
        }
    }


    public void onOptionButtonClick(){
        try {
            Map<String, Object> forOption = new HashMap<>();
            forOption.put("username", "admin");
            forOption.put("name", "ชัยยงค์ คึมยะราช");
            forOption.put("profilePic", profile);
            forOption.put("role", "ผูู้แลระบบ");
            FXRouter.goTo("change-password", forOption);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void onLogoutButtonClick(){
        try{
            FXRouter.goTo("homepage");
        }catch(IOException e){
            throw new RuntimeException(e);
        }
    }



    public void onBoardBackClick() {
        try {
            FXRouter.goTo("admin");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void onBoardToDataClick() {
        try {
            FXRouter.goTo("data");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void OnBoardToClassClick(){
        try {
            FXRouter.goTo("section");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void OnBoardToPersonalClick(){
        try {
            FXRouter.goTo("personal");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}


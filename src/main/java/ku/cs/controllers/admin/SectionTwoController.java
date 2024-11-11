package ku.cs.controllers.admin;

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
import javafx.stage.Modality;
import javafx.stage.Stage;
import ku.cs.models.admin.Personal;
import ku.cs.models.admin.PersonalList;
import ku.cs.models.admin.SectionTwo;
import ku.cs.models.admin.SectionTwoList;
import ku.cs.services.Datasource;
import ku.cs.services.FXRouter;
import ku.cs.services.admin.PersonalListCodeFileDatasource;
import ku.cs.services.admin.SectionTwoListCodeFileDatasource;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SectionTwoController {
    @FXML
    private ImageView dashBoardImageView3;
    @FXML
    private ImageView DataImageView3;
    @FXML
    private ImageView DataClassImageView3;
    @FXML
    private ImageView PersonalImageView3;
    @FXML
    private ImageView loginImageView3;

    @FXML TableView<SectionTwo> sectionTwoTable;

    private SectionTwoList sectionTwoList;

    private Datasource<SectionTwoList> datasource;

    @FXML private SectionTwo selectedSectionTwo;

    @FXML private Label majorLabel;
    @FXML private Label MajorIdLabel;
    @FXML private Label facultyLabel;

    @FXML private TextField searchTextField;

    @FXML private Label adminnameLabel;
    private Datasource<PersonalList> personalDatasource;
    private PersonalList personalList;


    @FXML private Label adminId;

    String profile;

    @FXML
    public void initialize() {

        clearSectionTwoInfo();
        datasource = new SectionTwoListCodeFileDatasource("data/user", "department.csv");
        sectionTwoList = datasource.readData();

        personalDatasource = new PersonalListCodeFileDatasource("data/user", "Staffuser.csv");
        personalList = personalDatasource.readData();

        showAdminInformation();

        for(Personal personal: personalList.getPersonalsList()){
            if(personal.getRole().equals("ผู้ดูแลระบบ")){
                profile = personal.getDefaultProfilePic();
                break;
            }
        }


        Image dataImage3 = new Image(getClass().getResource("/images/person.png").toString());
        Image dashBoardImage3 = new Image(getClass().getResource("/images/board.png").toString());
        Image dataClassImage3 = new Image(getClass().getResource("/images/class.png").toString());
        Image personalImage3 = new Image(getClass().getResource("/images/personal.png").toString());


        dashBoardImageView3.setImage(dashBoardImage3);
        DataImageView3.setImage(dataImage3);
        DataClassImageView3.setImage(dataClassImage3);
        PersonalImageView3.setImage(personalImage3);

        showTable(sectionTwoList);

        sectionTwoTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<SectionTwo>() {
            @Override
            public void changed(ObservableValue<? extends SectionTwo> observable, SectionTwo oldValue, SectionTwo newValue) {
                if (newValue == null) {
                    clearSectionTwoInfo();
                    selectedSectionTwo = null;
                } else {
                    showSectionTwoInfo(newValue);
                    selectedSectionTwo = newValue;
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
                    loginImageView3.setImage(profileImage);
                } else {
                    // Handle case where the image does not exist (e.g., set a default image)
                    loginImageView3.setImage(new Image(getClass().getResourceAsStream("/images/default-profile.png")));
                }
                break;
            }
        }
    }

    private void clearSectionTwoInfo() {
        facultyLabel.setText("");
        majorLabel.setText("");
        MajorIdLabel.setText("");
    }

    private void showSectionTwoInfo(SectionTwo sectionTwo) {
        facultyLabel.setText(sectionTwo.getFaculty());
        majorLabel.setText(sectionTwo.getMajor());
        MajorIdLabel.setText(sectionTwo.getMajorId());
    }

    private void onSearchChange(String searchTerm) {
        sectionTwoTable.getItems().clear();
        searchTerm = searchTerm.trim().toLowerCase();

        if (searchTerm.trim().isEmpty()) {
            showTable(sectionTwoList);
            return;
        };

        for(SectionTwo sectionTwo : sectionTwoList.getSectionTwoList()){
            String majorId = sectionTwo.getMajorId().toLowerCase();
            String major = sectionTwo.getMajor().toLowerCase();
            String faculty = sectionTwo.getFaculty().toLowerCase();

            if(majorId.contains(searchTerm) || major.contains(searchTerm) || faculty.startsWith(searchTerm)){
                if(!sectionTwo.getMajor().equals("majorName")){
                    sectionTwoTable.getItems().add(sectionTwo);
                }
            }
        }

    }

    @FXML
    private void onAddMoreButtonClicked(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ku/cs/views/addSectionTwoPopup.fxml"));
            Parent root = loader.load();

            AddMoreSectionTwoPopupController controller = loader.getController();
            controller.setDatasource(datasource);
            controller.setSectionTwoList(sectionTwoList);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Add More Major");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            showTable(sectionTwoList);

        }catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void OnChangeEditButtonClicked() {
        SectionTwo selected = sectionTwoTable.getSelectionModel().getSelectedItem();
        if(selected != null) {
            try{
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ku/cs/views/EditSectionTwoPopup.fxml"));
                Parent root = loader.load();

                EditSectionTwoPopupController controller = loader.getController();
                controller.setSectionTwo(selected);
                controller.setDatasource(datasource); // Pass the datasource if needed
                controller.setCurrentList(sectionTwoList); // New method to set the current list

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Edit Section Info");
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait();

                // Refresh the TableView after closing the popup
                showTable(sectionTwoList);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please select a entry to edit.");
            alert.showAndWait();
        }
    }


    public void showTable(SectionTwoList sectionTwoList) {
        TableColumn<SectionTwo, String> majorColumn = new TableColumn<>("Major");
        majorColumn.setCellValueFactory(new PropertyValueFactory<>("major"));

        TableColumn<SectionTwo, String> facultyColumn = new TableColumn<>("Faculty");
        facultyColumn.setCellValueFactory(new PropertyValueFactory<>("faculty"));

        TableColumn<SectionTwo, String> majorIdColumn = new TableColumn<>("Major ID");
        majorIdColumn.setCellValueFactory(new PropertyValueFactory<>("majorId"));

        sectionTwoTable.getColumns().clear();
        sectionTwoTable.getColumns().addAll(majorColumn, facultyColumn, majorIdColumn);

        sectionTwoTable.getItems().clear();

        for(SectionTwo sectionTwo : sectionTwoList.getSectionTwoList()) {
            if(!sectionTwo.getMajor().equals("majorName")){
                sectionTwoTable.getItems().add(sectionTwo);
            }
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

    public void OnBoardToClassClick(){
        try {
            FXRouter.goTo("section");
        } catch (IOException e) {
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

    public void onButtonOneClick(){
        try {
            FXRouter.goTo("section");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void onButtonTwoClick(){
        try {
            FXRouter.goTo("sectionTwo");
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
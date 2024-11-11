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
import ku.cs.models.admin.Section;
import ku.cs.models.admin.SectionList;
import ku.cs.services.Datasource;
import ku.cs.services.FXRouter;
import ku.cs.services.admin.PersonalListCodeFileDatasource;
import ku.cs.services.admin.SectionListCodeFileDatasource;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SectionController {
    @FXML
    private ImageView dashBoardImageView2;
    @FXML
    private ImageView DataImageView2;
    @FXML
    private ImageView DataClassImageView2;
    @FXML
    private ImageView PersonalImageView2;
    @FXML
    private ImageView loginImageView2;

    @FXML private TableView<Section> sectionTableView;

    private SectionList sectionList;

    private Datasource<SectionList> datasource;

    @FXML private Section selectedSection;

    @FXML private Label facultyNameLabel;
    @FXML private Label facultyIdLabel;

    @FXML private TextField searchTextField;

    @FXML private Label adminnameLabel;
    private Datasource<PersonalList> personalDatasource;
    private PersonalList personalList;

    @FXML private Label adminId;

    String profile;

    @FXML
    public void initialize() {


        clearSectionInfo();
        datasource = new SectionListCodeFileDatasource("data/user", "faculty.csv");
        sectionList = datasource.readData();

        personalDatasource = new PersonalListCodeFileDatasource("data/user", "Staffuser.csv");
        personalList = personalDatasource.readData();

        showAdminInformation();

        for(Personal personal: personalList.getPersonalsList()){
            if(personal.getRole().equals("ผู้ดูแลระบบ")){
                profile = personal.getDefaultProfilePic();
                break;
            }
        }


        Image dataImage2 = new Image(getClass().getResource("/images/person.png").toString());
        Image dashBoardImage2 = new Image(getClass().getResource("/images/board.png").toString());
        Image dataClassImage2 = new Image(getClass().getResource("/images/class.png").toString());
        Image personalImage2 = new Image(getClass().getResource("/images/personal.png").toString());


        dashBoardImageView2.setImage(dashBoardImage2);
        DataImageView2.setImage(dataImage2);
        DataClassImageView2.setImage(dataClassImage2);
        PersonalImageView2.setImage(personalImage2);

        showTable(sectionList);

        sectionTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Section>() {
            @Override
            public void changed(ObservableValue<? extends Section> observable, Section oldValue, Section newValue) {
                if (newValue == null) {
                    clearSectionInfo();
                    selectedSection = null;
                } else {
                    showSectionInfo(newValue);
                    selectedSection = newValue;
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
                    loginImageView2.setImage(profileImage);
                } else {

                    loginImageView2.setImage(new Image(getClass().getResourceAsStream("/images/default-profile.png")));
                }
                break;
            }
        }
    }


    private void clearSectionInfo() {
        facultyIdLabel.setText("");
        facultyNameLabel.setText("");

    }

    private void showSectionInfo(Section section) {
        facultyIdLabel.setText(section.getFacultyId());
        facultyNameLabel.setText(section.getFacultyName());
    }

    @FXML
    public void showTable(SectionList sectionList) {


        TableColumn<Section, String> facultyName = new TableColumn<>("Faculty Name");
        facultyName.setCellValueFactory(new PropertyValueFactory<>("facultyName"));

        TableColumn<Section, String> facultyId = new TableColumn<>("Faculty ID");
        facultyId.setCellValueFactory(new PropertyValueFactory<>("facultyId"));

        sectionTableView.getColumns().clear();
        sectionTableView.getColumns().addAll(facultyId,facultyName);
        sectionTableView.getItems().clear();

        for (Section section : sectionList.getSections()) {
            if(!section.getFacultyId().equals("facultyId") && !section.getFacultyName().equals("facultyName")){
                sectionTableView.getItems().add(section);
            }

        }

    }

    private void onSearchChange(String searchTerm) {

        sectionTableView.getItems().clear();

        searchTerm = searchTerm.trim().toLowerCase();

        if (searchTerm.trim().isEmpty()) {
            showTable(sectionList);
            return;
        };

        for(Section section : sectionList.getSections()) {
            String facultyId = section.getFacultyId().toLowerCase();
            String facultyName = section.getFacultyName().toLowerCase();

            if(facultyId.contains(searchTerm) || facultyName.contains(searchTerm)) {
                if(!section.getFacultyId().equals("facultyId")){
                    sectionTableView.getItems().add(section);
                }
            }
        }

    }

    @FXML
    private void onAddMoreButtonClicked(){

        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ku/cs/views/addSectionPopup.fxml"));
            Parent root = loader.load();

            AddMoreSectionPopupController controller = loader.getController();

            controller.setDatasource(datasource);
            controller.setCurrentList(sectionList);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Add More Faculty");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();


            showTable(sectionList);

        }catch (IOException e) {
            e.printStackTrace();
        }


    }


    @FXML
    private void OnChangeEditButtonClicked() {
        Section selected = sectionTableView.getSelectionModel().getSelectedItem();
        if(selected != null) {
            try{
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ku/cs/views/SectionEditPopup.fxml"));
                Parent root = loader.load();

                EditSectionPopupController controller = loader.getController();
                controller.setSection(selected);
                controller.setDatasource(datasource);
                controller.setCurrentList(sectionList);

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Edit Section Info");
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait();


                showTable(sectionList);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please select a entry to edit.");
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

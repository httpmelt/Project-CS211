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
import ku.cs.models.admin.Data;
import ku.cs.models.admin.DataList;
import ku.cs.models.admin.Personal;
import ku.cs.models.admin.PersonalList;
import ku.cs.services.admin.DataListCodeFileDatasource;
import ku.cs.services.Datasource;
import ku.cs.services.FXRouter;
import ku.cs.services.admin.PersonalListCodeFileDatasource;


import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class DataController {
    @FXML
    private ImageView sampleImageView;
    @FXML
    private ImageView dashBoardImageView1;
    @FXML
    private ImageView DataImageView1;
    @FXML
    private ImageView DataClassImageView1;
    @FXML
    private ImageView PersonalImageView1;
    @FXML
    private ImageView loginImageView1;


    @FXML private TableView<Data> DataTableView;

    @FXML private TextField searchTextField;

    @FXML private ComboBox<String> RoleNameComboBox;

    @FXML private Label adminnameLabel;
    @FXML private Label adminIdLabel;
    @FXML private ImageView adminImage;


    private DataList dataList;
    private Datasource<DataList> datasource;
    private Data selectedData;

    private Datasource<PersonalList> personalDatasource;
    private PersonalList personalList;

    @FXML private Label usernameLabel;
    @FXML private Label nameLabel;
    @FXML private Label roleNameLabel;
    @FXML private Label statusAccess;
    @FXML private Label timeLabel;

    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    String Profile;

    @FXML
    public void initialize() {

        clearDataInfo();
        datasource = new DataListCodeFileDatasource("data/user", "Allusers.csv");
        dataList = datasource.readData();

        personalDatasource = new PersonalListCodeFileDatasource("data/user", "Staffuser.csv");
        personalList = personalDatasource.readData();

        showAdminInformation();

        for(Personal personal: personalList.getPersonalsList()){
            if(personal.getRole().equals("ผู้ดูแลระบบ")){
                Profile = personal.getDefaultProfilePic();
                break;
            }
        }




        Image image =  new Image(getClass().getResource("/images/default-profile-pic.png").toString());
        Image dataImage = new Image(getClass().getResource("/images/person.png").toString());
        Image dashBoardImage = new Image(getClass().getResource("/images/board.png").toString());
        Image dataClassImage = new Image(getClass().getResource("/images/class.png").toString());
        Image personalImage = new Image(getClass().getResource("/images/personal.png").toString());



        sampleImageView.setImage(image);
        dashBoardImageView1.setImage(dashBoardImage);
        DataImageView1.setImage(dataImage);
        DataClassImageView1.setImage(dataClassImage);
        PersonalImageView1.setImage(personalImage);

        showTable(dataList);

        RoleNameComboBox.getItems().addAll(
                "ทั้งหมด",
                "เจ้าหน้าที่ภาควิชา",
                "เจ้าหน้าที่คณะ",
                "อาจารย์ที่ปรึกษา",
                "นิสิต"
        );

        RoleNameComboBox.setValue("ทั้งหมด");

        RoleNameComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            showTable(dataList); // Refresh the table whenever the selected role changes
        });



        DataTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Data>() {
            @Override
            public void changed(ObservableValue<? extends Data> observable, Data oldValue, Data newValue) {
                if (newValue == null) {
                    clearDataInfo();
                    selectedData = null;
                } else {
                    showStudentInfo(newValue);
                    selectedData = newValue;
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
                adminIdLabel.setText(personal.getUsername());
                adminIdLabel.setStyle("-fx-text-fill: white;");
                String profilePicPath = "data/profile-pic/" + personal.getDefaultProfilePic();
                File profilePicFile = new File(profilePicPath);
                if (profilePicFile.exists()) {
                    Image profileImage = new Image(profilePicFile.toURI().toString());
                    loginImageView1.setImage(profileImage);
                } else {
                    // Handle case where the image does not exist (e.g., set a default image)
                    loginImageView1.setImage(new Image(getClass().getResourceAsStream("/images/default-profile.png")));
                }
                break;
            }
        }
    }

    private void showStudentInfo(Data data) {
        usernameLabel.setText(data.getUsername());
        nameLabel.setText(data.getName() + " " + data.getSurname());
        roleNameLabel.setText("("+data.getRole()+")");
        timeLabel.setText(data.getTime().format(dateTimeFormatter).toString());


        String profilePicPath = "data/profile-pic/" + data.getProfile();
        File profilePicFile = new File(profilePicPath);
        if (profilePicFile.exists()) {
            Image profileImage = new Image(profilePicFile.toURI().toString());
            sampleImageView.setImage(profileImage);
        } else {
            // Handle case where the image does not exist (e.g., set a default image)
            sampleImageView.setImage(new Image(getClass().getResourceAsStream("/images/default-profile.png")));
        }

    }

    private void clearDataInfo() {
        usernameLabel.setText("");
        nameLabel.setText("");
        roleNameLabel.setText("");
        timeLabel.setText("");
    }




    private void showTable(DataList dataList) {
        TableColumn<Data, String> usernameColumn = new TableColumn<>("Username");
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<Data, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Data, String> surnameColumn = new TableColumn<>("Surname");
        surnameColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));

        TableColumn<Data, LocalDateTime> timeColumn = new TableColumn<>("Time");
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));

        timeColumn.setCellFactory(col -> new TableCell<Data, LocalDateTime>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.format(dateTimeFormatter));
                }
            }
        });

        timeColumn.setSortable(true);
        timeColumn.setComparator((LocalDateTime a, LocalDateTime b) -> b.compareTo(a)); // Change to newest to oldest

        DataTableView.getColumns().clear();
        DataTableView.getColumns().addAll(nameColumn, surnameColumn, usernameColumn, timeColumn);

        DataTableView.getItems().clear();

        String role = RoleNameComboBox.getValue();
        if (role == null || role.equals("ทั้งหมด")) {
            for (Data data : dataList.getDataList()) {
                if (!data.getSurname().equals("surname")) {
                    DataTableView.getItems().add(data);
                }
            }
        } else {
            for (Data data : dataList.getDataList()) {
                if (data.getRole().equals(role) && !data.getSurname().equals("surname")) {
                    DataTableView.getItems().add(data);
                }
            }
        }

        // Optional: Sort by time column after loading items
        DataTableView.getSortOrder().add(timeColumn);
    }




    @FXML
    private void HandlePermissionButtonClick(){
        Data selected = DataTableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ku/cs/views/EditUserPermission.fxml"));
                Parent root = loader.load();

                EditDataPermissionController controller = loader.getController();
                controller.setData(selected);
                controller.setDatasource(datasource);
                controller.setCurrentList(dataList);

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Edit Personal Info");
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait();


                showTable(dataList); // Refresh the display

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please select a personal entry to edit.");
            alert.showAndWait();
        }
    }



    private void onSearchChange(String searchTerm) {
        DataTableView.getItems().clear();
        searchTerm = searchTerm.trim().toLowerCase();

        String role = RoleNameComboBox.getValue();

        for (Data data : dataList.getDataList()) {
            String name = data.getName().toLowerCase();
            String username = data.getUsername().toLowerCase();
            String surname = data.getSurname().toLowerCase();


            if (name.contains(searchTerm) || username.contains(searchTerm) || surname.contains(searchTerm)) {
                if (!data.getSurname().equals("surname")) {
                    if (role == null || role.equals("ทั้งหมด")) {
                        DataTableView.getItems().add(data);
                    } else {
                        if (data.getRole().equals(role)) {
                            DataTableView.getItems().add(data);
                        }
                    }
                }
            }
        }

    }

    public void onOptionButtonClick(){
        try {
            Map<String, Object> forOption = new HashMap<>();
            forOption.put("username", "admin");
            forOption.put("name", "ชัยยงค์ คึมยะราช");
            forOption.put("profilePic", Profile);
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
            e.printStackTrace();
        }
    }

    public void onBoardToDataClick() {
        try {
            FXRouter.goTo("data");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void OnBoardToClassClick() {
        try {
            FXRouter.goTo("section");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void OnBoardToPersonalClick() {
        try {
            FXRouter.goTo("personal");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
package ku.cs.controllers.admin;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import ku.cs.models.admin.DataList;
import ku.cs.models.admin.Personal;
import ku.cs.models.admin.PersonalList;
import ku.cs.services.Datasource;
import ku.cs.services.FXRouter;
import ku.cs.services.admin.DataListCodeFileDatasource;
import ku.cs.services.admin.PersonalListCodeFileDatasource;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AdminController {
    @FXML
    private ImageView dashBoardImageView;
    @FXML
    private ImageView DataImageView;
    @FXML
    private ImageView DataClassImageView;
    @FXML
    private ImageView PersonalImageView;
    @FXML
    private ImageView loginImageView;

    @FXML private Label numberOfUsersLabel;

    @FXML private Label adminnameLabel;
    private Datasource<PersonalList> personalDatasource;
    private PersonalList personalList;

    private Datasource<DataList> dataListDatasource;
    private DataList dataList;

    String Profile;

    @FXML private Label adminId;

    @FXML
    public void initialize() {

        dataListDatasource = new DataListCodeFileDatasource("data/user", "Allusers.csv");
        dataList = dataListDatasource.readData();


        personalDatasource = new PersonalListCodeFileDatasource("data/user", "Staffuser.csv");
        personalList = personalDatasource.readData();

        showAdminInformation();

        for(Personal personal: personalList.getPersonalsList()){
            if(personal.getRole().equals("ผู้ดูแลระบบ")){
                Profile = personal.getDefaultProfilePic();
                break;
            }
        }





        Image image = new Image(getClass().getResource("/images/person.png").toString());
        Image image1 = new Image(getClass().getResource("/images/personal.png").toString());
        Image image2 = new Image(getClass().getResource("/images/class.png").toString());
        Image image3 = new Image(getClass().getResource("/images/board.png").toString());

        dashBoardImageView.setImage(image3);
        DataImageView.setImage(image);
        DataClassImageView.setImage(image2);
        PersonalImageView.setImage(image1);


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
                    loginImageView.setImage(profileImage);
                } else {
                    // Handle case where the image does not exist (e.g., set a default image)
                    loginImageView.setImage(new Image(getClass().getResourceAsStream("/images/default-profile.png")));
                }
                break;
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

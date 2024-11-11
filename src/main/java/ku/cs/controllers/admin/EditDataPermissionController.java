package ku.cs.controllers.admin;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import ku.cs.models.admin.Data;
import ku.cs.models.admin.DataList;
import ku.cs.services.Datasource;

import java.io.File;
import java.time.format.DateTimeFormatter;

public class EditDataPermissionController {
    @FXML private Label usernameLabel;
    @FXML private Label nameLabel;
    @FXML private Label statusLabel;
    @FXML private Label roleLabel;
    @FXML private Label timeLabel;
    @FXML private Button saveButton;
    @FXML private ImageView userProfileImageView;

    private Datasource<DataList> datasource;
    private DataList currentDataList;
    private Data data;

    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void setDatasource(Datasource<DataList> datasource) {
        this.datasource = datasource;
    }

    public void setCurrentList(DataList dataList) {
        this.currentDataList = dataList;
    }

    public void setData(Data selected) {
        this.data = selected;
        updateUI();
    }

    private void updateUI() {

        usernameLabel.setText(data.getUsername());
        nameLabel.setText(data.getName() + " " + data.getSurname());
        roleLabel.setText("(" + data.getRole() + ")");
        timeLabel.setText(dateTimeFormatter.format(data.getTime()));

        String profilePicPath = "data/profile-pic/" + data.getProfile();
        File profilePicFile = new File(profilePicPath);

        if (profilePicFile.exists()) {
            Image profileImage = new Image(profilePicFile.toURI().toString());
            userProfileImageView.setImage(profileImage);
        } else {
            userProfileImageView.setImage(new Image(getClass().getResourceAsStream("/images/default-profile.png")));
        }


    }



    @FXML
    public void onSaveButtonClicked() {
        currentDataList.updateDataList(data);
        datasource.writeData(currentDataList);

        Stage stage = (Stage) usernameLabel.getScene().getWindow();
        stage.close();
    }
}
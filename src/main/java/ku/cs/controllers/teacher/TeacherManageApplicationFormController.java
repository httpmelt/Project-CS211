package ku.cs.controllers.teacher;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import ku.cs.controllers.student.StudentApplicationListController;
import ku.cs.services.FXRouter;
import ku.cs.models.student.StudentReq;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class TeacherManageApplicationFormController extends StudentApplicationListController {

    @FXML
    private ImageView profilePicLabel;

    @FXML
    private TableView<StudentReq> studentApplicationFormTableView;

    private String name, username, role, teacherId, profilePic;

    @FXML
    private Label nameLabel;

    @FXML
    public void onToStudentTableListButtonClick() {
        try {
            FXRouter.goTo("Teacher_StudentTableList");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onToCheckStudentApplicationHistoryButtonClick() {
        try {
            FXRouter.goTo("TeacherManagement");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onLogoutClick() {
        try {
            FXRouter.goTo("homepage");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void goToOptionButton() {
        try {
            Map<String, Object> forOption = new HashMap<>();
            forOption.put("username", username);
            forOption.put("name", name);
            forOption.put("profilePic", profilePic);
            forOption.put("role", role);
            FXRouter.goTo("change-password", forOption);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void initialize() {
        Map<String, Object> data = (Map<String, Object>) FXRouter.getData();
        name = (String) data.get("name");
        username = (String) data.get("username");
        profilePic = (String) data.get("profilePic");
        teacherId = (String) data.get("teacherId");
        role = (String) data.get("role");
        nameLabel.setText(name);

        System.out.println(teacherId);
        setProfilePicture();

        showStudentReqListByTeacherId(studentApplicationFormTableView, teacherId);

        studentApplicationFormTableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                StudentReq selectedItem = studentApplicationFormTableView.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    try {
                        FXRouter.goTo("ReqFormDetail", String.join(",", "TeacherManagement", String.valueOf(selectedItem.getReqId())));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    private void setProfilePicture() {
        if (profilePic != null && !profilePic.isEmpty()) {
            Path profilePicPath = Paths.get("data/profile-pic/", profilePic);
            File profilePicFile = profilePicPath.toFile();
            if (profilePicFile.exists()) {
                Image userImage = new Image(profilePicFile.toURI().toString());
                profilePicLabel.setImage(userImage);
            } else {
                Image defaultPic = new Image(getClass().getResource("/default-profile-pic.png").toString());
                profilePicLabel.setImage(defaultPic);
            }
        } else {
            Image defaultPic = new Image(getClass().getResource("/default-profile-pic.png").toString());
            profilePicLabel.setImage(defaultPic);
        }
    }
}

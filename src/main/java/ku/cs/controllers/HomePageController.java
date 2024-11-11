package ku.cs.controllers;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import ku.cs.services.FXRouter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HomePageController {
    @FXML private ImageView sampleImageView;
    @FXML private ImageView sampleImageView2;

    @FXML
    public void initialize() {
        Image image = new Image(getClass().getResourceAsStream("/images/login_page.jpg"));
        sampleImageView.setImage(image);

        Image image2 = new Image(getClass().getResourceAsStream("/images/ku_logo1.png"));
        sampleImageView2.setImage(image2);
    }
    @FXML
    private void onOfficerButtonClick() {
        try {
            Map<String, Object> forStaff = new HashMap<>();
            forStaff.put("staff", "staff");
            FXRouter.goTo("staff-login",forStaff);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void onStudentButtonClick() {
        try {
            Map<String, Object> forStudent = new HashMap<>();
            forStudent.put("student", "student");
            FXRouter.goTo("StudentLogin",forStudent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void btToCreatorPage() {
        try {
            FXRouter.goTo("creatorPage");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void goToInstructionPage() {
        try {
            FXRouter.goTo("instruction");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

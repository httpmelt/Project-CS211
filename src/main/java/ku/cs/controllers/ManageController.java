package ku.cs.controllers;

import javafx.fxml.FXML;
import ku.cs.services.FXRouter;
import javafx.scene.control.ComboBox;

import java.io.IOException;

public class ManageController {
    @FXML
    private void onNextButtonClick() {
        try {
            FXRouter.goTo("manage");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    private void nextButtonClick() {
        try {
            FXRouter.goTo("list-name");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    private ComboBox<String> actionComboBox;
    @FXML
    public void initialize() {

        actionComboBox.getItems().addAll("หัวหน้าภาควิชาวิทยาการคอมพิวเตอร์", "คณบดีคณะวิทยาศาสตร์", "ปฎิเสธ");
    }
    @FXML
    private void goNextClick() {
        try {
            FXRouter.goTo("faculty-staff-list");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

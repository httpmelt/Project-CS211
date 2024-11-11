package ku.cs.controllers;

import javafx.fxml.FXML;
import ku.cs.services.FXRouter;

import java.io.IOException;

public class ListNameController {
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
    private void goNextClick() {
        try {
            FXRouter.goTo("faculty-staff-list");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

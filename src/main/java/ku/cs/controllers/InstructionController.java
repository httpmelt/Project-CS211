package ku.cs.controllers;

import javafx.fxml.FXML;
import ku.cs.services.FXRouter;

import java.io.IOException;

public class InstructionController {

    @FXML
    void goToLoginPageButton() {
        try {
            FXRouter.goTo("homepage");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}

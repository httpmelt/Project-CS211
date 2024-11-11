package ku.cs.controllers;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import ku.cs.services.FXRouter;

import java.io.IOException;

public class CreatorPageController {



    @FXML
    private void btToHomePage() {
        try {
            FXRouter.goTo("homepage");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }





}

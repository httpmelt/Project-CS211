package ku.cs.application;


import ku.cs.services.FXRouter;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXRouter.bind(this, stage, "Hello World");
        configRoute();
        FXRouter.goTo("personal");
    }

    public static void configRoute() {
        String viewPath = "ku/cs/views/";
        FXRouter.when("hello", viewPath + "hello-view.fxml");
        FXRouter.when("faculty-staff-approved", viewPath + "faculty-staff-approved.fxml");
        FXRouter.when("faculty-staff-management", viewPath + "faculty-staff-management.fxml");
        FXRouter.when("faculty-staff-management-table", viewPath + "faculty-staff-showTable.fxml");
        FXRouter.when("faculty-staff-table", viewPath + "faculty-staff-managementTable.fxml");
        FXRouter.when("login", viewPath + "login-admin-staff-advisor.fxml");

    }
}

package ku.cs.controllers.departmentStaff;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import ku.cs.services.FXRouter;
import ku.cs.models.Person;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
// หน้าเพิ่มว่าใครเป็นคนอนุมัติ nextButtonClick
public class DepartmentNameListController {

    @FXML
    private TextField firstNameField;
    @FXML
    private TextField facultyNameField;
    @FXML
    private TextField positionField;
    @FXML
    private Button addButton;
    @FXML
    private Button resetButton;  // ปุ่มรีเซ็ตข้อมูล
    @FXML
    private ListView<Person> namelist;

    @FXML
    private ImageView profilePicLabel;

    @FXML
    private Label nameLabel;

    private String majorId, username, name, profilePic, role ,majorIdFromTeacher,departmentId;

    private ObservableList<Person> personList = FXCollections.observableArrayList();
    private final String FILE_PATH = "data/staff/person_data.csv";  // ไฟล์ที่ใช้บันทึกข้อมูล

    @FXML
    private void initialize() {

        Map<String, Object> data = (Map<String, Object>) FXRouter.getData();
        name = (String) data.get("name");
        username = (String) data.get("username");
        profilePic = (String) data.get("profilePic");
        majorId = (String) data.get("majorId");  // รหัสภาควิชา
        role = (String) data.get("role");
        majorIdFromTeacher = (String) data.get("majorIdFromTeacher");
        departmentId = (String) data.get("departmentId");

        namelist.setItems(personList);

        addButton.setOnAction(event -> addData());

        loadDataFromFile();
    }

    private void setProfilePicture() {
        if (profilePic != null && !profilePic.isEmpty()) {
            Path profilePicPath = Paths.get("data/profile-pic/", profilePic);
            File profilePicFile = profilePicPath.toFile();
            if (profilePicFile.exists()) {
                Image userImage = new Image(profilePicFile.toURI().toString());
                profilePicLabel.setImage(userImage);
            } else {
                // หากหาไฟล์รูปโปรไฟล์ไม่เจอ ให้ใช้ default แทน
                Image defaultPic = new Image(getClass().getResource("data/profile-pic/default-profile-pic.png").toString());
                profilePicLabel.setImage(defaultPic);
            }
        } else {
            // หากผู้ใช้ไม่มีรูปโปรไฟล์ ให้ใช้รูป default
            Image defaultPic = new Image(getClass().getResource("data/profile-pic/default-profile-pic.png").toString());
            profilePicLabel.setImage(defaultPic);
        }
    }

    private void addData() {
        String firstName = firstNameField.getText().trim();
        String lastName = facultyNameField.getText().trim();
        String position = positionField.getText().trim();

        if (firstName.isEmpty() || lastName.isEmpty() || position.isEmpty()) {
            System.out.println("Please fill in all fields before adding data.");
            showAlert();
            return;
        }

        Person person = new Person(firstName, lastName, position);
        personList.add(person);

        saveDataToFile();

        firstNameField.clear();
        facultyNameField.clear();
        positionField.clear();
    }

    private void showAlert() {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Incomplete Information");
        alert.setHeaderText("Input Error");
        alert.setContentText("The information is incomplete. Please fill in all fields.");

        alert.showAndWait();
    }

    private void saveDataToFile() {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(FILE_PATH), StandardCharsets.UTF_8))) {
            for (Person person : personList) {
                writer.write(person.getFirstName() + "," + person.getLastName() + "," + person.getPosition());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadDataFromFile() {
        File file = new File(FILE_PATH);
        if (file.exists()) {
            try (Scanner scanner = new Scanner(file, StandardCharsets.UTF_8)) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    String[] data = line.split(",");
                    if (data.length == 3) {
                        Person person = new Person(data[0].trim(), data[1].trim(), data[2].trim());
                        personList.add(person);
                        System.out.println("Loaded: " + person);
                    }
                }
                System.out.println("Total persons loaded: " + personList.size());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("File not found: " + FILE_PATH);
        }
    }

    @FXML
    private void handleResetButtonClick() {
        Person selectedPerson = namelist.getSelectionModel().getSelectedItem();
        if (selectedPerson != null) {
            personList.remove(selectedPerson);

            saveDataToFile();

            System.out.println("Selected person has been removed: " + selectedPerson);
        } else {
            System.out.println("No person selected for removal.");
            showNoSelectionAlert(); // แสดง alert หากไม่ได้เลือกแถวใด
        }
    }

    private void showNoSelectionAlert() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("No Selection");
        alert.setHeaderText("No Person Selected");
        alert.setContentText("Please select a person in the list to remove.");

        alert.showAndWait();
    }

    @FXML
    public void onNextButtonClick() {
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("majorIdFromTeacher", majorIdFromTeacher);
            data.put("name", name);
            data.put("username", username);
            data.put("profilePic", profilePic);
            data.put("role", "เจ้าหน้าที่ภาควิชา");
            FXRouter.goTo("manage", data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void nextButtonClick() {
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("majorIdFromTeacher", majorIdFromTeacher);
            data.put("name", name);
            data.put("username", username);
            data.put("profilePic", profilePic);
            data.put("role", "เจ้าหน้าที่ภาควิชา");
            FXRouter.goTo("edit-name-list",data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void goNextClick() {
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("majorId", majorIdFromTeacher);
            data.put("name", name);
            data.put("username", username);
            data.put("profilePic", profilePic);
            data.put("role", "เจ้าหน้าที่ภาควิชา");
            FXRouter.goTo("faculty-staff-list",data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void goingToNextClick() {
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("majorIdFromTeacher", majorIdFromTeacher);
            data.put("name", name);
            data.put("username", username);
            data.put("profilePic", profilePic);
            data.put("role", "เจ้าหน้าที่ภาควิชา");
            FXRouter.goTo("manage-student",data);
        } catch (IOException e) {
            e.printStackTrace();
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
    void goOptionButton() {
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
}

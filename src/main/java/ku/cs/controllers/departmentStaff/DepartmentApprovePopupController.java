package ku.cs.controllers.departmentStaff;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ku.cs.controllers.ApplicationApprovalController;
import ku.cs.models.department.DepartmentApproverList;
import ku.cs.services.department.DepartmentApproverListDatasource;
import ku.cs.services.FXRouter;
import ku.cs.services.department.DepartmentUserListFileDatasource;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DepartmentApprovePopupController extends ApplicationApprovalController {

    @FXML
    private AnchorPane approveAnchor;

    @FXML
    private ComboBox<String> approverComboBox;

    @FXML
    private AnchorPane rejectAnchor;

    @FXML
    private TextArea rejectTextField;

    @FXML
    private Label fileNameLabel;

    @FXML
    private CheckBox approveCheckBox;

    @FXML
    private CheckBox rejectCheckBox;

    @FXML
    private CheckBox sendTo;


    private String departmentId, name, username, profilePic, role, formName, majorId;
    private int reqId, reqProcessId;

    private String rejectReason;
    private String selectedApprover;
    private String attachedFileName;

    private DepartmentApproverListDatasource approverListDatasource;
    private DepartmentApproverList approverList;
    private DepartmentUserListFileDatasource userListFileDatasource;

    // Map สำหรับเก็บ departmentId และ departmentName
    private Map<String, String> departmentIdToNameMap = new HashMap<>();

    public void setData(String departmentId, String name, String username, String profilePic, String role, int reqId, int reqProcessId, String formName) {
        this.departmentId = departmentId;
        this.name = name;
        this.username = username;
        this.profilePic = profilePic;
        this.role = role;
        this.reqId = reqId;
        this.reqProcessId = reqProcessId;
        this.formName = formName;
    }

    @FXML
    public void initialize() {
        // รับข้อมูลจาก FXRouter
        Map<String, Object> data = (Map<String, Object>) FXRouter.getData();
        departmentId = (String) data.get("departmentId");
        name = (String) data.get("name");
        username = (String) data.get("username");
        profilePic = (String) data.get("profilePic");
        role = (String) data.get("role");
        formName = (String) data.get("formName");
        reqId = (int) data.get("requestId");
        reqProcessId = (int) data.get("reqProcessId");
        majorId = (String) data.get("majorId");


        approveAnchor.setVisible(false);
        rejectAnchor.setVisible(false);

        userListFileDatasource = new DepartmentUserListFileDatasource("data/user", "department.csv");
        departmentIdToNameMap = userListFileDatasource.readDepartmentIdToNameMap();
        String departmentName = departmentIdToNameMap.get(departmentId);

        approverListDatasource = new DepartmentApproverListDatasource("data/staff", "person_data.csv");
        approverList = approverListDatasource.readData();


        populateApproverComboBox(departmentName);
    }

    private void populateApproverComboBox(String departmentName) {
        List<String> approversInDepartment = approverList.getDepartmentApprovers().stream()
                .map(approver -> approver.getName() + " " + approver.getSurname() + " - " + approver.getRole())
                .collect(Collectors.toList());

        System.out.println("Approvers found: " + approversInDepartment.size());
        approversInDepartment.forEach(System.out::println); // แสดงรายชื่อผู้อนุมัติที่พบ

        approverComboBox.getItems().clear();
        approverComboBox.getItems().addAll(approversInDepartment);
        System.out.println(reqId);
    }



    // เมื่อคลิก CheckBox "อนุมัติคำร้อง"
    @FXML
    public void onApproveCheckBoxAction() {
        if (approveCheckBox.isSelected()) {
            approveAnchor.setVisible(true);
            rejectCheckBox.setSelected(false);
            sendTo.setSelected(false);  // ยกเลิกการเลือกใน CheckBox อื่น
            rejectAnchor.setVisible(false);
            rejectTextField.clear(); // ล้างข้อมูลเหตุผลการปฏิเสธ
        } else {
            approveAnchor.setVisible(false);
        }
    }

    @FXML
    public void onSendtCheckBoxAction() {
        if (sendTo.isSelected()) {
            approveAnchor.setVisible(true); // แสดงส่วนของการอนุมัติ
            rejectCheckBox.setSelected(false);
            approveCheckBox.setSelected(false);  // ยกเลิกการเลือกใน CheckBox อื่น
            rejectAnchor.setVisible(false);
            rejectTextField.clear(); // ล้างข้อมูลเหตุผลการปฏิเสธ
        } else {
            approveAnchor.setVisible(false); // ซ่อนส่วนของการอนุมัติเมื่อยกเลิกการเลือก
        }
    }


    // เมื่อคลิกปุ่มแนบไฟล์
    @FXML
    public void onAttachFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            try {
                Path directoryPath = Paths.get("data/ApprovedByStaff");
                if (!Files.exists(directoryPath)) {
                    Files.createDirectories(directoryPath);
                }

                String requestNumber = String.valueOf(reqId);
                String fileExtension = ".pdf";
                Path destinationPath = directoryPath.resolve("ใบอนุมัติ_ใบคำร้องที่" + requestNumber + "" + formName + "" + fileExtension);

                Files.copy(selectedFile.toPath(), destinationPath);

                fileNameLabel.setText(selectedFile.getName());
                attachedFileName = selectedFile.getName(); // กำหนดค่าให้ attachedFileName หลังจากแนบไฟล์สำเร็จ

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    // เมื่อคลิก CheckBox "ปฏิเสธคำร้อง"
    @FXML
    public void onRejectCheckBoxAction() {
        if (rejectCheckBox.isSelected()) {
            rejectAnchor.setVisible(true);
            approveCheckBox.setSelected(false);
            sendTo.setSelected(false); // ยกเลิกการเลือกใน CheckBox อื่น
            approveAnchor.setVisible(false);
        } else {
            rejectAnchor.setVisible(false);
        }
    }



    // ตรวจสอบเหตุผลการปฏิเสธ
    public boolean validateRejectReason() {
        rejectReason = rejectTextField.getText().trim();
        if (rejectReason.isEmpty()) {
            rejectTextField.setPromptText("กรุณาระบุเหตุผลในการปฏิเสธ");
            return false;
        }
        return true;
    }

    // เมื่อคลิกปุ่ม "ดำเนินการ"
    public void onSubmitStatusButton() {
        selectedApprover = approverComboBox.getSelectionModel().getSelectedItem();

        // กรณีอนุมัติคำร้อง
        if (approveCheckBox.isSelected() && selectedApprover != null && fileNameLabel != null) {

            approveSubmit(reqId, 4); // อัพเดตสถานะเป็นอนุมัติโดยหัวหน้าภาควิชา
            System.out.println("Approve submitted.");

        } else if (sendTo.isSelected() && selectedApprover != null && fileNameLabel != null) {
            approveSubmit(reqId, 5); // ส่งต่อให้คณบดี
            System.out.println("Forward to Dean submitted.");

        } else if (rejectCheckBox.isSelected() && validateRejectReason()) {
            rejectSubmit(reqId, 6, rejectReason); // ปฏิเสธคำร้อง
            System.out.println("Rejection submitted with reason: " + rejectReason);
        } else {
            System.out.println("กรุณาเลือกผู้อนุมัติแนบไฟล์หรือระบุเหตุผลในการปฏิเสธ");
        }

        try {
            Map<String, Object> data = new HashMap<>();
            data.put("name", name);
            data.put("username", username);
            data.put("profilePic", profilePic);
            data.put("role", role);
            data.put("majorId", majorId);
            FXRouter.goTo("faculty-staff-list", data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        closePopup();
    }

    private void closePopup() {
        Stage stage = (Stage) approveAnchor.getScene().getWindow();
        stage.close();
    }

}

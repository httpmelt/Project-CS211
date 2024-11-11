package ku.cs.cs211671project;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ku.cs.services.FXRouter;

import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXRouter.bind(this, stage, "Student Request Application", 800, 600);
        configRoutes();
        FXRouter.goTo("homepage");
    }

    private void configRoutes() {
        String viewPath = "ku/cs/views/";

        FXRouter.when("homepage", viewPath + "homePage.fxml");
        FXRouter.when("creatorPage", viewPath + "creatorPage.fxml");

        FXRouter.when("faculty-staff-approved", viewPath + "faculty-staff-approved.fxml");
        FXRouter.when("faculty-staff-approver", viewPath + "faculty-staff-approver.fxml");
        FXRouter.when("faculty-staff-show-table", viewPath + "faculty-staff-showTable.fxml");
        FXRouter.when("faculty-staff-management-table", viewPath + "faculty-staff-managementTable.fxml");
        FXRouter.when("staff-login", viewPath + "login-admin-staff-advisor.fxml");
        FXRouter.when("faculty-staff-management", viewPath + "faculty-staff-management.fxml");
        FXRouter.when("change-password", viewPath + "change-password.fxml");
        FXRouter.when("student-change-password", viewPath + "student-change-password.fxml");
        FXRouter.when("option-page", viewPath + "option-page.fxml");
        FXRouter.when("approve-popup", viewPath + "approvePopup.fxml");
        FXRouter.when("instruction", viewPath + "instruction-page.fxml");


        FXRouter.when("StudentLogin", viewPath + "studentLogin.fxml");
        FXRouter.when("StudentRegister", viewPath + "StudentRegistration-view.fxml");
        FXRouter.when("EduFeeForm", viewPath + "eduFeeForm.fxml");
        FXRouter.when("ReqFormDetail", viewPath + "reqFormDetail.fxml");
        FXRouter.when("LeaveOfAbsenceForm", viewPath + "leaveOfAbsence.fxml");
        FXRouter.when("ApplicationStatus", viewPath + "StudentApplicationFormTracking-table.fxml");

        FXRouter.when("Teacher_StudentTableList", viewPath + "Teacher-ShowStudent-table.fxml");
        FXRouter.when("TeacherManagement", viewPath + "TeacherManageRequest-list.fxml");
        FXRouter.when("TeacherApplicationTracking", viewPath + "TeacherApplicationTracking-table.fxml");



        FXRouter.when("data", viewPath + "data-view.fxml");
        FXRouter.when("personal" , viewPath + "personal-view.fxml");
        FXRouter.when("section" , viewPath + "section-view.fxml");
        FXRouter.when("sectionTwo", viewPath + "sectionTwo-view.fxml");
        FXRouter.when("admin", viewPath + "admin-view.fxml");

        FXRouter.when("name-list", viewPath + "name-list.fxml");
        FXRouter.when("teacher", viewPath + "Teacher-ShowStudent-table.fxml");


        FXRouter.when("faculty-staff-list", viewPath + "faculty-staff-list.fxml"); // หน้าแสดงข้อมูลรายการคำร้องที่รอเราอนุมัติ goNextClick
        FXRouter.when("manage", viewPath + "department-staff-managementTable.fxml"); //หน้าจัดการข้อมูลที่เรากดจากรายการคำร้องที่เราอนุมัติ onNextButtonClick majorIdFromTeacher
        FXRouter.when("request-action", viewPath + "request_action.fxml");
        FXRouter.when("edit-name-list", viewPath + "name-list.fxml"); //หน้า กำหนดว่าใครเป็นคนอนุมัติ nextButtonClick departmentId
        FXRouter.when("manage-student", viewPath + "department-manage-student.fxml"); //หน้าแก้ไขข้อมูลเพิ่มข้อมูล student goingToNextClick
        FXRouter.when("edit-student", viewPath + "department-edit-student.fxml"); // หน้า edit
        FXRouter.when("add-student", viewPath + "department-add-student.fxml"); //หน้า add
        FXRouter.when("DepartmentStaffManagement", viewPath + "department-staff-management.fxml"); //หน้า manage จาก table ที่เลือก .

    }

    public static void main(String[] args) {

        launch();
    }
}
module ku.cs {
    requires javafx.controls;
    requires javafx.fxml;
    requires bcrypt;
    requires java.desktop;
    requires java.xml.crypto;
    requires jbcrypt;
    requires java.sql;

    opens ku.cs.cs211671project to javafx.fxml;
    exports ku.cs.cs211671project;
    exports ku.cs.controllers;
    exports ku.cs.controllers.faculty to javafx.fxml;
    opens ku.cs.controllers to javafx.fxml;
    opens ku.cs.controllers.faculty to javafx.fxml;

    exports ku.cs.models;
    opens ku.cs.models to javafx.base;
    exports ku.cs.controllers.departmentStaff;
    opens ku.cs.controllers.departmentStaff to javafx.fxml;

    exports ku.cs.controllers.student to javafx.fxml;
    opens ku.cs.controllers.student to javafx.fxml;
    exports ku.cs.controllers.teacher;
    opens ku.cs.controllers.teacher to javafx.fxml;
    exports ku.cs.models.student;
    opens ku.cs.models.student to javafx.base;
    exports ku.cs.controllers.admin;
    opens ku.cs.controllers.admin to javafx.fxml;
    exports ku.cs.models.admin;
    opens ku.cs.models.admin to javafx.base;
    exports ku.cs.models.teacher;
    opens ku.cs.models.teacher to javafx.base;
    exports ku.cs.models.user;
    opens ku.cs.models.user to javafx.base;
    exports ku.cs.models.faculty;
    opens ku.cs.models.faculty to javafx.base;


}
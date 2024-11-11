package ku.cs.models.student;

import ku.cs.services.student.StudentRegisterDatasource;

import java.util.ArrayList;

public class StudentRegisterList {

    public StudentRegisterList(ArrayList<StudentRegister> studentRegisters) {
        this.studentRegisters = studentRegisters;
      //  loadCsv();
    }

    private ArrayList<StudentRegister> studentRegisters;

    public StudentRegisterList() {
        studentRegisters = new ArrayList<>();
    }

    public void addNewStudentRegister(String username, String password, String studentId) {
        username = username.trim();
        password = password.trim();
        studentId = studentId.trim();

        studentRegisters.add(new StudentRegister(username, password, studentId));
    }


    public ArrayList<StudentRegister> getStudentRegisters() {
        return studentRegisters;
    }

   /* public void loadCsv(){
        StudentRegisterDatasource datasource = new StudentRegisterDatasource("data/student", "studentRegister.csv");
        StudentRegisterList studentRegisterList = datasource.readData();
    } */
}


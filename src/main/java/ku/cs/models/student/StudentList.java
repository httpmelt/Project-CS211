package ku.cs.models.student;

import ku.cs.models.user.User;

import java.util.ArrayList;

public class StudentList {
    private ArrayList<Student> students;

    public StudentList() {
        students = new ArrayList<>();
    }

    public void addNewStudent(String studentId, String name, String surname, String email, String teacherId) {
        studentId = studentId.trim();
        name = name.trim();
        surname = surname.trim();
        email = email.trim();
        teacherId = teacherId.trim();

        if (!studentId.equals("") && !name.equals("") && !surname.equals("") && !email.equals("") && !teacherId.equals("")) {
            Student existingStudent = findStudentById(studentId);
            if (existingStudent == null) {
                students.add(new Student(studentId, name, surname, email, teacherId));
            }
        }
    }

    public Student findStudentById(String studentId) {
        for (Student student : students) {
            if (student.getStudentId().equals(studentId)) {
                return student;
            }
        }
        return null;
    }


    public ArrayList<Student> getStudents() {
        return students;
    }


}



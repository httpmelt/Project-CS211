package ku.cs.models.teacher;

import java.util.ArrayList;

public class TeacherList {
    private ArrayList<Teacher> teachers;

    public TeacherList() {
        teachers = new ArrayList<>();
    }

    // Method to add a new teacher
    public void addNewTeacher(String defaultProfilePic, String role, String name, String surname, String username, String password, String facultyId, String majorId, String teacherId) {
        defaultProfilePic = defaultProfilePic.trim();
        role = role.trim();
        name = name.trim();
        surname = surname.trim();
        username = username.trim();
        password = password.trim();
        facultyId = facultyId.trim();
        majorId = majorId.trim();
        teacherId = teacherId.trim();

        // Ensure essential fields are not empty
        if (!role.equals("") && !name.equals("") && !surname.equals("") && !username.equals("") && !password.equals("") && !facultyId.equals("") && !majorId.equals("") && !teacherId.equals("")) {
            // Check if a teacher with the same teacherId already exists
            Teacher existingTeacher = findTeacherById(teacherId);
            if (existingTeacher == null) {
                // Add new teacher if not found
                teachers.add(new Teacher(defaultProfilePic, role, name, surname, username, password, facultyId, majorId, teacherId));
            }
        }
    }

    // Method to find a teacher by teacherId
    public Teacher findTeacherById(String teacherId) {
        for (Teacher teacher : teachers) {
            if (teacher.getTeacherId().equals(teacherId)) {
                return teacher;
            }
        }
        return null;
    }

    public Teacher findTeacherByUsername(String username) {
        for (Teacher teacher : teachers) {
            if (teacher.getUsername().equals(username)) {
                return teacher;
            }
        }
        return null;
    }

    // Method to get all teachers
    public ArrayList<Teacher> getTeachers() {
        return teachers;
    }
}

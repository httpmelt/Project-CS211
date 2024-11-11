package ku.cs.models.user;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class StaffList {
    private ArrayList<Staff> allOfficers;

    public StaffList() {
        allOfficers = new ArrayList<>();
    }

    // Method to add a new staff member
    public void addNewStaff(String defaultProfilePic, String role, String name, String surname, String username, String password, String facultyId, String majorId, String teacherId, boolean register, LocalDateTime time) {
        // Trimming input fields to avoid whitespace issues
        defaultProfilePic = defaultProfilePic.trim();
        role = role.trim();
        name = name.trim();
        surname = surname.trim();
        username = username.trim();
        password = password.trim();
        facultyId = facultyId.trim();
        majorId = majorId.trim();
        teacherId = teacherId.trim();
        register = register;

        allOfficers.add(new Staff(defaultProfilePic, role, name, surname, username, password, facultyId, majorId, teacherId, register,time));
    }

    public ArrayList<Staff> getAllOfficers() {
        return allOfficers;
    }

    public Staff findStaffByUsername(String username) {
        username = username.trim(); // Trim the search input
        for (Staff staff : allOfficers) {
            if (staff.getUsername().equals(username)) {
                return staff;
            }
        }
        return null;  // Return null if no staff is found
    }



    public void updateStaff(Staff updatedStaff) {
        for (int i = 0; i < allOfficers.size(); i++) {
            if (allOfficers.get(i).getUsername().equals(updatedStaff.getUsername())) {
                allOfficers.set(i, updatedStaff);
                return;
            }
        }
    }

    public boolean isUsernameTaken(String username) {
        return findStaffByUsername(username) != null;
    }

}

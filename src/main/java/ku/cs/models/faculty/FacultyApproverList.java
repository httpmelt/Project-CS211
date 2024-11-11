package ku.cs.models.faculty;

import java.util.ArrayList;
import java.util.List;

public class FacultyApproverList {
    private List<FacultyApprover> facultyApprovers;

    public FacultyApproverList() {
        facultyApprovers = new ArrayList<>();
    }

    public void addFacultyApprover(String name, String role, String faculty) {
        name = name.trim();
        role = role.trim();
        faculty = faculty.trim();
        if (!name.isEmpty() && !role.isEmpty() && !faculty.isEmpty() ) {
            FacultyApprover exist = findApproverByName(name);
            if (exist == null) {
                facultyApprovers.add(new FacultyApprover(name, role, faculty));
            }
        }
    }


    public FacultyApprover findApproverByName(String name) {
        for (FacultyApprover approver : facultyApprovers) {
            if (approver.getName().equalsIgnoreCase(name)) {
                return approver;
            }
        }
        return null;
    }

    public List<FacultyApprover> getAllFacultyApprovers() {
        return facultyApprovers;
    }

    public List<FacultyApprover> getFacultyApprovers() {
        return facultyApprovers;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Faculty Approvers:\n");
        for (FacultyApprover approver : facultyApprovers) {
            sb.append(approver.toString()).append("\n");
        }
        return sb.toString();
    }

    public void addFacultyApprover(FacultyApprover approver) {
        facultyApprovers.add(approver);
    }
}

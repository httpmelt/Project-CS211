package ku.cs.models.department;

import java.util.ArrayList;
import java.util.List;

public class DepartmentApproverList {
    private List<DepartmentApprover> departmentApprovers;

    public DepartmentApproverList() {
        departmentApprovers = new ArrayList<>();
    }

    public void addDepartmentApprover(String name, String surname, String role) {
        name = name.trim();
        surname = surname.trim();
        role = role.trim();
        if (!name.isEmpty() && !surname.isEmpty() && !role.isEmpty()) {
            DepartmentApprover exist = findApproverByNameAndSurname(name, surname);
            if (exist == null) {
                departmentApprovers.add(new DepartmentApprover(name, surname, role));
            }
        }
    }

    public DepartmentApprover findApproverByNameAndSurname(String name, String surname) {
        for (DepartmentApprover approver : departmentApprovers) {
            if (approver.getName().equalsIgnoreCase(name) && approver.getSurname().equalsIgnoreCase(surname)) {
                return approver;
            }
        }
        return null;
    }

    public List<DepartmentApprover> getAllDepartmentApprovers() {
        return departmentApprovers;
    }

    public List<DepartmentApprover> getDepartmentApprovers() {
        return departmentApprovers;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Department Approvers:\n");
        for (DepartmentApprover approver : departmentApprovers) {
            sb.append(approver.toString()).append("\n");
        }
        return sb.toString();
    }

    public void addDepartmentApprover(DepartmentApprover approver) {
        departmentApprovers.add(approver);
    }
}

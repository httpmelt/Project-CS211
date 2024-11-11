package ku.cs.services.department;

import ku.cs.models.department.DepartmentApprover;
import ku.cs.models.department.DepartmentApproverList;
import ku.cs.services.Datasource;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class DepartmentApproverListDatasource implements Datasource<DepartmentApproverList> {

    private String directoryName;
    private String fileName;

    public DepartmentApproverListDatasource(String directoryName, String fileName) {
        this.directoryName = directoryName;
        this.fileName = fileName;
        checkFileIsExisted();
    }

    private void checkFileIsExisted() {
        File dir = new File(directoryName);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String filePath = directoryName + File.separator + fileName;
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException("Error creating file: " + filePath, e);
            }
        }
    }

    @Override
    public DepartmentApproverList readData() {
        DepartmentApproverList departmentApproverList = new DepartmentApproverList();
        File file = new File(directoryName, fileName);

        try (BufferedReader reader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 3) { // Ensure there are at least 3 columns: name, surname, role
                    String approverName = data[0].trim();
                    String surname = data[1].trim();
                    String role = data[2].trim();

                    // Add the approver with name, surname, and role
                    departmentApproverList.addDepartmentApprover(approverName, surname, role);
                    System.out.println("Loaded Approver: " + approverName + " " + surname + " with Role: " + role);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading approver CSV file: " + e.getMessage());
        }

        return departmentApproverList;
    }

    @Override
    public void writeData(DepartmentApproverList data) {
        File file = new File(directoryName, fileName);

        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {
            for (DepartmentApprover approver : data.getAllDepartmentApprovers()) {
                String line = approver.getName() + "," + approver.getSurname() + "," + approver.getRole();
                writer.write(line);
                writer.newLine();
            }
            writer.flush();
        } catch (IOException e) {
            System.err.println("Error writing approver CSV file: " + e.getMessage());
        }
    }

    @Override
    public void writeData() {
        throw new UnsupportedOperationException("writeData() without parameters is not supported in this context.");
    }
}

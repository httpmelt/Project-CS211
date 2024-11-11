package ku.cs.services.faculty;

import ku.cs.models.faculty.FacultyApprover;
import ku.cs.models.faculty.FacultyApproverList;
import ku.cs.services.Datasource;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class FacultyApproverListDatasource implements Datasource<FacultyApproverList> {

    private String directoryName;
    private String fileName;

    public FacultyApproverListDatasource(String directoryName, String fileName) {
        this.directoryName = directoryName;
        this.fileName = fileName;
        checkFileIsExisted();
    }

    // Check if the file exists, if not, create it.
    private void checkFileIsExisted() {
        File dir = new File(directoryName);
        if (!dir.exists()) {
            dir.mkdirs(); // Ensure the directory exists
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
    public FacultyApproverList readData() {
        FacultyApproverList facultyApproverList = new FacultyApproverList();
        File file = new File(directoryName, fileName);

        try (BufferedReader reader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 3) { // Ensure there are at least 3 columns: name, role, faculty
                    String approverName = data[0].trim();
                    String role = data[1].trim();
                    String facultyName = data[2].trim();

                    // Now pass individual string parameters to the addFacultyApprover method
                    facultyApproverList.addFacultyApprover(approverName, role, facultyName);
                    System.out.println("Loaded Approver: " + approverName + " for Faculty: " + facultyName);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading approver CSV file: " + e.getMessage());
        }

        return facultyApproverList;
    }


    @Override
    public void writeData(FacultyApproverList data) {
        File file = new File(directoryName, fileName);

        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {
            for (FacultyApprover approver : data.getAllFacultyApprovers()) {
                String line = approver.getName() + "," + approver.getRole() + "," + approver.getFacultyName();
                writer.write(line);
                writer.newLine();  // Write a new line after each entry
            }
            writer.flush();  // Ensure all data is written to the file
        } catch (IOException e) {
            System.err.println("Error writing approver CSV file: " + e.getMessage());
        }
    }

    @Override
    public void writeData() {
        throw new UnsupportedOperationException("writeData() without parameters is not supported in this context.");
    }
}

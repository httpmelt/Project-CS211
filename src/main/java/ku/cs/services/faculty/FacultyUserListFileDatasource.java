package ku.cs.services.faculty;

import ku.cs.models.user.FacultyUser;
import ku.cs.models.user.FacultyUserList;
import ku.cs.services.Datasource;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class FacultyUserListFileDatasource implements Datasource<FacultyUserList> {
    private String directory;
    private String filename;

    public FacultyUserListFileDatasource(String directory, String filename) {
        this.directory = directory;
        this.filename = filename;
    }

    @Override
    public FacultyUserList readData() {
        FacultyUserList facultyUserList = new FacultyUserList();
        File file = new File(directory, filename);

        try (FileInputStream fileInputStream = new FileInputStream(file);
             InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);
             BufferedReader buffer = new BufferedReader(inputStreamReader)) {
            String line;
            while ((line = buffer.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 2) { // expecting 2 columns: facultyId, facultyName
                    String facultyId = data[0].trim();
                    String facultyName = data[1].trim();

                    if (!facultyId.isEmpty() && !facultyName.isEmpty()) {
                        FacultyUser facultyUser = new FacultyUser("", "faculty", "", "", "", "", facultyId, facultyName);
                        facultyUserList.addFacultyUser(facultyUser);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading faculty CSV file: " + e.getMessage());
        }

        return facultyUserList;
    }

    @Override
    public void writeData(FacultyUserList facultyUserList) {
        File tempFile = new File(directory, "temp_" + filename);

        try (
                OutputStream outputStream = new FileOutputStream(tempFile);
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
                BufferedWriter writer = new BufferedWriter(outputStreamWriter)
        ) {
            for (FacultyUser facultyUser : facultyUserList.getFacultyUsers()) {
                writer.write(facultyUser.getFacultyId() + "," + facultyUser.getFacultyName());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing faculty CSV file: " + e.getMessage());
        }

        // Optional: Rename tempFile to the original filename if needed.
        tempFile.renameTo(new File(directory, filename));
    }

    public Map<String, String> readFacultyIdToNameMap() {
        Map<String, String> facultyIdToNameMap = new HashMap<>();
        File file = new File(directory, filename);

        try (BufferedReader reader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            String line;

            // Assuming the first line is the header and skipping it
            reader.readLine(); // Skip header line

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    String facultyId = parts[0].trim();
                    String facultyName = parts[1].trim();

                    if (!facultyId.isEmpty() && !facultyName.isEmpty()) {
                        facultyIdToNameMap.put(facultyId, facultyName);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading faculty CSV file: " + e.getMessage());
        }

        return facultyIdToNameMap;
    }

    @Override
    public void writeData() {
        // Implement as needed
    }
}

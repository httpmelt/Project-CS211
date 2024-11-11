package ku.cs.services.major;

import ku.cs.models.user.MajorUser;
import ku.cs.models.user.MajorUserList;
import ku.cs.services.Datasource;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class MajorUserListFileDatasource implements Datasource<MajorUserList> {
    private String directory;
    private String filename;

    public MajorUserListFileDatasource(String directory, String filename) {
        this.directory = directory;
        this.filename = filename;
    }

    public Map<String, String> readMajorIdToNameMap() {
        Map<String, String> majorIdToNameMap = new HashMap<>();
        File file = new File(directory, filename);

        System.out.println("Looking for file at: " + file.getAbsolutePath());

        try (BufferedReader reader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 3) {
                    String majorId = data[0].trim();
                    String majorName = data[1].trim();
                    String facultyName = data[2].trim();

                    if (!majorId.isEmpty() && !majorName.isEmpty()) {
                        majorIdToNameMap.put(majorId, majorName);
                    } else {
                        System.out.println("CSV is invalid: " + line);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return majorIdToNameMap;
    }

    @Override
    public MajorUserList readData() {
        MajorUserList majorUserList = new MajorUserList();
        File file = new File(directory, filename);

        try (FileInputStream fileInputStream = new FileInputStream(file);
             InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);
             BufferedReader buffer = new BufferedReader(inputStreamReader)) {
            String line;
            while ((line = buffer.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 3) { // expecting 3 columns: majorId, majorName, facultyName
                    String majorId = data[0].trim();
                    String majorName = data[1].trim();
                    String facultyName = data[2].trim();

                    MajorUser majorUser = new MajorUser("", "major", "", "", "", "", majorId, majorName, facultyName);
                    majorUserList.addMajorUser(majorUser);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }

        return majorUserList;
    }

    @Override
    public void writeData(MajorUserList majorUserList) {
        File tempFile = new File(directory, "temp_" + filename);

        try (
                OutputStream outputStream = new FileOutputStream(tempFile);
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
                BufferedWriter writer = new BufferedWriter(outputStreamWriter)
        ) {
            for (MajorUser majorUser : majorUserList.getMajorUsers()) {
                writer.write(majorUser.getMajorId() + "," + majorUser.getMajorName() + "," + majorUser.getFacultyName());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }

        // Optional: Rename tempFile to the original filename if needed.
        tempFile.renameTo(new File(directory, filename));
    }



    @Override
    public void writeData() {
        // This method can remain empty if unused
    }
}

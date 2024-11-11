package ku.cs.services;

import ku.cs.models.user.Staff;
import ku.cs.models.user.StaffList;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class StaffListFileDatasource implements Datasource<StaffList> {

    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private String directoryName;
    private String fileName;

    private StaffList staffList;

    public StaffListFileDatasource(String directoryName, String fileName) {
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
    public StaffList readData() {
        StaffList staffList = new StaffList();
        String filePath = directoryName + File.separator + fileName;


        File file = new File(filePath);
        if (!file.exists()) {
            System.err.println("File not found: " + filePath);
            return staffList;
        }

        try (
                InputStream inputStream = new FileInputStream(file);
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                BufferedReader buffer = new BufferedReader(inputStreamReader)
        ) {
            String line;
            while ((line = buffer.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] data = line.split(",");

                if (data.length >= 11) {
                    String defaultProfilePic = data[0].trim();
                    String role = data[1].trim();
                    String name = data[2].trim();
                    String surname = data[3].trim();
                    String username = data[4].trim();
                    String password = data[5].trim();
                    String facultyId = data[6].trim();
                    String majorId = data[7].trim();
                    String teacherId = data[8].trim();
                    boolean register = Boolean.parseBoolean(data[9].trim());

                    // Parse date-time, and use the current date-time if parsing fails
                    LocalDateTime time;
                    try {
                        time = LocalDateTime.parse(data[10].trim(), dateTimeFormatter);
                    } catch (Exception e) {
                        System.err.println("Error parsing date-time on line: " + line + ". Using current date-time instead.");
                        time = LocalDateTime.now();
                    }

                    staffList.addNewStaff(defaultProfilePic, role, name, surname, username, password, facultyId, majorId, teacherId, register, time);
                } else {
                    System.err.println("Error: Incomplete data on line: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + filePath);
            e.printStackTrace();
        }

        return staffList;
    }


    @Override
    public void writeData(StaffList staffList) {
        String filePath = directoryName + File.separator + fileName;
        File file = new File(filePath);

        try (
                OutputStream outputStream = new FileOutputStream(file);
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
                BufferedWriter buffer = new BufferedWriter(outputStreamWriter)
        ) {
            for (Staff staff : staffList.getAllOfficers()) {
                String line = String.join(",",
                        staff.getDefaultProfilePic(),
                        staff.getRole(),
                        staff.getName(),
                        staff.getSurname(),
                        staff.getUsername(),
                        staff.getPassword(),
                        staff.getFacultyId(),
                        staff.getMajorID(),
                        staff.getTeacherID(),
                        Boolean.toString(staff.isRegister()),
                        staff.getTime().format(dateTimeFormatter)
                );
                buffer.append(line);
                buffer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing file: " + filePath);
            e.printStackTrace();
        }
    }

    @Override
    public void writeData() {
    }
}

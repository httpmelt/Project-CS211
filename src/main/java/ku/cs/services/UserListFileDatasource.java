package ku.cs.services;

import ku.cs.models.user.User;
import ku.cs.models.user.UserList;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UserListFileDatasource implements Datasource<UserList> {

    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private String directoryName;
    private String fileName;

    // Constructor to initialize the file and directory paths
    public UserListFileDatasource(String directoryName, String fileName) {
        this.directoryName = directoryName;
        this.fileName = fileName;
    }

    @Override
    public UserList readData() {
        UserList userList = new UserList();
        File file = new File(directoryName, fileName);

        if (!file.exists()) {
            System.out.println("File not found: " + file.getPath());
            return userList;
        }

        try (
                InputStream inputStream = new FileInputStream(file);
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                BufferedReader br = new BufferedReader(inputStreamReader)
        ) {
            String line;
            br.readLine(); // Skip header

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");

                // Ensure the CSV data has the right number of values
                if (values.length != 6) {
                    continue; // Skip invalid lines
                }

                String defaultProfilePic = values[0];
                String role = values[1];
                String name = values[2];
                String surname = values[3];
                String username = values[4];
                LocalDateTime time;
                try {
                    time = LocalDateTime.parse(values[5].trim(), dateTimeFormatter);
                } catch (Exception e) {
                    System.err.println("Error parsing date-time on line: " + line + ". Using current date-time instead.");
                    time = LocalDateTime.now();
                }
                // Create user and add to the list
                User user = new User(defaultProfilePic, role, name, surname, username, time);
                userList.addUser(user);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return userList;
    }

    @Override
    public void writeData(UserList userList) {
        String filePath = directoryName + File.separator + fileName;
        File file = new File(filePath);

        // Create a temporary file to store updated data
        File tempFile = new File(directoryName, "temp_" + fileName);

        try (
                OutputStream outputStream = new FileOutputStream(tempFile);
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
                BufferedWriter writer = new BufferedWriter(outputStreamWriter)
        ) {
            writer.write("defaultProfilePic,role,name,surname,username,2023-10-15 10:30:45");
            writer.newLine();


            // Iterate through the userList and write data to the temp file
            for (User user : userList.getUsers()) {
                String updatedLine = String.join(",",
                        user.getDefaultProfilePic(),
                        user.getRole(),
                        user.getName(),
                        user.getSurname(),
                        user.getUsername(),
                        user.getTime().format(dateTimeFormatter)
                );
                writer.append(updatedLine);
                writer.newLine();
            }

        } catch (IOException e) {
            System.err.println("Error writing file: " + filePath);
            e.printStackTrace();
        }

        // Replace old file with updated temp file
        if (!file.delete()) {
            System.err.println("Could not delete original file");
        }
        if (!tempFile.renameTo(file)) {
            System.err.println("Could not rename temp file to original file name");
        }
    }

    // Empty method removed
    @Override
    public void writeData() {

    }
}

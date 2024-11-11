package ku.cs.services.admin;

import ku.cs.models.admin.Personal;
import ku.cs.models.admin.PersonalList;
import ku.cs.services.Datasource;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PersonalListCodeFileDatasource implements Datasource<PersonalList> {

    String directoryName;
    String fileName;
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public PersonalListCodeFileDatasource(String directoryName, String fileName) {
        this.directoryName = directoryName;
        this.fileName = fileName;
        checkFileIsExisted();
    }

    private void checkFileIsExisted() {
        File file = new File(directoryName);
        if (!file.exists()) {
            file.mkdirs();
        }
        String filePath = directoryName + File.separator + fileName;
        file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public PersonalList readData() {
        PersonalList personalList = new PersonalList();
        String filePath = directoryName + File.separator + fileName;
        File file = new File(filePath);

        FileInputStream fileInputStream = null;

        try {
            fileInputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        InputStreamReader inputStreamReader = new InputStreamReader(
                fileInputStream,
                StandardCharsets.UTF_8
        );
        BufferedReader buffer = new BufferedReader(inputStreamReader);

        String line = "";
        try {

            while ( (line = buffer.readLine()) != null ){

                if (line.equals("")) continue;


                String[] data = line.split(",");


                String Profile = data[0];
                String role = data[1];
                String name = data[2];
                String surname = data[3];
                String username = data[4];
                String password = data[5];
                String facultyId = data[6];
                String majorId = data[7];
                String teacherId = data[8];
                boolean register = Boolean.parseBoolean(data[9]);
                LocalDateTime time = LocalDateTime.parse(data[10].trim(), dateTimeFormatter);

                personalList.addPersonal(Profile, role, name, surname,
                        username, password, facultyId, majorId, teacherId, register, time);


            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return personalList;
    }

    @Override
    public void writeData(PersonalList personalList) {
        String filePath = directoryName + File.separator + fileName;
        File file = new File(filePath);

        // เตรียม object ที่ใช้ในการเขียนไฟล์
        FileOutputStream fileOutputStream = null;

        try {
            fileOutputStream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
                fileOutputStream,
                StandardCharsets.UTF_8
        );
        BufferedWriter buffer = new BufferedWriter(outputStreamWriter);

        try {
            // สร้าง csv ของ Student และเขียนลงในไฟล์ทีละบรรทัด

            for (Personal personal : personalList.getPersonalsList()) {
                String line = personal.getDefaultProfilePic() + "," + personal.getRole() +
                        "," + personal.getName() + "," + personal.getSurname() + "," + personal.getUsername() + "," + personal.getPassword() +
                        "," + personal.getFacultyId() + "," + personal.getMajorId() + "," + personal.getTeacherId() + "," + personal.isRegister() + "," + personal.getTime().format(dateTimeFormatter);
                buffer.append(line);
                buffer.append("\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                buffer.flush();
                buffer.close();
            }
            catch (IOException e){
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void writeData() {

    }
}

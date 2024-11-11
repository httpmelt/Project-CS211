package ku.cs.services.student;

import ku.cs.models.student.StudentRegister;
import ku.cs.models.student.StudentRegisterList;
import ku.cs.services.Datasource;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class StudentRegisterDatasource implements Datasource<StudentRegisterList> {
    private String directoryName;
    private String fileName;

    public StudentRegisterDatasource(String directoryName, String fileName) {
        this.directoryName = directoryName;
        this.fileName = fileName;
        checkFileIsExisted();
    }

    private void checkFileIsExisted() {
        File directory = new File(directoryName);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        String filePath = directoryName + File.separator + fileName;
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public StudentRegisterList readData() {
        StudentRegisterList studentRegisterList = new StudentRegisterList();
        String filePath = directoryName + File.separator + fileName;
        File file = new File(filePath);

        try (FileInputStream fileInputStream = new FileInputStream(file);
             InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);
             BufferedReader buffer = new BufferedReader(inputStreamReader)) {

            String line;
            while ((line = buffer.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] data = line.split(",");
                if (data.length < 3) {
                    continue; // Skip invalid rows
                }

                String username = data[0].trim();
                String password = data[1].trim();
                String studentId = data[2].trim();

                // Add the valid student register to the list
                studentRegisterList.addNewStudentRegister(username, password, studentId);
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return studentRegisterList;
    }

    @Override
    public void writeData(StudentRegisterList data) {

        String filePath = directoryName + File.separator + fileName;
        File file = new File(filePath);

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
            // สร้าง csv ของ StudentRegister และเขียนลงในไฟล์ทีละบรรทัด
            for (StudentRegister studentRegister : data.getStudentRegisters()) {
                String line = studentRegister.getUsername()
                + "," + studentRegister.getPassword()
                + "," + studentRegister.getStudentId();

                buffer.append(line);
                buffer.append("\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                buffer.flush();
                buffer.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void writeData() {
        // This method can be left empty or implemented based on specific requirements
    }
}


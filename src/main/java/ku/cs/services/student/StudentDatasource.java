package ku.cs.services.student;

import ku.cs.models.student.Student;
import ku.cs.models.student.StudentList;
import ku.cs.services.Datasource;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class StudentDatasource implements Datasource<StudentList> {
    private String directoryName;
    private String fileName;

    public StudentDatasource(String directoryName, String fileName) {
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
    public StudentList readData() {
        StudentList studentList = new StudentList();
        String filePath = directoryName + File.separator + fileName;
        File file = new File(filePath);

        try (FileInputStream fileInputStream = new FileInputStream(file);
             InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);
             BufferedReader buffer = new BufferedReader(inputStreamReader)) {

            String line;
            while ((line = buffer.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] data = line.split(",");
                if (data.length < 5) {
                    continue; // Skip invalid rows
                }

                String studentId = data[0].trim();
                String name = data[1].trim();
                String surname = data[2].trim();
                String email = data[3].trim();
                String teacherId = data[4].trim();

                // Add the valid student to the list
                studentList.addNewStudent(studentId, name, surname, email, teacherId);
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return studentList;
    }

    @Override
    public void writeData(StudentList data) {
        String filePath = directoryName + File.separator + fileName;
        File file = new File(filePath);

        try (FileOutputStream fileOutputStream = new FileOutputStream(file);
             OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8);
             BufferedWriter buffer = new BufferedWriter(outputStreamWriter)) {

            for (Student student : data.getStudents()) {
                String line = String.format("%s,%s,%s,%s,%s",
                        student.getStudentId(), student.getName(), student.getSurname(), student.getEmail(), student.getTeacherId());

                buffer.write(line);
                buffer.newLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void writeData() {
        // This method can be implemented based on specific requirements if needed
    }
}


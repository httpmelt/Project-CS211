package ku.cs.services.student;

import ku.cs.models.student.StudentReqList;
import ku.cs.models.student.StudentReq;
import ku.cs.services.Datasource;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class StudentReqListFileDatasource implements Datasource<StudentReqList> {
    private String directoryName;
    private String fileName;

    public StudentReqListFileDatasource(String directoryName, String fileName) {
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
    public StudentReqList readData() {
        StudentReqList studentReqList = new StudentReqList();
        String filePath = directoryName + File.separator + fileName;
        File file = new File(filePath);

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        try (FileInputStream fileInputStream = new FileInputStream(file);
             InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);
             BufferedReader buffer = new BufferedReader(inputStreamReader)) {

            String line;
            while ((line = buffer.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] data = line.split(",");
                if (data.length < 27) {
                    continue; // Skip invalid rows
                }

                int reqId;
                String reqFormId = data[1].trim();
                String formName = data[2].trim();
                String studentId = data[3].trim();
                int reqProcessId;
                String reqStatus = data[5].trim();
                String processStatus = data[6].trim();
                LocalDateTime studentProcessDate;
                LocalDateTime teacherProcessDate;
                LocalDateTime majorProcessDate;
                LocalDateTime facultyProcessDate;
                String teacherId = data[11].trim();
                String faculty = data[12].trim();
                String major = data[13].trim();
                int educationGrade;
                int educationTerm;
                int educationYear;
                int feeAmount;
                Date extensionDueDate;
                String optionAddOrDrop = data[19].trim();
                String subject = data[20].trim();
                String subjectId = data[21].trim();
                String degree = data[22].trim();
                String curriculum = data[23].trim();
                String reason = data[24].trim();
                String rejectReason = data[25].trim();
                LocalDateTime updatedDate;

                try {
                    reqId = Integer.parseInt(data[0].trim());
                    reqProcessId = Integer.parseInt(data[4].trim());
                    educationGrade = Integer.parseInt(data[14].trim());
                    educationTerm = Integer.parseInt(data[15].trim());
                    educationYear = Integer.parseInt(data[16].trim());
                    feeAmount = Integer.parseInt(data[17].trim());

                    studentProcessDate = LocalDateTime.parse(data[7].trim(), dateTimeFormatter);
                    teacherProcessDate = LocalDateTime.parse(data[8].trim(), dateTimeFormatter);
                    majorProcessDate = LocalDateTime.parse(data[9].trim(), dateTimeFormatter);
                    facultyProcessDate = LocalDateTime.parse(data[10].trim(), dateTimeFormatter);
                    updatedDate = LocalDateTime.parse(data[26].trim(), dateTimeFormatter);


                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    extensionDueDate = dateFormat.parse(data[18].trim());

                } catch (NumberFormatException | ParseException e) {
                    continue; // Skip rows with invalid number or date formats
                }

                // Add the valid student request to the list
                studentReqList.addNewStudentReq(reqId, reqFormId, formName, studentId, reqProcessId, reqStatus, processStatus, studentProcessDate, teacherProcessDate, majorProcessDate, facultyProcessDate, teacherId, faculty, major, educationGrade, educationTerm, educationYear, feeAmount, extensionDueDate, optionAddOrDrop, subject, subjectId, degree, curriculum, reason, rejectReason, updatedDate);
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return studentReqList;
    }

    @Override
    public void writeData(StudentReqList data) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        // แปลง Date เป็น String ที่มีรูปแบบ yyyy-MM-dd
        // String formattedDate = formatter.format(date);

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
            for (StudentReq studentReq : data.getStudentReqs()) {
                String line = studentReq.getReqId()
                        + "," + studentReq.getReqFormId()
                        + "," + studentReq.getFormName()
                        + "," + studentReq.getStudentId()
                        + "," + studentReq.getReqProcessId()
                        + "," + studentReq.getReqStatus()
                        + "," + studentReq.getProcessStatus()
                        + "," + studentReq.getStudentProcessDate().format(dateTimeFormatter)
                        + "," + studentReq.getTeacherProcessDate().format(dateTimeFormatter)
                        + "," + studentReq.getMajorProcessDate().format(dateTimeFormatter)
                        + "," + studentReq.getFacultyProcessDate().format(dateTimeFormatter)
                        + "," + nullToEmpty(studentReq.getTeacherId())
                        + "," + nullToEmpty(studentReq.getFaculty())
                        + "," + nullToEmpty(studentReq.getMajor())
                        + "," + studentReq.getEducationGrade()
                        + "," + studentReq.getEducationTerm()
                        + "," + studentReq.getEducationYear()
                        + "," + studentReq.getFeeAmount()
                        + "," + nullToEmpty(formatter.format(studentReq.getExtensionDueDate()))
                        + "," + nullToEmpty(studentReq.getOptionAddOrDrop())
                        + "," + nullToEmpty(studentReq.getSubject())
                        + "," + nullToEmpty(studentReq.getSubjectId())
                        + "," + nullToEmpty(studentReq.getDegree())
                        + "," + nullToEmpty(studentReq.getCurriculum())
                        + "," + nullToEmpty(studentReq.getReason())
                        + "," + nullToEmpty(studentReq.getRejectReason())
                        + "," + studentReq.getUpdatedDate().format(dateTimeFormatter);

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

    private String nullToEmpty(String str) {
        return (str == null) ? "" : str;
    }


    @Override
    public void writeData() {

    }
}
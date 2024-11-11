package ku.cs.models.student;

import java.util.ArrayList;
import java.util.Date;
import java.time.LocalDateTime;

public class StudentReqList {
    private ArrayList<StudentReq> studentReqs;

    public StudentReqList() {
        studentReqs = new ArrayList<>();
    }

    // Method to add a student request directly
    public void addRequestForm(StudentReq req) {
        studentReqs.add(req);
    }

    // Method to add a new student request with parameters
    public void addNewStudentReq(int reqId, String reqFormId, String formName, String studentId, int reqProcessId,
                                 String reqStatus, String processStatus, LocalDateTime studentProcessDate,
                                 LocalDateTime teacherProcessDate, LocalDateTime majorProcessDate,
                                 LocalDateTime facultyProcessDate, String teacherId, String faculty, String major,
                                 int educationGrade, int educationTerm, int educationYear, int feeAmount, Date extensionDueDate,
                                 String optionAddOrDrop, String subject, String subjectId, String degree,
                                 String curriculum, String reason, String rejectReason, LocalDateTime updatedDate) {
        StudentReq req = new StudentReq(reqId, reqFormId, formName, studentId, reqProcessId, reqStatus, processStatus,
                studentProcessDate, teacherProcessDate, majorProcessDate, facultyProcessDate,
                teacherId, faculty, major, educationGrade, educationTerm, educationYear,
                feeAmount, extensionDueDate, optionAddOrDrop, subject, subjectId,
                degree, curriculum, reason, rejectReason, updatedDate);
        studentReqs.add(req);
    }

    public ArrayList<StudentReq> getStudentReqs() {
        return studentReqs;
    }

//    // Method to add an existing StudentReq to the list
//    public void addRequestForm(StudentReq request) {
//        studentReqs.add(request);
//    }
//
//
//    public StudentReqList filterByFacultyId(String facultyId) {
//        StudentReqList filteredList = new StudentReqList();
//        for (StudentReq request : this.getStudentReqs()) {
//            if (request.getFaculty().equals(facultyId)) {
//                filteredList.addRequestForm(request);  // Add the filtered request
//            }
//        }
//        return filteredList;
    }





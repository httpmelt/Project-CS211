package ku.cs.models.student;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public class StudentReq {
    private int reqId;
    private String reqFormId;
    private String formName;
    private String studentId;
    private int reqProcessId;
    private String reqStatus;
    private String processStatus;
    private LocalDateTime studentProcessDate;
    private LocalDateTime teacherProcessDate;
    private LocalDateTime majorProcessDate;
    private LocalDateTime facultyProcessDate;
    private String teacherId;
    private String faculty;
    private String major;
    private int educationGrade;
    private int educationTerm;
    private int educationYear;
    private int feeAmount;
    private Date extensionDueDate;
    private String optionAddOrDrop;
    private String subject;
    private String subjectId;
    private String degree;
    private String curriculum;
    private String reason;
    private String rejectReason;
    private LocalDateTime updatedDate;


    public StudentReq(int reqId, String reqFormId, String formName, String studentId, int reqProcessId, String reqStatus, String processStatus, LocalDateTime studentProcessDate, LocalDateTime teacherProcessDate, LocalDateTime majorProcessDate, LocalDateTime facultyProcessDate, String teacherId, String faculty, String major, int educationGrade, int educationTerm, int educationYear, int feeAmount, Date extensionDueDate, String optionAddOrDrop, String subject, String subjectId, String degree, String curriculum, String reason,String rejectReason, LocalDateTime updatedDate) {
        this.reqId = reqId;
        this.reqFormId = reqFormId;
        this.formName = formName;
        this.studentId = studentId;
        this.reqProcessId = reqProcessId;
        this.reqStatus = reqStatus;
        this.processStatus = processStatus;
        this.studentProcessDate = studentProcessDate;
        this.teacherProcessDate = teacherProcessDate;
        this.majorProcessDate = majorProcessDate;
        this.facultyProcessDate = facultyProcessDate;
        this.teacherId = teacherId;
        this.faculty = faculty;
        this.major = major;
        this.educationGrade = educationGrade;
        this.educationTerm = educationTerm;
        this.educationYear = educationYear;
        this.feeAmount = feeAmount;
        this.extensionDueDate = extensionDueDate;
        this.optionAddOrDrop = optionAddOrDrop;
        this.subject = subject;
        this.subjectId = subjectId;
        this.degree = degree;
        this.curriculum = curriculum;
        this.reason = reason;
        this.rejectReason = rejectReason;
        this.updatedDate = updatedDate;
    }

    public int getReqId() {
        return reqId;
    }

    public void setReqId(int reqId) {
        this.reqId = reqId;
    }

    public String getReqFormId() {
        return reqFormId;
    }

    public void setReqFormId(String reqFormId) {
        this.reqFormId = reqFormId;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public int getReqProcessId() {
        return reqProcessId;
    }

    public void setReqProcessId(int reqProcessId) {
        this.reqProcessId = reqProcessId;
    }

    public String getReqStatus() {
        return reqStatus;
    }

    public void setReqStatus(String reqStatus) {
        this.reqStatus = reqStatus;
    }

    public String getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(String processStatus) {
        this.processStatus = processStatus;
    }

    public LocalDateTime getStudentProcessDate() {
        return studentProcessDate;
    }

    public void setStudentProcessDate(LocalDateTime studentProcessDate) {
        this.studentProcessDate = studentProcessDate;
    }

    public LocalDateTime getTeacherProcessDate() {
        return teacherProcessDate;
    }

    public void setTeacherProcessDate(LocalDateTime teacherProcessDate) {
        this.teacherProcessDate = teacherProcessDate;
    }

    public LocalDateTime getMajorProcessDate() {
        return majorProcessDate;
    }

    public void setMajorProcessDate(LocalDateTime majorProcessDate) {
        this.majorProcessDate = majorProcessDate;
    }

    public LocalDateTime getFacultyProcessDate() {
        return facultyProcessDate;
    }

    public void setFacultyProcessDate(LocalDateTime facultyProcessDate) {
        this.facultyProcessDate = facultyProcessDate;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public int getEducationGrade() {
        return educationGrade;
    }

    public void setEducationGrade(int educationGrade) {
        this.educationGrade = educationGrade;
    }

    public int getEducationTerm() {
        return educationTerm;
    }

    public void setEducationTerm(int educationTerm) {
        this.educationTerm = educationTerm;
    }

    public int getEducationYear() {
        return educationYear;
    }

    public void setEducationYear(int educationYear) {
        this.educationYear = educationYear;
    }

    public int getFeeAmount() {
        return feeAmount;
    }

    public void setFeeAmount(int feeAmount) {
        this.feeAmount = feeAmount;
    }

    public Date getExtensionDueDate() {
        return extensionDueDate;
    }

    public void setExtensionDueDate(Date extensionDueDate) {
        this.extensionDueDate = extensionDueDate;
    }

    public String getOptionAddOrDrop() {
        return optionAddOrDrop;
    }

    public void setOptionAddOrDrop(String optionAddOrDrop) {
        this.optionAddOrDrop = optionAddOrDrop;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getCurriculum() {
        return curriculum;
    }

    public void setCurriculum(String curriculum) {
        this.curriculum = curriculum;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }

    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(LocalDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }

    @Override
    public String toString() {
        return "StudentReq{" +
                "reqId=" + reqId +
                ", reqFormId='" + reqFormId + '\'' +
                ", formName='" + formName + '\'' +
                ", studentId='" + studentId + '\'' +
                ", reqProcessId=" + reqProcessId +
                ", reqStatus='" + reqStatus + '\'' +
                ", processStatus='" + processStatus + '\'' +
                ", studentProcessDate=" + studentProcessDate +
                ", teacherProcessDate=" + teacherProcessDate +
                ", majorProcessDate=" + majorProcessDate +
                ", facultyProcessDate=" + facultyProcessDate +
                ", teacherId='" + teacherId + '\'' +
                ", faculty='" + faculty + '\'' +
                ", major='" + major + '\'' +
                ", educationGrade=" + educationGrade +
                ", educationTerm=" + educationTerm +
                ", educationYear=" + educationYear +
                ", feeAmount=" + feeAmount +
                ", extensionDueDate=" + extensionDueDate +
                ", optionAddOrDrop='" + optionAddOrDrop + '\'' +
                ", subject='" + subject + '\'' +
                ", subjectId='" + subjectId + '\'' +
                ", degree='" + degree + '\'' +
                ", curriculum='" + curriculum + '\'' +
                ", reason='" + reason + '\'' +
                ", rejectReason='" + rejectReason + '\'' +
                ", updatedDate=" + updatedDate +
                '}';
    }


}
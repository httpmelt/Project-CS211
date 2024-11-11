package ku.cs.models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Manage {
    private final SimpleStringProperty firstName;
    private final SimpleStringProperty lastName;
    private final SimpleStringProperty studentId;
    private final SimpleStringProperty requestDetails;
    private final SimpleStringProperty status;
    private final SimpleIntegerProperty requestDate;
    private final SimpleStringProperty requestTime;
    private SimpleIntegerProperty approvalDate;
    private SimpleStringProperty approvalTime;
    private SimpleStringProperty rejectionReason;


    public Manage(String firstName, String lastName, String studentId, String requestDetails, String status, int requestDate, String requestTime) {
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.studentId = new SimpleStringProperty(studentId);
        this.requestDetails = new SimpleStringProperty(requestDetails);
        this.status = new SimpleStringProperty(status);
        this.requestDate = new SimpleIntegerProperty(requestDate);
        this.requestTime = new SimpleStringProperty(requestTime);

    }

    private void initializeDefaults() {
        this.approvalDate = new SimpleIntegerProperty(0);  // ค่าเริ่มต้นเป็น 0 จะอัปเดตเมื่อได้รับการอนุมัติ
        this.approvalTime = new SimpleStringProperty("");  // ค่าเริ่มต้นเป็นค่าว่าง จะอัปเดตเมื่อได้รับการอนุมัติ
        this.rejectionReason = new SimpleStringProperty("");  // ค่าเริ่มต้นเป็นค่าว่าง จะอัปเดตหากถูกปฏิเสธ
    }



    public String getFirstName() {
        return firstName.get();
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public String getLastName() {
        return lastName.get();
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public String getStudentId() {
        return studentId.get();
    }

    public void setStudentId(String studentId) {
        this.studentId.set(studentId);
    }

    public String getRequestDetails() {
        return requestDetails.get();
    }

    public void setRequestDetails(String requestDetails) {
        this.requestDetails.set(requestDetails);
    }

    public String getStatus() {
        return status.get();
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public int getRequestDate() {
        return requestDate.get();
    }

    public void setRequestDate(int requestDate) {
        this.requestDate.set(requestDate);
    }

    public String getRequestTime() {
        return requestTime.get();
    }

    public void setRequestTime(String requestTime) {
        this.requestTime.set(requestTime);
    }

    public int getApprovalDate() {
        return approvalDate.get();
    }

    public void setApprovalDate(int approvalDate) {
        this.approvalDate.set(approvalDate);
    }

    public String getApprovalTime() {
        return approvalTime.get();
    }

    public void setApprovalTime(String approvalTime) {
        this.approvalTime.set(approvalTime);
    }

    public String getRejectionReason() {
        return rejectionReason.get();
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason.set(rejectionReason);
    }

}

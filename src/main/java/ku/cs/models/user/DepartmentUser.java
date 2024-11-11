package ku.cs.models.user;

public class DepartmentUser {
    private String majorId;  // รหัสภาควิชา
    private String majorName;  // ชื่อภาควิชา

    // Constructor สำหรับสร้าง DepartmentUser
    public DepartmentUser(String majorId, String majorName) {
        this.majorId = majorId;
        this.majorName = majorName;
    }

    // Getter และ Setter สำหรับ majorId (รหัสภาควิชา)
    public String getMajorId() {
        return majorId;
    }

    public void setMajorId(String majorId) {
        this.majorId = majorId;
    }

    // Getter และ Setter สำหรับ majorName (ชื่อภาควิชา)
    public String getMajorName() {
        return majorName;
    }

    public void setMajorName(String majorName) {
        this.majorName = majorName;
    }

    // Override เมธอด toString() สำหรับแสดงข้อมูลภาควิชาในรูปแบบ string
    @Override
    public String toString() {
        return "DepartmentUser{" +
                "majorId='" + majorId + '\'' +
                ", majorName='" + majorName + '\'' +
                '}';
    }
}

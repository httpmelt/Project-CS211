package ku.cs.models.user;

import java.util.ArrayList;
import java.util.List;

public class DepartmentUserList {
    private List<DepartmentUser> departmentUsers;  // รายการของผู้ใช้ภาควิชา

    // Constructor สำหรับสร้าง DepartmentUserList
    public DepartmentUserList() {
        this.departmentUsers = new ArrayList<>();  // กำหนดให้ departmentUsers เป็น ArrayList
    }

    // เมธอดสำหรับเพิ่มผู้ใช้ภาควิชาในรายการ
    public void addDepartmentUser(DepartmentUser departmentUser) {
        departmentUsers.add(departmentUser);
    }

    // เมธอดสำหรับค้นหาผู้ใช้ภาควิชาโดยใช้ majorId (รหัสภาควิชา)
    public DepartmentUser findByMajorId(String majorId) {
        for (DepartmentUser departmentUser : departmentUsers) {
            if (departmentUser.getMajorId().equals(majorId)) {
                return departmentUser;  // คืนค่า DepartmentUser ที่มี majorId ตรงกับที่ค้นหา
            }
        }
        return null;  // ถ้าไม่พบคืนค่า null
    }

    // เมธอดสำหรับลบผู้ใช้ภาควิชาจากรายการโดยใช้ majorId
    public void removeByMajorId(String majorId) {
        departmentUsers.removeIf(departmentUser -> departmentUser.getMajorId().equals(majorId));
    }

    // Getter สำหรับเรียกดูรายการผู้ใช้ภาควิชา
    public List<DepartmentUser> getDepartmentUsers() {
        return departmentUsers;
    }

    // เมธอดสำหรับแสดงข้อมูลผู้ใช้ทั้งหมดในรูปแบบสตริง
    @Override
    public String toString() {
        return "DepartmentUserList{" +
                "departmentUsers=" + departmentUsers +
                '}';
    }
}

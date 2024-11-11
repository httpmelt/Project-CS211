package ku.cs.services.department;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class DepartmentUserListFileDatasource {

    private String directoryName;
    private String fileName;

    public DepartmentUserListFileDatasource(String directoryName, String fileName) {
        this.directoryName = directoryName;
        this.fileName = fileName;
        checkFileIsExisted();
    }

    private void checkFileIsExisted() {
        File dir = new File(directoryName);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String filePath = directoryName + File.separator + fileName;
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException("Error creating file: " + filePath, e);
            }
        }
    }

    public Map<String, String> readMajorIdToNameMap() {
        Map<String, String> majorIdToNameMap = new HashMap<>();
        File file = new File(directoryName, fileName);

        try (BufferedReader reader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 2) { // ตรวจสอบว่ามี column ที่เพียงพอ
                    String majorId = data[0].trim();
                    String majorName = data[1].trim();
                    majorIdToNameMap.put(majorId, majorName); // เพิ่มข้อมูลใน map
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading department CSV file: " + e.getMessage());
        }

        return majorIdToNameMap;
    }

    // เมธอดสำหรับอ่านข้อมูลและสร้าง Map ของ departmentId และ departmentName
    public Map<String, String> readDepartmentIdToNameMap() {
        Map<String, String> departmentIdToNameMap = new HashMap<>();
        File file = new File(directoryName, fileName);

        try (BufferedReader reader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 2) { // ตรวจสอบให้แน่ใจว่ามีข้อมูลเพียงพอ
                    String departmentId = data[0].trim();
                    String departmentName = data[1].trim();
                    departmentIdToNameMap.put(departmentId, departmentName);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading department CSV file: " + e.getMessage());
        }

        return departmentIdToNameMap;
    }
}

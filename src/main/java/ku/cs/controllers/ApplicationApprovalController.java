package ku.cs.controllers;

import ku.cs.models.student.StudentReq;
import ku.cs.models.student.StudentReqList;
import ku.cs.services.Datasource;
import ku.cs.services.student.StudentReqListFileDatasource;

import java.time.LocalDateTime;


public class ApplicationApprovalController {

    public void approveSubmit(int reqId, int reqProcessId) {
        Datasource datasource = new StudentReqListFileDatasource("data/student", "studentReq.csv");
        StudentReqList studentReqList = (StudentReqList) datasource.readData();

        for (StudentReq req : studentReqList.getStudentReqs()) {
            if (req.getReqId() == reqId) {
                req.setReqProcessId(reqProcessId);
                req.setUpdatedDate(LocalDateTime.now());
                switch (reqProcessId) {
                    case 2:
                        req.setProcessStatus("คำร้องส่งต่อให้หัวหน้าภาควิชา");
                        req.setReqStatus("อนุมัติโดยอาจารย์ที่ปรึกษา");
                        req.setTeacherProcessDate(LocalDateTime.now());
                        break;
                    case 4:
                        req.setProcessStatus("คำร้องดำเนินการครบถ้วน");
                        req.setReqStatus("อนุมัติโดยหัวหน้าภาควิชา");
                        req.setMajorProcessDate(LocalDateTime.now());
                        break;
                    case 5:
                        req.setProcessStatus("คำร้องส่งต่อให้คณบดี");
                        req.setReqStatus("อนุมัติโดยหัวหน้าภาควิชา");
                        req.setMajorProcessDate(LocalDateTime.now());
                        break;
                    case 7:
                        req.setProcessStatus("คำร้องดำเนินการครบถ้วน");
                        req.setReqStatus("อนุมัติโดยคณบดี");
                        req.setFacultyProcessDate(LocalDateTime.now());
                        break;
                    default:
                        break;
                }
            }
        }
        datasource.writeData(studentReqList);
    }

    public void rejectSubmit(int reqId, int reqProcessId, String rejectReason) {
        Datasource datasource = new StudentReqListFileDatasource("data/student", "studentReq.csv");
        StudentReqList studentReqList = (StudentReqList) datasource.readData();

        for (StudentReq req : studentReqList.getStudentReqs()) {
            if (req.getReqId() == reqId) {
                req.setReqProcessId(reqProcessId);
                req.setReqStatus("คำร้องถูกปฏิเสธ");
                req.setRejectReason(rejectReason);
                req.setUpdatedDate(LocalDateTime.now());
                switch (reqProcessId) {
                    case 3:
                        req.setProcessStatus("ปฏิเสธโดยอาจารย์ที่ปรึกษา");
                        req.setTeacherProcessDate(LocalDateTime.now());
                        break;
                    case 6:
                        req.setProcessStatus("ปฏิเสธโดยหัวหน้าภาควิชา");
                        req.setMajorProcessDate(LocalDateTime.now());
                        break;
                    case 8:
                        req.setProcessStatus("ปฏิเสธโดยคณบดี");
                        req.setFacultyProcessDate(LocalDateTime.now());
                        break;
                    default:
                        break;
                }
            }
        }
        datasource.writeData(studentReqList);
        //   navBack(backRoutelabel);
    }

}


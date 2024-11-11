package ku.cs.models.admin;

import java.util.ArrayList;

public class SectionTwoList {

    private ArrayList<SectionTwo> sectionTwoList;

    public SectionTwoList() {
        sectionTwoList = new ArrayList<>();
    }

    public void addSectionTwo( String majorId, String majorName, String facultyName) {
        SectionTwo exist = FindMajorByMajorName(majorName);
        if (exist == null) {
            sectionTwoList.add(new SectionTwo(majorId, majorName, facultyName));
        }
    }

    public SectionTwo FindMajorByMajorName(String major) {
        for(SectionTwo sectionTwo : sectionTwoList) {
            if(sectionTwo.isMajor(major)) {
                return sectionTwo;
            }
        }
        return null;
    }

    public ArrayList<SectionTwo> getSectionTwoList() {
        return sectionTwoList;
    }

    public void updateSectionTwoList(SectionTwo updatedSectionTwo) {
       for(int i = 0; i < sectionTwoList.size(); i++) {
           SectionTwo p = sectionTwoList.get(i);
           if(p.getMajor().equals(updatedSectionTwo.getMajor())) {
               sectionTwoList.set(i, updatedSectionTwo);
               break;
           }
       }
    }



}

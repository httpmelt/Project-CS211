package ku.cs.models.admin;

import java.util.ArrayList;

public class SectionList {
    private ArrayList<Section> sections;

    public SectionList() {
        this.sections = new ArrayList<>();
    }

    public void addSection(String facultyId, String facultyName) {
        Section exist = findFacultyByFacultyName(facultyName);
        if (exist == null) {
            sections.add(new Section(facultyId, facultyName));
        }
    }


    public Section findFacultyByFacultyName(String facultyName) {
        for (Section section : sections) {
            if(section.isFaculty(facultyName)) {
                return section;
            }
        }
        return null;
    }


    public ArrayList<Section> getSections() {
        return sections;
    }

    public void updateSectionList(Section updatedPersonal) {
        for(int i = 0; i < sections.size(); i++) {
            Section p = sections.get(i);
            if(p.getFacultyName().equals(updatedPersonal.getFacultyName())) {
               sections.set(i, updatedPersonal);
               break;
            }
        }
    }
}
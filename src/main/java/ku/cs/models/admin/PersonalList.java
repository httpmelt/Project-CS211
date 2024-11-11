package ku.cs.models.admin;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class PersonalList {

    private ArrayList<Personal> personals;

    private Section section;

    public PersonalList() {
        this.personals = new ArrayList<>();
    }

    public void addPersonal(String Profile, String role, String name, String surname,
                            String username, String password, String facultyId, String majorId, String teacherId, boolean register, LocalDateTime dateTime) {

        Personal exist = findPersonalByUsername(username);
        if ((exist == null)) {
            personals.add(new Personal(Profile, role, name, surname,
                    username, password, facultyId, majorId, teacherId, register, dateTime));

        }
    }



    public Personal findPersonalByUsername(String username) {
        for (Personal personal : personals) {
            if(personal.isUsername(username)) {
                return personal;
            }
        }
        return null;
    }

    public ArrayList<Personal> getPersonalsList() {
        return personals;
    }

    public void updatePersonal(Personal updatedPersonal) {
        for (int i = 0; i < personals.size(); i++) {
            Personal p = personals.get(i);
            if (p.getUsername().equals(updatedPersonal.getUsername())) {
                personals.set(i, updatedPersonal);
                break;
            }
        }
    }





}

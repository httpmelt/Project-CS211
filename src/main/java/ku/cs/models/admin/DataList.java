package ku.cs.models.admin;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class DataList {

    private ArrayList<Data> datalist;

    public DataList() {
        this.datalist = new ArrayList<>();
    }

    public void addData(String profile, String role, String name, String surname, String username, LocalDateTime time) {
        Data exist = findUserByUsername(username);
        if (exist == null) {
            datalist.add(new Data(profile, role, name, surname, username , time));
        }
    }


    public Data findUserByUsername(String username) {
        for (Data user : datalist) {
            if (user.isData(username)) {
                return user;
            }
        }
        return null;
    }


    public ArrayList<Data> getDataList() {
        return datalist;
    }

    public void updateDataList(Data updateData) {
        for(int i = 0; i < datalist.size(); i++) {
            Data d = datalist.get(i);
            if(d.getName().equals(updateData.getUsername())) {
                datalist.set(i, updateData);
                break;
            }
        }
        datalist.add(updateData);
    }



}

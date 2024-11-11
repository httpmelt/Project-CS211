
package ku.cs.services.admin;
import ku.cs.models.admin.Data;
import ku.cs.models.admin.DataList;
import ku.cs.services.Datasource;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DataListCodeFileDatasource implements Datasource<DataList> {
    private String directoryName;
    private String fileName;

    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public DataListCodeFileDatasource(String directoryName, String fileName) {
        this.directoryName = directoryName;
        this.fileName = fileName;
        checkFileIsExisted();
    }

    private void checkFileIsExisted() {
        File file = new File(directoryName);
        if (!file.exists()) {
            file.mkdirs();
        }
        String filePath = directoryName + File.separator + fileName;
        file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }



    @Override
    public DataList readData() {
        DataList dataList = new DataList();
        String filePath = directoryName + File.separator + fileName;
        File file = new File(filePath);

        FileInputStream fileInputStream = null;

        try {
            fileInputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        InputStreamReader inputStreamReader = new InputStreamReader(
                fileInputStream,
                StandardCharsets.UTF_8
        );
        BufferedReader buffer = new BufferedReader(inputStreamReader);

        String line = "";
        try {

            while ( (line = buffer.readLine()) != null ){

                if (line.equals("")) continue;


                String[] data = line.split(",");

                String Profile = data[0].trim();
                String role = data[1].trim();
                String name = data[2].trim();
                String surname = data[3].trim();
                String username = data[4].trim();
                LocalDateTime time = LocalDateTime.parse(data[5].trim(), dateTimeFormatter);


                dataList.addData(Profile, role, name, surname, username, time);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return dataList;


    }

    @Override
    public void writeData(DataList dataList) {
        String filePath = directoryName + File.separator + fileName;
        File file = new File(filePath);

        // Using try-with-resources for better resource management
        try (BufferedWriter buffer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(file), StandardCharsets.UTF_8))) {


            for (Data data : dataList.getDataList()) {
                String line = String.join(",",
                        data.getProfile(),
                        data.getRole(),
                        data.getName(),
                        data.getSurname(),
                        data.getUsername(),
                        data.getTime().format(dateTimeFormatter)
                );
                buffer.write(line);
                buffer.newLine();
            }

        } catch (IOException e) {
            throw new RuntimeException("Error writing data to CSV file", e);
        }
    }


    @Override
    public void writeData() {

    }
}
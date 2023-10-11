package server;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.Duration;
import java.util.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.sql.*;

public class Reader {
    HashSet<Worker> Workers = new HashSet<Worker>();
    public void addWorker(Worker worker){
        Workers.add(worker);
    }
    //ArrayList<Integer> takenID = new ArrayList<Integer>();
    ArrayList<String> uniqueEndDate = new ArrayList<>();
    ArrayList<LocalDateTime> startDates = new ArrayList<>();
    ArrayList<Integer> xCoords = new ArrayList<>();
    ArrayList<String> scriptedCommands = new ArrayList<>();
    public int maxXCoord(){
        return Collections.max(xCoords);
    }

    public int nextID() throws Exception{

        try {
            // Загрузка драйвера
            Class.forName("org.postgresql.Driver");

            // Создание соединения с базой данных
            String url = "jdbc:postgresql://localhost:5432/postgres";
            String user = serverTest.AM.getUsername();
            String password = serverTest.AM.getPassword();
            Connection conn = DriverManager.getConnection(url, user, password);

            // Выполнение запроса
            Statement stmt = conn.createStatement();


            ResultSet id = stmt.executeQuery("select nextval('WorkerId')");
            return id.getInt("nextval");
        }
        catch (Exception e){
            return 1;
        }
    }
    public void workersClear(){
        Workers.clear();
    }
    public void readScript(String name) throws Exception{
        FileInputStream fisScript = new FileInputStream(name);
        BufferedInputStream bisScript = new BufferedInputStream(fisScript);
        InputStreamReader istScript = new InputStreamReader(bisScript);
        BufferedReader brScript = new BufferedReader(istScript);

        String line;
        while((line = brScript.readLine()) != null){
            scriptedCommands.add(line);

        }
    }
    public void readd() throws Exception {

        Workers.clear();

        try {
            // Загрузка драйвера
            Class.forName("org.postgresql.Driver");

            // Создание соединения с базой данных
            String url = "jdbc:postgresql://localhost:5432/postgres";
            String user = "postgres";
            String password = "qwerty";
            Connection conn = DriverManager.getConnection(url, user, password);

            // Выполнение запроса
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select * from workers");




            // Обработка результатов запроса
            while (rs.next()) {
                //takenID.add(rs.getInt("ID"));
                xCoords.add(Integer.parseInt(rs.getString("coordinates").split(" ")[0]));
                String[] xy =rs.getString("coordinates").split(" ");
                Coordinates coords= new Coordinates(Long.parseLong(xy[0]), Float.valueOf(xy[1]));

                LocalDate ld = LocalDate.parse(rs.getString("creationdate"));

                LocalDateTime ldt = LocalDateTime.parse(rs.getString("startdate"));
                String zdtstr = rs.getString("enddate");
                ZonedDateTime zdt = ZonedDateTime.parse(zdtstr);
                Position pos = Position.valueOf(rs.getString("position"));
                String[] orginf = rs.getString("organization").split(" ");
                Organization org = new Organization(Long.parseLong(orginf[0]), OrganizationType.valueOf(orginf[1]));
                String creator = rs.getString("creator");
                Worker holder = new Worker(Integer.parseInt(rs.getString("ID")), rs.getString("name"),coords, ld ,Integer.parseInt(rs.getString("salary")), ldt, zdt, pos, org, creator);
                Workers.add(holder);
            }
            // Закрытие соединения
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


        /*FileInputStream fis = new FileInputStream("test.csv");
        BufferedInputStream bis = new BufferedInputStream(fis);
        InputStreamReader isr = new InputStreamReader(bis);
        BufferedReader br = new BufferedReader(isr);

        String line;
        while ((line = br.readLine()) != null) {
            String[] elements = line.split(",");
            takenID.add(Integer.parseInt(elements[0]));
            xCoords.add(Integer.parseInt(elements[2].split(" ")[0]));
            String[] xy = elements[2].split(" ");
            Coordinates coords= new Coordinates(Long.parseLong(xy[0]), Float.valueOf(xy[1]));

            LocalDate ld = LocalDate.parse(elements[3]);

            LocalDateTime ldt = LocalDateTime.parse(elements[5]);
            String zdtstr = elements[6];
            ZonedDateTime zdt = ZonedDateTime.parse(zdtstr);
            Position pos = Position.valueOf(elements[7]);
            String[] orginf = elements[8].split(" ");
            Organization org = new Organization(Long.parseLong(orginf[0]), OrganizationType.valueOf(orginf[1]));

            Worker holder = new Worker(Integer.parseInt(elements[0]), elements[1],coords, ld ,Integer.parseInt(elements[4]), ldt, zdt, pos, org);
            Workers.add(holder);
        }
        br.close();*/

    }
    public String getWorkers(){
        String s="";
        String result="";
        boolean isEmpty = Workers.stream().filter(c->c.getName().startsWith("A")).findFirst().isEmpty();
        if (isEmpty){System.out.println("No names that start with A");}
        for (Worker work: Workers){
            s+=(work.getID()+", ");
            s+=(work.getName()+", ");
            s+=(work.getCoordinates()+", ");
            s+=(work.getCreationDate()+", ");
            s+=(work.getSalary()+", ");
            s+=(work.getStartDate()+", ");
            s+=(work.getEndDate()+", ");
            s+=(work.getPosition()+", ");
            s+=(work.getOrganization().getOrganization()+",");
            s+=(work.getCreator()+"\n");

        }

        return s;
    }


    public String getWorkersToWrite(){
        String s = "";
        for (Worker work: Workers){
            s+=(work.getID()+",");
            s+=(work.getName()+",");
            s+=(work.getCoordinates()+",");
            s+=(work.getCreationDate()+",");
            s+=(work.getSalary()+",");
            s+=(work.getStartDate()+",");
            s+=(work.getEndDate()+",");
            s+=(work.getPosition()+",");
            s+=(work.getOrganization().getOrganization()+"\n");
        }
        System.out.println(s);
        return s;
    }

    public Worker getMinEndDate(){
        Worker workerToReturn = Workers.iterator().next();
        ZonedDateTime minEndDate= ZonedDateTime.now();
        for (Worker work: Workers){
            if (Duration.between(work.getEndDate(), minEndDate).isNegative() == false){
                minEndDate=work.getEndDate();
                workerToReturn = work;
            }
        }

        Worker result = Workers.stream().min(Comparator.comparing(Worker::getEndDate)).get();
        return result;
    }
    public void removeWorker(int ID){
        Workers.stream().filter(c -> c.getID() == ID).forEach(worker -> Workers.remove(worker));

    }
    public void removeWorkerIfGreater(int xCoord) {
        Workers.stream().filter(c -> Integer.parseInt(c.getCoordinates().toString().split(" ")[0]) > xCoord).map(worker -> Workers.remove(worker));
    }
    public String updateID(int ID) throws Exception {
        int newID = nextID();

        for (Worker work: Workers){
            if(work.getID()==ID){
                if (work.getCreator().equalsIgnoreCase(serverTest.AM.getUsername()))
                {


                    Class.forName("org.postgresql.Driver");

                    String url = "jdbc:postgresql://localhost:5432/postgres";
                    String user = serverTest.AM.getUsername();
                    String password = serverTest.AM.getPassword();
                    Connection conn = DriverManager.getConnection(url, user, password);

                    String sql = String.format(("update workers set id = %d where id = %d"),newID, ID);
                    Statement rs = conn.createStatement();
                    rs.executeUpdate(sql);


                    work.setID(newID);
                    //takenID.add(newID);
                    return "Updated successfully";
                } else{return "Access denied";}
            }
        }
        return "No worker with such ID";
    }
    public String uniqueEndDate(){
        String endDate="";
        for (Worker work: Workers){
            if (uniqueEndDate.contains(work.getEndDate().toString())==false){
            uniqueEndDate.add(work.getEndDate().toString());}
        }
        for (String element: uniqueEndDate){
            endDate+=(element+"\n");
        }
        return endDate;
    }
    public String descendingStartDate(){
        String startDate = "";
        for (Worker work: Workers){
             startDates.add(work.getStartDate());
        }
        Collections.sort(startDates);
        Collections.reverse(startDates);
        for (LocalDateTime ldt : startDates){
            startDate+=(ldt + "\n");
        }
        return startDate;

    }

}

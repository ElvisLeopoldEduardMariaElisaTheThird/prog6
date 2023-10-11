package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.sql.*;

public class CommandManager {
    ScannerClass scanClass = new ScannerClass();
    Writer LW = serverTest.ColM.LW;
    List<String> history = serverTest.ColM.history;
    Reader LR = serverTest.ColM.LR;
    public CommandManager() throws Exception{
        LR.readd();
    }



    public String generalCommands(String outsideCommand) throws Exception{



        String scriptLog = "";

        boolean flag = true;
        boolean execution = false;
        int i = 0;
        while (flag) {
            String command = null;

            if (execution) {
                command = LR.scriptedCommands.get(i);
                i++;
                if (i == LR.scriptedCommands.size()) {
                    execution = false;
                }
            } else {
                command = outsideCommand;
            }
            String[] comms = command.split(" ");

            if (history.size() == 11) {
                history.remove(0);
            }
            history.add(comms[0]);

            if (!execution) {
                switch (comms[0]) {
                    case ("help"):
                        return ("help : вывести справку по доступным командам\n" +
                                "info : вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)\n" +
                                "show : вывести в стандартный поток вывода все элементы коллекции в строковом представлении\n" +
                                "add {element} : добавить новый элемент в коллекцию\n" +
                                "update id {element} : обновить значение элемента коллекции, id которого равен заданному\n" +
                                "remove_by_id id : удалить элемент из коллекции по его id\n" +
                                "clear : очистить коллекцию\n" +
                                "save : сохранить коллекцию в файл\n" +
                                "execute_script file_name : считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.\n" +
                                "exit : завершить программу (без сохранения в файл)\n" +
                                "add_if_max {element} : добавить новый элемент в коллекцию, если его значение превышает значение наибольшего элемента этой коллекции\n" +
                                "remove_greater {element} : удалить из коллекции все элементы, превышающие заданный\n" +
                                "history : вывести последние 11 команд (без их аргументов)\n" +
                                "min_by_end_date : вывести любой объект из коллекции, значение поля endDate которого является минимальным\n" +
                                "print_unique_end_date : вывести уникальные значения поля endDate всех элементов в коллекции\n" +
                                "print_field_descending_start_date : вывести значения поля startDate всех элементов в порядке убывания");

                    case ("info"):
                        return ("info command");

                    case ("show"):
                        LR.readd();
                        return (LR.getWorkers());
                    case ("add"):
                        int id = scanClass.scanID();
                        String name = serverTest.ColM.workerInfo.get(1);
                        String[] coordinates = serverTest.ColM.workerInfo.get(2).split(" ");
                        Coordinates coords = new Coordinates(Long.parseLong(coordinates[0]),Float.valueOf(coordinates[1]));
                        int salary = Integer.parseInt(serverTest.ColM.workerInfo.get(3));
                        Position position = Position.valueOf(serverTest.ColM.workerInfo.get(4));
                        Organization org = scanClass.scanOrganization(Integer.parseInt(serverTest.ColM.workerInfo.get(5)), OrganizationType.valueOf(serverTest.ColM.workerInfo.get(6)));
                        LocalDate ld = LocalDate.now();
                        LocalDateTime ldt = LocalDateTime.now();
                        ZonedDateTime zdt = ZonedDateTime.now();
                        String creator = serverTest.AM.getUsername();

                        Worker worker = new Worker(id, name, coords, ld, salary, ldt, zdt, position, org,creator);

                        try {
                            Class.forName("org.postgresql.Driver");

                            String url = "jdbc:postgresql://localhost:5432/postgres";
                            String user = serverTest.AM.getUsername();
                            String password = serverTest.AM.getPassword();
                            Connection conn = DriverManager.getConnection(url, user, password);

                            String sql = String.format("insert into workers(id, name, coordinates, creationdate, salary, startdate, enddate, position, organization, creator) values (%d,'%s','%s','%s',%d,'%s','%s','%s','%s','%s')",worker.getID(), worker.getName(),worker.getCoordinates().toString(),worker.getCreationDate().toString(),worker.getSalary(),worker.getStartDate().toString(),worker.getEndDate().toString(),worker.getPosition().toString(),worker.getOrganization().getOrganization(),worker.getCreator());
                            Statement rs = conn.createStatement();
                            rs.executeUpdate(sql);

                            LR.addWorker(worker);

                            rs.close();
                            conn.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        break;
                    case ("update"):
                        try {
                            Integer.parseInt(comms[1]);
                            return LR.updateID(Integer.parseInt(comms[1]));
                        } catch (Exception e) {
                            return (e.toString());
                        }
                    case ("remove_by_id"):
                        try {
                            Integer.parseInt(comms[1]);
                            LR.removeWorker(Integer.parseInt(comms[1]));
                        } catch (Exception e) {
                            return ("Input error, try again");
                        }
                        break;
                    case ("clear"):
                        LR.workersClear();
                        break;
                    case ("save"):
                        LW.write(LR.getWorkersToWrite());
                        break;
                    case ("execute_script"):
                        try {
                            LR.readScript(comms[1]);
                            execution = true;
                            i = 0;
                        } catch (Exception e) {
                            return ("Input error, try again");
                        }
                        break;
                    case ("exit"):
                        flag = false;
                        break;
                    case ("add_if_max"):
                        id = scanClass.scanID();
                        name = scanClass.scanName();
                        coords = scanClass.scanCoordinates();
                        salary = scanClass.scanSalary();
                        position = scanClass.scanPosition();
                        org = scanClass.scanOrganization(scanClass.scanOrganizationCount(), scanClass.scanOrganizationType());
                        ld = LocalDate.now();
                        ldt = LocalDateTime.now();
                        zdt = ZonedDateTime.now();
                        creator = serverTest.AM.getUsername();
                        worker = new Worker(id, name, coords, ld, salary, ldt, zdt, position, org, creator);
                        if (Integer.parseInt(worker.getCoordinates().toString().split(" ")[0]) > LR.maxXCoord()) {
                            try {
                                Class.forName("org.postgresql.Driver");

                                String url = "jdbc:postgresql://localhost:5432/postgres";
                                String user = serverTest.AM.getUsername();
                                String password = serverTest.AM.getPassword();
                                Connection conn = DriverManager.getConnection(url, user, password);

                                String sql = String.format("insert into workers(id, name, coordinates, creationdate, salary, startdate, enddate, position, organization, creator) values (%d,'%s','%s','%s',%d,'%s','%s','%s','%s','%s')",worker.getID(), worker.getName(),worker.getCoordinates().toString(),worker.getCreationDate().toString(),worker.getSalary(),worker.getStartDate().toString(),worker.getEndDate().toString(),worker.getPosition().toString(),worker.getOrganization().getOrganization(),worker.getCreator());
                                System.out.println(sql);
                                Statement rs = conn.createStatement();
                                rs.executeUpdate(sql);

                                LR.addWorker(worker);

                                rs.close();
                                conn.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    case ("remove_greater"):
                        try {
                            Integer.parseInt(comms[1]);
                            LR.removeWorkerIfGreater(Integer.parseInt(comms[1]));
                        } catch (Exception e) {
                            return ("Input error, try again");
                        }
                        break;
                    case ("history"):
                        String hist = "";
                        for (String com : history) {
                            hist += (com + "\n");
                        }
                        return hist;
                    case ("min_by_end_date"):
                        return (LR.getMinEndDate().toString());
                    case ("print_unique_end_date"):
                        return (LR.uniqueEndDate());
                    case ("print_field_descending_start_date"):
                        return (LR.descendingStartDate());
                    default:
                        return ("Incorrect command. Try help for receiving more information on the commands.");
                }
                flag = false;
            } else if (execution) {
                switch (comms[0]) {
                    case ("help"):
                        scriptLog += ("help : вывести справку по доступным командам\n" +
                                "info : вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)\n" +
                                "show : вывести в стандартный поток вывода все элементы коллекции в строковом представлении\n" +
                                "add {element} : добавить новый элемент в коллекцию\n" +
                                "update id {element} : обновить значение элемента коллекции, id которого равен заданному\n" +
                                "remove_by_id id : удалить элемент из коллекции по его id\n" +
                                "clear : очистить коллекцию\n" +
                                "save : сохранить коллекцию в файл\n" +
                                "execute_script file_name : считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.\n" +
                                "exit : завершить программу (без сохранения в файл)\n" +
                                "add_if_max {element} : добавить новый элемент в коллекцию, если его значение превышает значение наибольшего элемента этой коллекции\n" +
                                "remove_greater {element} : удалить из коллекции все элементы, превышающие заданный\n" +
                                "history : вывести последние 11 команд (без их аргументов)\n" +
                                "min_by_end_date : вывести любой объект из коллекции, значение поля endDate которого является минимальным\n" +
                                "print_unique_end_date : вывести уникальные значения поля endDate всех элементов в коллекции\n" +
                                "print_field_descending_start_date : вывести значения поля startDate всех элементов в порядке убывания" + "\n");
                        break;

                    case ("info"):
                        scriptLog += ("info command" + "\n");
                        break;

                    case ("show"):
                        scriptLog += (LR.getWorkers() + "\n");
                        break;
                    case ("add"):
                        int id = scanClass.scanID();
                        String name = serverTest.ColM.workerInfo.get(1);
                        Coordinates coords = new Coordinates(Long.parseLong(serverTest.ColM.workerInfo.get(2)));
                        int salary = Integer.parseInt(serverTest.ColM.workerInfo.get(3));
                        Position position = Position.valueOf(serverTest.ColM.workerInfo.get(4));
                        Organization org = scanClass.scanOrganization(Integer.parseInt(serverTest.ColM.workerInfo.get(5)), OrganizationType.valueOf(serverTest.ColM.workerInfo.get(6)));
                        LocalDate ld = LocalDate.now();
                        LocalDateTime ldt = LocalDateTime.now();
                        ZonedDateTime zdt = ZonedDateTime.now();
                        String creator = serverTest.AM.getUsername();
                        Worker worker = new Worker(id, name, coords, ld, salary, ldt, zdt, position, org, creator);
                        try {
                            Class.forName("org.postgresql.Driver");

                            String url = "jdbc:postgresql://localhost:5432/postgres";
                            String user = serverTest.AM.getUsername();
                            String password = serverTest.AM.getPassword();
                            Connection conn = DriverManager.getConnection(url, user, password);

                            String sql = String.format("insert into workers(id, name, coordinates, creationdate, salary, startdate, enddate, position, organization, creator) values (%d,'%s','%s','%s',%d,'%s','%s','%s','%s','%s')",worker.getID(), worker.getName(),worker.getCoordinates().toString(),worker.getCreationDate().toString(),worker.getSalary(),worker.getStartDate().toString(),worker.getEndDate().toString(),worker.getPosition().toString(),worker.getOrganization().getOrganization(),worker.getCreator());
                            System.out.println(sql);
                            Statement rs = conn.createStatement();
                            rs.executeUpdate(sql);

                            LR.addWorker(worker);

                            rs.close();
                            conn.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case ("update"):
                        try {
                            Integer.parseInt(comms[1]);
                            LR.updateID(Integer.parseInt(comms[1]));
                        } catch (Exception e) {
                            scriptLog += ("Input error, try again" + "\n");
                        }
                        break;
                    case ("remove_by_id"):
                        try {
                            Integer.parseInt(comms[1]);
                            LR.removeWorker(Integer.parseInt(comms[1]));
                        } catch (Exception e) {
                            scriptLog += ("Input error, try again" + "\n");
                        }
                        break;
                    case ("clear"):
                        LR.workersClear();
                        break;
                    /*case ("save"):
                        1
                        break;*/
                    case ("execute_script"):
                        try {
                            LR.readScript(comms[1]);
                            execution = true;
                            i = 0;
                        } catch (Exception e) {
                            scriptLog += ("Input error, try again" + "\n");
                        }
                        break;
                    case ("exit"):
                        flag = false;
                        break;
                    case ("add_if_max"):
                        id = scanClass.scanID();
                        name = serverTest.ColM.workerInfo.get(1);
                        coords = new Coordinates(Long.parseLong(serverTest.ColM.workerInfo.get(2)));
                        salary = Integer.parseInt(serverTest.ColM.workerInfo.get(3));
                        position = Position.valueOf(serverTest.ColM.workerInfo.get(4));
                        org = scanClass.scanOrganization(Integer.parseInt(serverTest.ColM.workerInfo.get(5)), OrganizationType.valueOf(serverTest.ColM.workerInfo.get(6)));
                        ld = LocalDate.now();
                        ldt = LocalDateTime.now();
                        zdt = ZonedDateTime.now();
                        creator = serverTest.AM.getUsername();
                        worker = new Worker(id, name, coords, ld, salary, ldt, zdt, position, org, creator);
                        LR.addWorker(worker);
                        if (Integer.parseInt(worker.getCoordinates().toString().split(" ")[0]) > LR.maxXCoord()) {
                            try {
                                Class.forName("org.postgresql.Driver");

                                String url = "jdbc:postgresql://localhost:5432/postgres";
                                String user = serverTest.AM.getUsername();
                                String password = serverTest.AM.getPassword();
                                Connection conn = DriverManager.getConnection(url, user, password);

                                String sql = String.format("insert into workers(id, name, coordinates, creationdate, salary, startdate, enddate, position, organization, creator) values (%d,'%s','%s','%s',%d,'%s','%s','%s','%s','%s')",worker.getID(), worker.getName(),worker.getCoordinates().toString(),worker.getCreationDate().toString(),worker.getSalary(),worker.getStartDate().toString(),worker.getEndDate().toString(),worker.getPosition().toString(),worker.getOrganization().getOrganization(),worker.getCreator());
                                System.out.println(sql);
                                Statement rs = conn.createStatement();
                                rs.executeUpdate(sql);

                                LR.addWorker(worker);

                                rs.close();
                                conn.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    case ("remove_greater"):
                        try {
                            Integer.parseInt(comms[1]);
                            LR.removeWorkerIfGreater(Integer.parseInt(comms[1]));
                        } catch (Exception e) {
                            scriptLog += ("Input error, try again" + "\n");
                        }
                        break;
                    case ("history"):
                        String hist = "";
                        for (String com : history) {
                            hist += (com + "\n");
                        }
                        scriptLog += hist + "\n";
                        break;
                    case ("min_by_end_date"):
                        scriptLog += (LR.getMinEndDate().toString() + "\n");
                        break;
                    case ("print_unique_end_date"):
                        scriptLog += (LR.uniqueEndDate() + "\n");
                        break;
                    case ("print_field_descending_start_date"):
                        scriptLog += (LR.descendingStartDate() + "\n");
                        break;
                    default:
                        scriptLog += ("Incorrect command. Try help for receiving more information on the commands." + "\n");
                        break;
                }
                return scriptLog;

            }


        }
        return "Command is done";
    }


}

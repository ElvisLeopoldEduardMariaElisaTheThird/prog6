package server;

import server.Reader;
import server.Worker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class CollectionManager {
    public static List<String> workerInfo = new ArrayList<>();
    public static Writer LW = new Writer();
    public static List<String> history = new ArrayList<>(11);
    public static Reader LR = new Reader();
    public static CommandManager CM;
    ScannerClass Scan = new ScannerClass();
    public static boolean scanningWorker = false;

    static {
        try {
            CM = new CommandManager();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public String getCommand(String response) throws Exception{
        if (!response.equalsIgnoreCase("add") & !scanningWorker) {
            String output = CM.generalCommands(response);
            return output;}
        else if(response.equalsIgnoreCase("add") & !scanningWorker){
            scanningWorker=true;
        workerInfo.clear();}
        if(scanningWorker==true){
            switch (workerInfo.size()) {
            case (0):
                workerInfo.add(String.valueOf(Scan.scanID()));
                return "Input worker's name";
            case (1):
                if (response.length() == 0) {
                    return ("Input error, try again");
                }
                workerInfo.add(response);
                return "Input worker's coordinates";
            case (2):
                String[] coordinates = response.split(" ");
                String x = coordinates[0];
                try {
                    Long.parseLong(x);
                } catch (Exception e) {
                    return ("Input error, try again");
                }
                if (coordinates.length == 2) {
                    try {
                        Float.valueOf(coordinates[1]);
                    } catch (Exception e) {
                        return ("Input error, try again");
                    }
                    float y = Float.valueOf(coordinates[1]);
                    Coordinates localCoords = new Coordinates(Long.parseLong(x), y);
                    workerInfo.add(localCoords.toString());
                    return "Input worker's salary. It should be greater than zero";
                } else if (coordinates.length == 1) {

                    Coordinates localCoords = new Coordinates(Long.parseLong(x));
                    workerInfo.add(localCoords.toString());
                    return "Input worker's salary. It should be greater than zero";
                } else {
                    return ("Input error, try again");
                }
            case (3):
                try {
                    Integer.parseInt(response);
                } catch (Exception e) {
                    return ("Input error, try again");
                }
                if (Integer.parseInt(response) <= 0) {
                    return ("Input error, try again");
                }
                workerInfo.add(response);
                return "Input worker's position. Here is a lit of positions: MANAGER, LABORER, MANAGER_OF_CLEANING";
            case (4):
                switch (response) {
                    case ("MANAGER"):
                    case ("LABORER"):
                    case ("MANAGER_OF_CLEANING"):
                        break;
                    default:
                        return ("Input error, try again");
                }
                workerInfo.add(response);
                return "Input worker's organization's employee count";
            case (5):
                try {
                    Integer.parseInt(response);
                } catch (Exception e) {
                    return ("Input error, try again");
                }
                workerInfo.add(response);
                return "Input worker's organization's type out of these: COMMERCIAL, PRIVATE_LIMITED_COMPANY, OPEN_JOINT_STOCK_COMPANY";
            case (6):
                switch (response) {
                    case ("COMMERCIAL"):
                    case ("PRIVATE_LIMITED_COMPANY"):
                    case ("OPEN_JOINT_STOCK_COMPANY"):
                        break;
                    default:
                        return ("Input error, try again");

                }
                workerInfo.add(response);
                scanningWorker = false;
                CM.generalCommands("add");
                return "All is done";
        }

        }
        return "An error occurred";
    }
}

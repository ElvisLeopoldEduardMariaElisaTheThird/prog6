package server;

import java.lang.invoke.VarHandle;
import java.util.Random;
import java.util.Scanner;
public class ScannerClass {
    Scanner in = new Scanner(System.in);
    public void scanWorker(){
        System.out.println("Input a worker stats");
        String worker = in.nextLine();
        String[] elements = worker.split(",");
        // Обработка данных
        /*server.Worker holder = new server.Worker();
        holder.setID(Integer.parseInt(elements[0]));
        holder.setName(elements[1]);
        return holder;*/
    }
    public int scanID() throws Exception{
        /*Scanner in = new Scanner(System.in);
        System.out.println("Input worker's ID. It should be greater than zero.");
        int id = in.nextInt();*/
        Reader reader = new Reader();
        reader.readd();
        int id = reader.nextID();
        return id;
    }
    public String scanName(){
        Scanner in = new Scanner(System.in);
        System.out.println("Input worker's name");
        String name = in.nextLine();
        if(name.length()==0){
            System.out.println("Input error, try again");
            return scanName();
        }
        return name;
    }
    public Coordinates scanCoordinates(){
        Scanner in = new Scanner(System.in);
        System.out.println("Input worker's coordinates");
        String cords = in.nextLine();
        String[] coordinates = cords.split(" ");
        String x = coordinates[0];
        try{
            Long.parseLong(x);
        }
        catch(Exception e){
            System.out.println("Input error, try again");
            return scanCoordinates();
        }
        if(coordinates.length == 2) {
            try{
                Float.valueOf(coordinates[1]);
            }
            catch(Exception e){
                System.out.println("Input error, try again");
                return scanCoordinates();
            }
            float y = Float.valueOf(coordinates[1]);
            Coordinates localCoords = new Coordinates(Long.parseLong(x),y);
            return localCoords;
        }else if(coordinates.length == 1){

            Coordinates localCoords = new Coordinates(Long.parseLong(x));
            return localCoords;
        } else {
            System.out.println("Input error, try again");
            return scanCoordinates();
        }
    }
    public int scanSalary(){
        Scanner in = new Scanner(System.in);
        System.out.println("Input worker's salary. It should be greater than zero");
        String salary = in.nextLine();
        try {
            Integer.parseInt(salary);
        }
        catch( Exception e ){
            System.out.println("Input error, try again");
            return scanSalary();
        }
        if (Integer.parseInt(salary)<=0){
            System.out.println("Input error, try again");
            return scanSalary();
        }
        return Integer.parseInt(salary);

    }
    public Position scanPosition(){
        Scanner in = new Scanner(System.in);
        System.out.println("Input worker's position. Here is a lit of positions: MANAGER, LABORER, MANAGER_OF_CLEANING");
        String pos = in.nextLine();
        switch (pos){
            case("MANAGER"):
            case ("LABORER"):
            case ("MANAGER_OF_CLEANING"):
                break;
            default:
                System.out.println("Input error, try again");
                return scanPosition();
        }
        return Position.valueOf(pos);
    }
    public Integer scanOrganizationCount() {
        Scanner in = new Scanner(System.in);
        System.out.println("Input worker's organization's employee count");
        String count = in.nextLine();
        try {
            Integer.parseInt(count);
        }
        catch( Exception e ){
            System.out.println("Input error, try again");
            return scanOrganizationCount();
        }
        return Integer.parseInt(count);
    }
    public OrganizationType scanOrganizationType(){
        Scanner inn = new Scanner(System.in);
        System.out.println("Input worker's organization's type out of these: COMMERCIAL, PRIVATE_LIMITED_COMPANY, OPEN_JOINT_STOCK_COMPANY");
        String type = inn.nextLine();
        switch (type){
            case("COMMERCIAL"):
            case("PRIVATE_LIMITED_COMPANY"):
            case("OPEN_JOINT_STOCK_COMPANY") :
                break;
            default:
                System.out.println("Input error, try again");
                return scanOrganizationType();
        }
        OrganizationType otype = OrganizationType.valueOf(type);
        return otype;
    }
    public Organization scanOrganization(int count, OrganizationType otype){
        Organization org = new Organization(count, otype);
        return org;
    }

    public String scan() {
        Scanner in = new Scanner(System.in);
        System.out.println("Input a command");
        String command = in.nextLine();
        return command;
    }
}

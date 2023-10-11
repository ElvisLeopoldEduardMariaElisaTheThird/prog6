package server;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
public class Worker{
    private Integer id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private java.time.LocalDate creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private int salary; //Значение поля должно быть больше 0
    private java.time.LocalDateTime startDate; //Поле не может быть null
    private java.time.ZonedDateTime endDate; //Поле может быть null
    private Position position; //Поле может быть null
    private Organization organization; //Поле не может быть null
    private String creator;

    public Worker(int id, String name, Coordinates coordinates, LocalDate creationDate, int salary,
                  LocalDateTime startDate, ZonedDateTime endDate, Position position, Organization organization, String creator){
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.salary = salary;
        this.startDate = startDate;
        this.endDate = endDate;
        this.position = position;
        this.organization = organization;
        this.creator = creator;
    }

    public void setID(Integer id){this.id=id;}
    public void setName(String name){this.name=name;}
    public void setCoordinates(Coordinates coordinates){this.coordinates=coordinates;}
    /**No creation date, because it is created automatically and shouldn`t be modified*/
    public void setSalary(int salary){this.salary=salary;}
    public void setStartDate(LocalDateTime startDate){this.startDate=startDate;}
    public void setEndDate(ZonedDateTime endDate){this.endDate=endDate;}
    public void setPosition(Position position){this.position=position;}
    public void setOrganization(Organization organization){this.organization=organization;}
    /**Getters*/
    public int getID() {return this.id;}
    public String getName() {return this.name;}
    public Coordinates getCoordinates(){return this.coordinates;}

    public LocalDate getCreationDate(){return this.creationDate;}
    public int getSalary(){return this.salary;}
    public LocalDateTime getStartDate(){return this.startDate;}
    public ZonedDateTime getEndDate() {return this.endDate;}

    public Position getPosition(){
        if(this.position == null){
            return null;
        } else{
            return this.position;
        }
    }
    public Organization getOrganization() {return this.organization;}
    public String getCreator(){return this.creator;}

    @Override
    public String toString(){
        return (this.id +","+ this.name+","+this.coordinates+","+this.creationDate+","+this.salary+","+this.startDate+","+this.endDate+","+this.position.name()+","+ this.organization.getOrganization()+","+this.creator);
    }
}
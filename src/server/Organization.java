package server;

public class Organization {
    private long employeesCount; //Значение поля должно быть больше 0
    private OrganizationType type; //Поле не может быть null
    public Organization(long employeesCount, OrganizationType type){
        this.employeesCount=employeesCount;
        this.type=type;
    }
    public String getOrganization(){
        return (this.employeesCount+" "+this.type);
    }
}
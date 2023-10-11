package server;

import java.sql.ResultSet;

public class Coordinates {
    private Long x; //Поле не может быть null
    private float y;

    public Coordinates(Long x, float y){
        this.x = x;
        this.y = y;
    }
    public Coordinates(Long x){
        this.x = x;

    }

    public Long getX(){return this.x;}
    public Float getY(){return this.y;}

    public void setX(){this.x = x;}
    public void setY(){this.y = y;}
    @Override
    public String toString(){
        return (this.x + " "+ this.y);
    }

}
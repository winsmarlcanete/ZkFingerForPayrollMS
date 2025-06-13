package src.entity;

import java.sql.Time;
import java.sql.Date;

public class Timecard {
    private int timecard_id;
    private int employee_id;

    private Date date;
    private Time time_in;
    private Time time_out;

    private double hours_clocked;
    private  double minutes_clocked;

    //Constructor
    public Timecard(int employee_id, Date date, Time time_in, Time time_out){
        this.employee_id = employee_id;
        this.date = date;
        this.time_in = time_in;
        this.time_out = time_out;

    }
    //Getters and Setters
    public int getTimecard_id(){return timecard_id;}
    public int getEmployee_id(){return employee_id;}

    public Date getDate(){return date;}
    public Time getTime_in(){return time_in;}
    public Time getTime_out(){return time_out;}

    public Double getHours_clocked(){return hours_clocked;}
    public Double getMinutes_clocked(){return minutes_clocked;}

}
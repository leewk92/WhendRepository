package net.whend.soodal.whend.model.top;

import net.whend.soodal.whend.model.base.Schedule;
import net.whend.soodal.whend.util.DateTimeFormatter;

/**
 * Created by JB on 15. 7. 9.
 */
public class Upload_Schedule {

    private String date_start, date_end;
    private String content;
    private String time_start, time_end;
    private String location;
    private Schedule s;
    public Upload_Schedule(Schedule s){
        this.content = s.getTitle();
        DateTimeFormatter dtf = new DateTimeFormatter(s.getStarttime());
        DateTimeFormatter dtf2 = new DateTimeFormatter(s.getEndtime());

        this.date_start = dtf.getDate();
        this.date_end = dtf2.getDate();

        this.time_start = dtf.getTime();
        this.time_end = dtf2.getTime();
        this.location = s.getLocation();
        this.s = s;
    }

    public Upload_Schedule(String date_start, String date_end, String content, String time_start, String location){
        this.date_end = date_end;
        this.date_start = date_start;
        this.content = content;
        this.time_start = time_start;
        this.time_end = time_end;
        this.location = location;
    }

    public void setDateStart(String date_start) {
        this.date_start = date_start;
    }

    public void setDateEnd(String date_end){this.date_end = date_end;}

    public void setContent(String content) {
        this.content = content;
    }

    public void setTime_start(String time_start) {
        this.time_start = time_start;
    }

    public void setTime_end(String time_end) {
        this.time_end = time_end;
    }

    public String getTime_end() {

        return time_end;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Schedule getSchedule() {
        return s;
    }

    public void setSchedule(Schedule s) {
        this.s = s;

    }

    public String getDateEnd() {
        return date_end;
    }
    public String getDateStart(){ return date_start; }
    public String getContent(){ return content; }
    public String getTime_start(){ return time_start; }
    public String getLocation(){ return location; }
}

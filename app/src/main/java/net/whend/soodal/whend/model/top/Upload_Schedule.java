package net.whend.soodal.whend.model.top;

import net.whend.soodal.whend.model.base.Schedule;
import net.whend.soodal.whend.util.DateTimeFormatter;

/**
 * Created by JB on 15. 7. 9.
 */
public class Upload_Schedule {

    private String date;
    private String content;
    private String time;
    private String location;

    public Upload_Schedule(Schedule s){
        this.content = s.getTitle();
        DateTimeFormatter dtf = new DateTimeFormatter(s.getStarttime());
        this.date = dtf.getYear() + dtf.getDate();
        this.time = dtf.getTime();
        this.location = s.getLocation();
    }

    public Upload_Schedule(String date, String content, String time, String location){
        this.date = date;
        this.content = content;
        this.time = time;
        this.location = location;
    }

    public String getDate(){ return date; }
    public String getContent(){ return content; }
    public String getTime(){ return time; }
    public String getLocation(){ return location; }
}

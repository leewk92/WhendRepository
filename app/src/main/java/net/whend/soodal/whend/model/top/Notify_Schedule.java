package net.whend.soodal.whend.model.top;

import android.graphics.Color;
import android.nfc.Tag;
import android.util.Log;

import net.whend.soodal.whend.model.base.Schedule;
import net.whend.soodal.whend.model.base.User;
import net.whend.soodal.whend.util.DateTimeFormatter;

import java.util.Random;

/**
 * Created by 김형성 on 2015-07-19.
 */




public class Notify_Schedule {

/*
    final int         START_FOLLOWING=0, REQUEST_FOLLOWING=1;
    final int         ADD_SCHEDULE=2, LIKE_SCHEDULE=3, UPDATED_SCHEDULE=4, COMMENT_ON_SCHEDULE=5;
    final int         NEW_SCHEDULE_ON_TAG=6;


    User       user_from;
    Tag        tag_following;
    Schedule   schedule_tonotify;
    int        event;
    String     event_string;


    public String getEvent_string() {
        return event_string;
    }

    public Notify_Schedule(){

        Random random = new Random();
        event = random.nextInt(6);
        switch (event){
            case START_FOLLOWING:
                event_string = "START_FOLLOWING";
                break;
            case REQUEST_FOLLOWING:
                event_string = "REQUEST_FOLLOWING";
                break;
            case ADD_SCHEDULE:
                event_string = "ADD_SCHEDULE";
                break;
            case LIKE_SCHEDULE:
                event_string = "LIKE_SCHEDULE";
                break;
            case UPDATED_SCHEDULE:
                event_string = "UPDATED_SCHEDULE";

                break;
            case COMMENT_ON_SCHEDULE:
                event_string = "COMMENT_ON_SCHEDULE";
                break;
            case NEW_SCHEDULE_ON_TAG:
                event_string = "NEW_SCHEDULE_ON_TAG";
                break;

        };

    }
*/
    int user_id, target_id;
    String actor_name, verb, description;
    String timestamp;
    String date,time;
    long timestamp_ms;

    public int getTarget_id() {
        return target_id;
    }

    public void setTarget_id(int target_id) {
        this.target_id = target_id;
    }
    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
        DateParse();
    }

    // Constructor
    public Notify_Schedule(){

    }

    public String getActor_name() {
        return actor_name;
    }

    public void setActor_name(String actor_name) {
        this.actor_name = actor_name;
    }

    public String getVerb() {
        return verb;
    }

    public void setVerb(String verb) {
        this.verb = verb;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void DateParse(){

        String tmpTimestamp = this.timestamp.substring(0,this.timestamp.length()-8)+'Z';
        Log.d("tmpTimestamp",tmpTimestamp);

        DateTimeFormatter df = new DateTimeFormatter(tmpTimestamp);
        this.date = df.getDate();
        this.time = df.getTime();
        this.setTimestamp_ms(df.getDatetime_ms());

    }

    public long getTimestamp_ms() {
        return timestamp_ms;
    }

    public void setTimestamp_ms(long timestamp_ms) {
        this.timestamp_ms = timestamp_ms;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}

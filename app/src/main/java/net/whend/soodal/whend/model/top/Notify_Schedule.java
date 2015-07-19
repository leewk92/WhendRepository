package net.whend.soodal.whend.model.top;

import android.graphics.Color;
import android.nfc.Tag;

import net.whend.soodal.whend.model.base.Schedule;
import net.whend.soodal.whend.model.base.User;

import java.util.Random;

/**
 * Created by 김형성 on 2015-07-19.
 */




public class Notify_Schedule {


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

}

package net.whend.soodal.whend.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.util.Log;

import net.whend.soodal.whend.model.base.Schedule;
import net.whend.soodal.whend.model.top.Concise_Schedule;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/** 내부 일정 리스트를 가져오거나 일정을 캘린더에 넣기 기능을 구현하기 위함.
 * Created by wonkyung on 15. 7. 15.
 */
public class CalendarProviderUtil {

    private ArrayList<Schedule> innerScheduleList = new ArrayList<Schedule>();
    private Context mContext;
    private int amount=10;     // 가져올 일정 개수
    public static final String[] CALENDAR_PROJECTION = new String[] {
            CalendarContract.Calendars._ID,                           // 0
            CalendarContract.Calendars.ACCOUNT_NAME,                  // 1
            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,         // 2
            CalendarContract.Calendars.OWNER_ACCOUNT,                  // 3
            CalendarContract.Calendars.NAME
    };
    // The indices for the projection array above.
    private static final int PROJECTION_ID_INDEX = 0;
    private static final int PROJECTION_ACCOUNT_NAME_INDEX = 1;
    private static final int PROJECTION_DISPLAY_NAME_INDEX = 2;
    private static final int PROJECTION_OWNER_ACCOUNT_INDEX = 3;
    public static final String[] EVENT_PROJECTION =  new String[]{
            CalendarContract.Events._ID,
            CalendarContract.Events.DTSTART,
            CalendarContract.Events.DTEND,
            CalendarContract.Events.RRULE,
            CalendarContract.Events.TITLE,
            CalendarContract.Events.DESCRIPTION,
            CalendarContract.Events.EVENT_TIMEZONE,
            CalendarContract.Events.EVENT_END_TIMEZONE,
            CalendarContract.Events.DURATION,
            CalendarContract.Events.ALL_DAY,
            CalendarContract.Events.RDATE,
            CalendarContract.Events.AVAILABILITY
    };


    // Constructors
    public CalendarProviderUtil(Context mContext){
        this.mContext = mContext;
        includeInnerScheduleList();
    }

    // functions
    public void includeInnerScheduleList(){
        Uri uri2 = CalendarContract.Events.CONTENT_URI;
        ContentResolver cr2 = mContext.getContentResolver();
        Cursor cur2 = cr2.query(uri2, EVENT_PROJECTION, CalendarContract.Events._ID+">="+
                ( (obtainLatestEventId()-amount)>0?(obtainLatestEventId()-amount):0),null, null);

        Calendar calendar = null;
        while (cur2.moveToNext()) {
/*Ref
            CalendarContract.Events._ID,
            CalendarContract.Events.DTSTART,
            CalendarContract.Events.DTEND,
            CalendarContract.Events.RRULE,
            CalendarContract.Events.TITLE,
            CalendarContract.Events.DESCRIPTION,
            CalendarContract.Events.EVENT_TIMEZONE,
            CalendarContract.Events.EVENT_END_TIMEZONE,
            CalendarContract.Events.DURATION,
            CalendarContract.Events.ALL_DAY,
            CalendarContract.Events.RDATE,
            CalendarContract.Events.AVAILABILITY
             */
            AppPrefs appPrefs = new AppPrefs(mContext);
            String username = appPrefs.getUsername();

            Schedule tmpSchedule = new Schedule();
            tmpSchedule.setTitle(cur2.getString(4));
            tmpSchedule.setStarttime_ms(cur2.getLong(1));
            tmpSchedule.setEndtime_ms(cur2.getLong(2));

            tmpSchedule.setUploaded_username(username);
            tmpSchedule.setAllday((cur2.getInt(8) == 1) ? true : false);
            tmpSchedule.setMemo(cur2.getString(5));
            tmpSchedule.setTimezone(cur2.getString(6));

            // datetime ms -> string
            DateTimeFormatter dtf_starttime = new DateTimeFormatter(tmpSchedule.getStarttime_ms());
            tmpSchedule.setStarttime(dtf_starttime.getOutputString());

            DateTimeFormatter dtf_endtime = new DateTimeFormatter(tmpSchedule.getStarttime_ms());
            tmpSchedule.setEndtime(dtf_endtime.getOutputString());
            innerScheduleList.add(tmpSchedule);
        }
    }

    public long obtainLatestCalendarId(){
        Cursor cur = null;
        ContentResolver cr = mContext.getContentResolver();

        Uri uri = CalendarContract.Calendars.CONTENT_URI;
        Cursor cursor = cr.query(uri, new String[]{"MAX(_id) as max_id"}, null, null, "_id");
        cursor.moveToFirst();

        long max_val = cursor.getLong(cursor.getColumnIndex("max_id"));

        return max_val;

    }

    public long obtainLatestEventId(){

        Uri uri = CalendarContract.Events.CONTENT_URI;
        ContentResolver cr = mContext.getContentResolver();
        Cursor cursor = cr.query(uri, new String [] {"MAX(_id) as max_id"}, null, null, "_id");
        cursor.moveToFirst();

        long max_val = cursor.getLong(cursor.getColumnIndex("max_id"));
        Log.d("maxVal", String.valueOf(max_val));
        return max_val;

    }

    // Getters and Setters
    public ArrayList<Schedule> getInnerScheduleList() {
        ArrayList<Schedule> tmp = new ArrayList<Schedule>();
        for(int i=0; i<innerScheduleList.size(); i++){
            tmp.add(innerScheduleList.get(innerScheduleList.size()-i-1));
        }
        innerScheduleList.clear();
        innerScheduleList = tmp;
        return innerScheduleList;
    }

    public void setInnerScheduleList(ArrayList<Schedule> innerScheduleList) {
        this.innerScheduleList = innerScheduleList;
    }


    /*
    private void syncDeviceCalendar() throws JSONException {


        calID.clear();
        displayName.clear();
        accountName.clear();
        ownerName.clear();
        eventID.clear();
        dtstart.clear();
        dtend.clear();
        rrule.clear();
        title.clear();
        mYear.clear();
        mMonth.clear();
        mDay.clear();

        Cursor cur = null;
        ContentResolver cr = mContext.getContentResolver();
        Uri uri = CalendarContract.Calendars.CONTENT_URI;
        //	String selection = "((" + Calendars.ACCOUNT_NAME + " = ?) AND ("
        //	                        + Calendars.ACCOUNT_TYPE + " = ?))";
        //	String[] selectionArgs = new String[]{"sampleuser@gmail.com", "com.google"};
        // Submit the query and get a Cursor object back.
        // 	cur = cr.query(uri, CALENDAR_PROJECTION, null, null, null);
        cur = cr.query(uri, CALENDAR_PROJECTION, CalendarContract.Calendars.VISIBLE + " = 1", null, null);
        // Use the cursor to step through the returned records



        while (cur.moveToNext()) {

            // Get the field values
            calID.add(cur.getLong(PROJECTION_ID_INDEX));
            displayName.add(cur.getString(PROJECTION_DISPLAY_NAME_INDEX));
            accountName.add(cur.getString(PROJECTION_ACCOUNT_NAME_INDEX));
            ownerName.add(cur.getString(PROJECTION_OWNER_ACCOUNT_INDEX));

            //	    Log.d("calid",String.valueOf(cur.getLong(PROJECTION_ID_INDEX)));



            Uri uri2 = CalendarContract.Events.CONTENT_URI;
            ContentResolver cr2 = mContext.getContentResolver();
            Cursor cur2 = cr2.query(uri2, EVENT_PROJECTION, CalendarContract.Events.CALENDAR_ID+" ="+cur.getLong(PROJECTION_ID_INDEX),null, null);


            JSONObject evtpropjson = null;
            Calendar calendar = null;
            while (cur2.moveToNext()) {
                evtpropjson = new JSONObject();
                // Get the field values
                eventID.add(cur2.getLong(0));
                dtstart.add(cur2.getLong(1));
                dtend.add(cur2.getLong(2));
                rrule.add(cur2.getString(3));
                title.add(cur2.getString(4));
                // 	   	Log.d("title",cur2.getString(4));
                calendar = Calendar.getInstance();
                calendar.setTimeInMillis(cur2.getLong(1));
                Calendar endCal = Calendar.getInstance();
                endCal.setTimeInMillis(cur2.getLong(2));



                mYear.add(calendar.get(Calendar.YEAR));
                mMonth.add(calendar.get(Calendar.MONTH));
                mDay.add(calendar.get(Calendar.DAY_OF_MONTH));
                //	    Log.d("mYear",String.valueOf(calendar.get(Calendar.YEAR)));
                //	    Log.d("mMonth",String.valueOf(calendar.get(Calendar.MONTH)));
                //	    Log.d("mDay",String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));

            }



        }

    }
*/



}
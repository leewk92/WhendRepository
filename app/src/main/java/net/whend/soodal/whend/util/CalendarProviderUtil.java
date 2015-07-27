package net.whend.soodal.whend.util;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
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
import java.util.TimeZone;

/** 내부 일정 리스트를 가져오거나 일정을 캘린더에 넣기 기능을 구현하기 위함.
 * Created by wonkyung on 15. 7. 15.
 */
public class CalendarProviderUtil {

    private ArrayList<Schedule> innerScheduleList = new ArrayList<Schedule>();
    private Context mContext;
    private long whendCalendarId;
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

            DateTimeFormatter dtf_endtime = new DateTimeFormatter(tmpSchedule.getEndtime_ms());
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
        Cursor cursor = cr.query(uri, new String[]{"MAX(_id) as max_id"}, null, null, "_id");
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

    public void addScheduleToInnerCalendar(Concise_Schedule cs){
        AppPrefs appPrefs = new AppPrefs(mContext);
        long calID = appPrefs.getWhendCalendarAccountId();
        Log.d("calID",String.valueOf(calID));

        // 중복 처리
        Uri uri_event = CalendarContract.Events.CONTENT_URI;
        ContentResolver cr_event = mContext.getContentResolver();

        String selection = "((" + CalendarContract.Events.CALENDAR_ID + " = ?) AND ("
                + CalendarContract.Events.TITLE + " = ?) AND ("
                + CalendarContract.Events.DTSTART + " = ?))";
        String[] selectionArgs = new String[] {appPrefs.getWhendCalendarAccountId()+""
                , cs.getTitle()
                , cs.getSchedule().getStarttime_ms()+""};

        Cursor cur_event = cr_event.query(uri_event, EVENT_PROJECTION, selection,selectionArgs, null);

        if(cur_event.moveToNext() == false ){
            AccountManager accountManager = AccountManager.get(mContext); //this is Activity
            Account account = accountManager.getAccounts()[0];

            ContentResolver cr = mContext.getApplicationContext().getContentResolver();
            ContentValues values = new ContentValues();
            values.put(CalendarContract.Events.DTSTART, cs.getSchedule().getStarttime_ms());
            values.put(CalendarContract.Events.DTEND, cs.getSchedule().getEndtime_ms());
            values.put(CalendarContract.Events.TITLE, cs.getTitle());
            values.put(CalendarContract.Events.DESCRIPTION, cs.getMemo());
            values.put(CalendarContract.Events.CALENDAR_ID, calID);
            values.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().toString());

            // Uri creationUri = asSyncAdapter(CalendarContract.Events.CONTENT_URI, account.name, account.type);
            // Uri uri = mContext.getContentResolver().insert(creationUri, values);

            Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
            long id = Long.parseLong(uri.getLastPathSegment());

            Log.d("added schedule id", id + "");
        }else{
            Log.d("repeated event","true");
        }
    }

    public void deleteScheduleFromInnerCalendar(Concise_Schedule cs){
        AppPrefs appPrefs = new AppPrefs(mContext);
        Uri uri_event = CalendarContract.Events.CONTENT_URI;
        ContentResolver cr_event = mContext.getContentResolver();

        String selection = "((" + CalendarContract.Events.CALENDAR_ID + " = ?) AND ("
                + CalendarContract.Events.TITLE + " = ?) AND ("
                + CalendarContract.Events.DTSTART + " = ?))";
        String[] selectionArgs = new String[] {appPrefs.getWhendCalendarAccountId()+""
                , cs.getTitle()
                , cs.getSchedule().getStarttime_ms()+""};

        Cursor cur_event = cr_event.query(uri_event, EVENT_PROJECTION, selection,selectionArgs, null);

        if(cur_event.moveToNext()==true ){

            long eventID = cur_event.getLong(0);

            ContentResolver cr = mContext.getContentResolver();
            ContentValues values = new ContentValues();
            Uri deleteUri = null;
            deleteUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventID);
            int rows = cr.delete(deleteUri, null, null);

        }else{
            Log.d("no schedule for deletin","true");
        }
    }
    public void addAccountOfCalendar(){
        AppPrefs appPrefs = new AppPrefs(mContext);
        appPrefs.setWhendCalendarAccountId(0);
        AccountManager accountManager = AccountManager.get(mContext); //this is Activity
        Account account = new Account("Whend","net.whend.soodal.account.DEMOACCOUNT");
        boolean success = accountManager.addAccountExplicitly(account, "password", null);
        if(success){
            Log.d("Account","Account created");
        }else{
            Log.d("Account","Account creation failed. Look at previous logs to investigate");
        }
        createCalendar(mContext,account);
    }

    void createCalendar(Context mContext, Account account)
    {
        AppPrefs appPrefs = new AppPrefs(mContext);

        Uri uri_cal = CalendarContract.Calendars.CONTENT_URI;
        ContentResolver cr_cal = mContext.getContentResolver();

        String selection = "((" + CalendarContract.Calendars.NAME + " = ?) AND ("
                + CalendarContract.Calendars.ACCOUNT_NAME + " = ?))";
        String[] selectionArgs = new String[] {account.name+""
                , account.name+ ""};

        Cursor cur_event = cr_cal.query(uri_cal, CALENDAR_PROJECTION, selection,selectionArgs, null);

        boolean alreadyCreated;
        if(cur_event.moveToNext()==true ){
            alreadyCreated = true;
        }else alreadyCreated = false;
// 이미 캘린더 만들었으면 안만듦 !
        if(alreadyCreated==false) {
            final ContentValues v = new ContentValues();
            v.put(CalendarContract.Calendars.NAME, account.name);
            v.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, account.name);
            v.put(CalendarContract.Calendars.ACCOUNT_NAME, account.name);
            v.put(CalendarContract.Calendars.ACCOUNT_TYPE, account.type);
            v.put(CalendarContract.Calendars.CALENDAR_COLOR, Color.rgb(3,169,245));
            v.put(CalendarContract.Calendars.OWNER_ACCOUNT, account.name);
            v.put(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL, CalendarContract.Calendars.CAL_ACCESS_OWNER);
            v.put(CalendarContract.Calendars._ID, obtainLatestCalendarId() + 1);// u can give any id there and use same id any where u need to create event
            v.put(CalendarContract.Calendars.SYNC_EVENTS, 1);
            v.put(CalendarContract.Calendars.VISIBLE, 1);
            Uri creationUri = asSyncAdapter(CalendarContract.Calendars.CONTENT_URI, account.name, account.type);
            Uri calendarData = mContext.getContentResolver().insert(creationUri, v);
            long id = Long.parseLong(calendarData.getLastPathSegment());
            whendCalendarId = id;
            appPrefs.setWhendCalendarAccountId((int)Long.parseLong(calendarData.getLastPathSegment()));        // 왜 안되는지 모르겠음
            Log.d("whendCalendar_last", id + "");

            Log.d("whendCalendarAccountId",appPrefs.getWhendCalendarAccountId()+"");
        }else {

            Log.d("whendCalendarAccountId_", appPrefs.getWhendCalendarAccountId() + "");
        }
    }
    private Uri asSyncAdapter(Uri uri, String account, String accountType)
    {
        return uri.buildUpon().appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, account)
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, accountType)
                .build();
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

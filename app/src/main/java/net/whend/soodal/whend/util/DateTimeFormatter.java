package net.whend.soodal.whend.util;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
/**
 * Created by wonkyung on 15. 7. 14.
 */
public class DateTimeFormatter {
    private String parsePattern = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    private String inputString;
    private String outputString;
    private String year;
    private String date;
    private String time;


    private long datetime_ms, datetime_ms_utc;
    Calendar cal;
    SimpleDateFormat odf = new SimpleDateFormat(parsePattern, Locale.KOREA);

    // Constructors
    public DateTimeFormatter() {
        cal = Calendar.getInstance();

        cal.setTimeZone(TimeZone.getDefault());
        odf.setTimeZone(TimeZone.getTimeZone("UTC"));

        datetime_ms = (cal.getTimeInMillis()/1000)*1000;
        outputString = odf.format(new Date(datetime_ms));
        Parse();

//"MM월 dd일/HH:mm"
    }
    public DateTimeFormatter(Long datetime_ms){
        cal = Calendar.getInstance();

        cal.setTimeZone(TimeZone.getDefault());
        odf.setTimeZone(TimeZone.getDefault());
//        odf.setTimeZone(TimeZone.getTimeZone("UTC"));

        outputString = odf.format(new Date((datetime_ms / 1000) * 1000));
        Log.d("outputStringFormatter",outputString);

        cal.setTime(odf.getCalendar().getTime());
        Parse();
    }
    public DateTimeFormatter(Long datetime_ms,TimeZone timezone){
        cal = Calendar.getInstance();

        cal.setTimeZone(timezone);
        odf.setTimeZone(timezone);
//        odf.setTimeZone(TimeZone.getTimeZone("UTC"));

        outputString = odf.format(new Date((datetime_ms / 1000) * 1000));
        Log.d("outputStringFormatter",outputString);

        cal.setTime(odf.getCalendar().getTime());
        Parse();
    }
    public Long LocaleToUTC(Long locale_datetime){
        cal = Calendar.getInstance();

        cal.setTimeZone(TimeZone.getDefault());
//        odf.setTimeZone(TimeZone.getDefault());
        odf.setTimeZone(TimeZone.getTimeZone("UTC"));

        outputString = odf.format(new Date((locale_datetime / 1000) * 1000));
        Log.d("outputStringFormatter", outputString);

        cal.setTime(odf.getCalendar().getTime());
        return cal.getTimeInMillis();

    }
    public DateTimeFormatter(String inputString){
        this.inputString = inputString;
        cal = Calendar.getInstance();
        try{
            Log.d("time", inputString);
            odf.setTimeZone(TimeZone.getTimeZone("UTC"));
            cal.setTime(odf.parse(inputString));// all done
        }catch(Exception e) {

        }

        Parse();
    }

    public void Parse(){
        this.year = cal.get(Calendar.YEAR)+"년";
        this.date = String.format("%d", cal.get(Calendar.MONTH) + 1) + "월"+  String.format("%d", cal.get(Calendar.DAY_OF_MONTH))+"일";
        this.time =  String.format("%02d", cal.get(Calendar.HOUR_OF_DAY))+":" +String.format("%02d", cal.get(Calendar.MINUTE));

        this.datetime_ms = (cal.getTimeInMillis()/1000)*1000;
        cal.setTimeZone(TimeZone.getTimeZone("UTC"));
        this.datetime_ms_utc = (cal.getTimeInMillis()/1000)*1000;
  //      getUTC();
    }

    public void getUTC(){
        TimeZone tz = new TimeZone() {
            @Override
            public int getOffset(int era, int year, int month, int day, int dayOfWeek, int timeOfDayMillis) {
                return 0;
            }

            @Override
            public int getRawOffset() {
                return 0;
            }

            @Override
            public boolean inDaylightTime(Date time) {
                return false;
            }

            @Override
            public void setRawOffset(int offsetMillis) {

            }

            @Override
            public boolean useDaylightTime() {
                return false;
            }
        };
        tz.setID(TimeZone.getDefault().getID());
        for(int i=0; i<tz.getAvailableIDs().length; i++){
            Log.d("getTimezoneID", tz.getAvailableIDs()[i].toString());
        }
        Log.d("thisTimezoneID", TimeZone.getDefault().getID().toString());
        Log.d("calTimezoneID",Calendar.getInstance().getTimeZone().getID().toString());
    }

    //Getters and Setters


    public Calendar getCalendar() {
        return cal;
    }

    public void setCalendar(Calendar cal) {
        this.cal = cal;
    }

    public long getDatetime_ms() {
        return datetime_ms;
    }

    public void setDatetime_ms(long datetime_ms) {
        this.datetime_ms = datetime_ms;
    }

    public String getParsePattern() {
        return parsePattern;
    }

    public void setParsePattern(String parsePattern) {
        this.parsePattern = parsePattern;
    }

    public String getInputString() {
        return inputString;
    }

    public void setInputString(String inputString) {
        this.inputString = inputString;
    }

    public String getOutputString() {
        return outputString;
    }

    public void setOutputString(String outputString) {
        this.outputString = outputString;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}

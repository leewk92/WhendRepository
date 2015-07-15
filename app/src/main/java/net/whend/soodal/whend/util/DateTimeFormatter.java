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
    Calendar cal;
    SimpleDateFormat odf = new SimpleDateFormat(parsePattern, Locale.KOREA);

    // Constructors
    public DateTimeFormatter() {
//"MM월 dd일/HH:mm"
    }
    public DateTimeFormatter(Long datetime_ms){
        cal = Calendar.getInstance();
        outputString = odf.format(new Date(datetime_ms));
        Log.d("outputStringFormatter",outputString);

        cal.setTime(odf.getCalendar().getTime());
        Parse();
    }
    public DateTimeFormatter(String inputString){
        this.inputString = inputString;
        cal = Calendar.getInstance();
        try{
            Log.d("time", inputString);
            cal.setTime(odf.parse(inputString));// all done
        }catch(Exception e) {

        }

        Parse();
    }

    public void Parse(){
        this.year = cal.get(Calendar.YEAR)+"년 ";
        this.date = cal.get(Calendar.MONTH)+"월 "+cal.get(Calendar.DAY_OF_MONTH)+"일";
        this.time = cal.get(Calendar.HOUR)+":"+cal.get(Calendar.MINUTE);

    }

    //Getters and Setters

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

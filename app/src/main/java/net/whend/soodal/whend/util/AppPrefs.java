package net.whend.soodal.whend.util;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
 
public class AppPrefs {
 private static final String USER_PREFS = "net.whend.soodal.whend";
 private SharedPreferences appSharedPrefs;
 private Editor prefsEditor;
 private String token = "token";
 private String username = "username";
 private String mostRecentInnerCalendarEventId="mostRecentInnerCalendarEventId";
 private String whendCalendarAccountId="whendCalendarAccountId";
 private String caljson_str = null;
 private String primary = "primary";
 private String user_id = "user_id";

public AppPrefs(Context context){
 this.appSharedPrefs = context.getSharedPreferences(USER_PREFS, Activity.MODE_PRIVATE);
 this.prefsEditor = appSharedPrefs.edit();
 }
public String getUsername() {
 return appSharedPrefs.getString(username, "");
 }
public void setUsername(String _username) {
 prefsEditor.putString(username, _username).commit();
}

public int getPrimary(){
	return appSharedPrefs.getInt(primary, 0);
}
public void setPrimary(int _primary){
	prefsEditor.putInt(primary, _primary).commit();
}

public String getToken() {
 return appSharedPrefs.getString(token, "");
 }
 public void setToken(String _token) { prefsEditor.putString(token, _token).commit(); }

 public int getMostRecentInnerCalendarEventId(){
  return appSharedPrefs.getInt(mostRecentInnerCalendarEventId, 0);
 }
 public void setMostRecentInnerCalendarEventId(int _mostRecentInnerCalendarEventId){
  prefsEditor.putInt(mostRecentInnerCalendarEventId, _mostRecentInnerCalendarEventId).commit();
 }

 public int getWhendCalendarAccountId(){
  return appSharedPrefs.getInt(whendCalendarAccountId, 0);
 }

 public void setWhendCalendarAccountId(int _whendCalendarAccountId){
  prefsEditor.putInt(whendCalendarAccountId, _whendCalendarAccountId).commit();
 }

 public int getUser_id(){
  return appSharedPrefs.getInt(user_id, 0);
 }
 public void setUser_id(int _user_id){
  prefsEditor.putInt(user_id, _user_id).commit();
 }


 public String getCaljson_str (){
	 return appSharedPrefs.getString(caljson_str,"");
 }
 public void setCaljson_str(String _caljson_str){
	 prefsEditor.putString(caljson_str, _caljson_str);
 }
 
 
 
}
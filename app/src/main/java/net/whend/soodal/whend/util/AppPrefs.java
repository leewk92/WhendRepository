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
 private String mostRecentInnerCalendarEventId = "mostRecentInnerCalendarEventId";
 private String whendCalendarAccountId = "whendCalendarAccountId";
 private String caljson_str = null;
 private String password = "password";
 private String user_id = "user_id";
 private String gcm_token = "gcm_token";
 private String photo = "photo";
 private String unreadNotificationCount = "unreadNotificationCount";
 private String push_setting = "push_setting";
 private String alarm_setting = "alarm_setting";

 public AppPrefs(Context context) {
  this.appSharedPrefs = context.getSharedPreferences(USER_PREFS, Activity.MODE_PRIVATE);
  this.prefsEditor = appSharedPrefs.edit();
 }

 public String getUsername() {
  return appSharedPrefs.getString(username, "");
 }

 public void setUsername(String _username) {
  prefsEditor.putString(username, _username).commit();
 }

 public String getPassword() {
  return appSharedPrefs.getString(password, "");
 }

 public void setPassword(String _password) {
  prefsEditor.putString(password, _password).commit();
 }

 public String getToken() {
  return appSharedPrefs.getString(token, "");
 }

 public void setToken(String _token) {
  prefsEditor.putString(token, _token).commit();
 }

 public int getMostRecentInnerCalendarEventId() {
  return appSharedPrefs.getInt(mostRecentInnerCalendarEventId, 0);
 }

 public void setMostRecentInnerCalendarEventId(int _mostRecentInnerCalendarEventId) {
  prefsEditor.putInt(mostRecentInnerCalendarEventId, _mostRecentInnerCalendarEventId).commit();
 }

 public int getWhendCalendarAccountId() {
  return appSharedPrefs.getInt(whendCalendarAccountId, 0);
 }

 public void setWhendCalendarAccountId(int _whendCalendarAccountId) {
  prefsEditor.putInt(whendCalendarAccountId, _whendCalendarAccountId).commit();
 }

 public int getUser_id() {
  return appSharedPrefs.getInt(user_id, 0);
 }

 public void setUser_id(int _user_id) {
  prefsEditor.putInt(user_id, _user_id).commit();
 }


 public String getCaljson_str() {
  return appSharedPrefs.getString(caljson_str, "");
 }

 public void setCaljson_str(String _caljson_str) {
  prefsEditor.putString(caljson_str, _caljson_str);
 }

 public String getGcm_token() {
  return appSharedPrefs.getString(gcm_token, "");
 }

 public void setGcm_token(String _gcm_token) {
  prefsEditor.putString(gcm_token, _gcm_token);
 }

 public String getPhoto() {
  return appSharedPrefs.getString(photo, "");
 }

 public void setPhoto(String _photo) {
  prefsEditor.putString(gcm_token, _photo);
 }

 public int getUnreadNotificationCount() {
  return appSharedPrefs.getInt(unreadNotificationCount, 0);
 }

 public void setUnreadNotificationCount(int _unreadNotificationCount) {
  prefsEditor.putInt(unreadNotificationCount, _unreadNotificationCount).commit();
 }

 public Boolean getPush_setting() {
  return appSharedPrefs.getBoolean(push_setting, true);
 }

 public void setPush_setting(Boolean _push_setting) {
  prefsEditor.putBoolean(push_setting, _push_setting).commit();
 }

 public Boolean getAlarm_setting() {
  return appSharedPrefs.getBoolean(alarm_setting, true);
 }

 public void setAlarm_setting(Boolean _alarm_setting) {
  prefsEditor.putBoolean(alarm_setting, _alarm_setting).commit();
 }
}
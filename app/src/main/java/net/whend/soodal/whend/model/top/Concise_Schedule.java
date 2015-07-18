package net.whend.soodal.whend.model.top;

import android.util.Log;

import net.whend.soodal.whend.model.base.Comment;
import net.whend.soodal.whend.model.base.HashTag;
import net.whend.soodal.whend.model.base.Schedule;
import net.whend.soodal.whend.model.base.User;
import net.whend.soodal.whend.util.DateTimeFormatter;

/**
 * Created by wonkyung on 15. 7. 9.
 */
public class Concise_Schedule {

    private String username;
    private String title;
    private String date;
    private String time;
    private String location;
    private String photo_dir;
    private String photo_dir_fromweb;
    private String memo;
    private int like_count;
    private String best_friend_like;
    private int follow_count;
    private String best_friend_comment;
    private String best_comment;
    private HashTag[] hashTags;         // 나중에 생각
    private boolean isLike;         // 좋아요 상태 표시
    private boolean isFollow;       // 받아보기 상태 표시

    // Constructor
    public Concise_Schedule() {

        this.isLike=false;
        this.isFollow=false;
    }

    public Concise_Schedule(Schedule s){
        this.username = s.getUploaded_username();
        this.title = s.getTitle();
        //this.location = s.getLocation();
        this.photo_dir = "";
        this.photo_dir_fromweb = s.getPhoto_dir_fromweb();
        this.memo = s.getMemo();
        this.like_count = 0;
        this.follow_count = 0;
        this.isLike=false;
        this.isFollow=false;
        DateParse(s);
    }
    public Concise_Schedule(Schedule s, User best_friend_forLike, Comment best_friend_forComment){
        this.username = s.getUploaded_user().getUsername();
        this.title = s.getTitle();
        this.location = s.getLocation();
        this.photo_dir = s.getPhoto_dir();
        this.memo = s.getMemo();
        this.photo_dir_fromweb = s.getPhoto_dir_fromweb();
        this.like_count = s.getLike_count();
        this.best_friend_like = best_friend_forLike.getUsername();
        this.follow_count = s.getFollow_count();
        this.best_friend_comment = best_friend_forComment.getWrite_user().getUsername();
        this.best_comment = best_friend_forComment.getContents();

        this.isLike=false;
        this.isFollow=false;
        DateParse(s);
    }

    public void DateParse(Schedule s){
        Log.d("DateTimeFormatter", s.getStarttime());
        DateTimeFormatter df = new DateTimeFormatter(s.getStarttime());
        this.date = df.getDate();
        this.time = df.getTime();
    }
    // like 전환 함수
    public void clickLike(){
        if(isLike==true)
            isLike=false;
        else isLike=true;
    }
    // follow 전환 함수
    public void clickFollow(){
        if(isFollow==true)
            isFollow=false;
        else isFollow=true;
    }

    // Accessors
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
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
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public String getPhoto_dir() {
        return photo_dir;
    }
    public void setPhoto_dir(String photo_dir) {
        this.photo_dir = photo_dir;
    }
    public String getMemo() {
        return memo;
    }
    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getPhoto_dir_fromweb() {
        return photo_dir_fromweb;
    }

    public void setPhoto_dir_fromweb(String photo_dir_fromweb) {
        this.photo_dir_fromweb = photo_dir_fromweb;
    }

    public boolean isLike() {
        return isLike;
    }

    public boolean isFollow() {
        return isFollow;
    }

    public int getLike_count() {
        return like_count;
    }
    public HashTag[] getHashTags() {
        return hashTags;
    }
    public boolean getIsLike(){
        return isLike;
    }
    public boolean getIsFollow(){
        return isFollow;
    }
    // Mutators
    public void setLike_count(int like_count) {
        this.like_count = like_count;
    }
    public String getBest_friend_like() {
        return best_friend_like;
    }
    public void setBest_friend_like(String best_friend_like) {
        this.best_friend_like = best_friend_like;
    }
    public int getFollow_count() {
        return follow_count;
    }
    public void setFollow_count(int follow_count) {
        this.follow_count = follow_count;
    }
    public String getBest_friend_comment() {
        return best_friend_comment;
    }
    public void setBest_friend_comment(String best_friend_comment) {
        this.best_friend_comment = best_friend_comment;
    }
    public String getBest_comment() {
        return best_comment;
    }
    public void setBest_comment(String best_comment) {
        this.best_comment = best_comment;
    }
    public void setHashTags(HashTag[] hashTags) {
        this.hashTags = hashTags;
    }
    public void setIsLike(boolean isLike){
        this.isLike = isLike;
    }
    public void setIsFollow(boolean isFollow){
        this.isFollow = isFollow;
    }
}

package net.whend.soodal.whend.model.base;

import net.whend.soodal.whend.model.top.Grid_Search_Schedule;

import java.util.ArrayList;

/**
 * Definition : 사용자 정보 모델
 * Created by wonkyung on 15. 7. 9.
 */
public class User {

    // References : The User Model on Server (WK)
    /*
    username = models.CharField(_('username'), max_length=30, unique=True)
    first_name = models.CharField(_('first name'), max_length=30, blank=True)
    last_name = models.CharField(_('last name'), max_length=30, blank=True)
    email = models.EmailField(_('email address'), blank=True)
    is_staff = models.BooleanField(_('staff status'), default=False, help_text=_('Designates whether the user can log into this admin ''site.'))
    is_active = models.BooleanField(_('active'), default=True,)
    date_joined = models.DateTimeField(_('date joined'), default=timezone.now)
    */

    // References : The Account Model on Server (WK)
    /*
    user = models.OneToOneField(settings.AUTH_USER_MODEL)
            #    gender = models.ForeignKey(Genders)
    photo = models.ImageField(upload_to='profile/%Y/%m/%d')
    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)
    login_at = models.DateTimeField()
    schedule_count = models.PositiveIntegerField(default=0)
    following_people_count = models.PositiveIntegerField(default=0)
    following_hashtag_count = models.PositiveIntegerField(default=0)
    follower_count = models.PositiveIntegerField(default=0)
    */

    private String username; // user 이름 (WK)
    private int id;
    //    private String first_name;
//    private String last_name;
    private String email;                   // user email (WK)
    //    private boolean is_staff;
//    private boolean is_active;
//    private String date_joined;
    private String photo;                   // Download photo on cache directory (WK)
    //    private String created_at;
//    private String updated_at;
//    private String login_at;
    private int schedule_count;             // 올린 일정 개수 (WK)
    private int count_following_user;     // 받아보고 있는 사람 수 (WK)
    private int count_following_hashtag;    // 받아보고 있는 hashtag 수 (WK)
    private int count_follower;             // 몇명이 나를 follow 하고 있는지, follower수 (WK)
    private int count_uploaded_schedule;
    private int[] following_user;
    private int[] following_hashtag;
    private int[] following_schedule;
    private int[] like_schedule;

    public int[] getLike_schedule() {
        return like_schedule;
    }

    public void setLike_schedule(int[] like_schedule) {
        this.like_schedule = like_schedule;
    }

    public User(){

    }

    public String getUsername() {
        return username;
    }

    public int getCount_uploaded_schedule() {
        return count_uploaded_schedule;
    }

    public void setCount_uploaded_schedule(int count_uploaded_schedule) {
        this.count_uploaded_schedule = count_uploaded_schedule;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getSchedule_count() {
        return schedule_count;
    }

    public void setSchedule_count(int schedule_count) {
        this.schedule_count = schedule_count;
    }

    public int getCount_following_user() {
        return count_following_user;
    }

    public void setCount_following_user(int count_following_user) {
        this.count_following_user = count_following_user;
    }

    public int getCount_following_hashtag() {
        return count_following_hashtag;
    }

    public void setCount_following_hashtag(int count_following_hashtag) {
        this.count_following_hashtag = count_following_hashtag;
    }

    public int getCount_follower() {
        return count_follower;
    }

    public void setCount_follower(int count_follower) {
        this.count_follower = count_follower;
    }

    public int[] getFollowing_user() {
        return following_user;
    }

    public void setFollowing_user(int[] following_user) {
        this.following_user = following_user;
    }

    public int[] getFollowing_hashtag() {
        return following_hashtag;
    }

    public void setFollowing_hashtag(int[] following_hashtag) {
        this.following_hashtag = following_hashtag;
    }

    public int[] getFollowing_schedule() {
        return following_schedule;
    }

    public void setFollowing_schedule(int[] following_schedule) {
        this.following_schedule = following_schedule;
    }
}
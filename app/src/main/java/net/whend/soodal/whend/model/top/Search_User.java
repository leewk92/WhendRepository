package net.whend.soodal.whend.model.top;

import net.whend.soodal.whend.model.base.User;

/**
 * Created by wonkyung on 2015-07-13.
 */
public class Search_User {
    private User user;
    private String username;                // user 이름 (WK)
    private int schedule_count;             // 올린 일정 개수 (WK)
    private int following_people_count;     // 받아보고 있는 사람 수 (WK)
    private int following_hashtag_count;    // 받아보고 있는 hashtag 수 (WK)
    private int total_following_count;      // 위에꺼 더하기 위에위에꺼
    private int follower_count;             // 몇명이 나를 follow 하고 있는지, follower수 (WK)
    private boolean isFollow;               // 내가 이사람을 받아보고 있는지 여부

    // Constructors
    public Search_User() {
        this.following_people_count = 0;
        this.following_hashtag_count = 0;
        this.total_following_count = 0;
        this.follower_count = 0;
        this.schedule_count = 0;
    }
    public Search_User(User user){
        this.user = user;

    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isFollow() {
        return isFollow;
    }

    // Functions
    public void clickFollow(){
        if(isFollow==true) {
            isFollow = false;
            user.setCount_follower(user.getCount_follower()-1);
        }
        else {
            isFollow = true;
            user.setCount_follower(user.getCount_follower()+1);
        }
    }

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getSchedule_count() {
        return schedule_count;
    }

    public void setSchedule_count(int schedule_count) {
        this.schedule_count = schedule_count;
    }

    public int getFollowing_people_count() {
        return following_people_count;
    }

    public void setFollowing_people_count(int following_people_count) {
        this.following_people_count = following_people_count;
    }

    public int getFollowing_hashtag_count() {
        return following_hashtag_count;
    }

    public void setFollowing_hashtag_count(int following_hashtag_count) {
        this.following_hashtag_count = following_hashtag_count;
    }

    public int getTotal_following_count() {
        return total_following_count;
    }

    public void setTotal_following_count(int total_following_count) {
        this.total_following_count = total_following_count;
    }

    public int getFollower_count() {
        return follower_count;
    }

    public void setFollower_count(int follower_count) {
        this.follower_count = follower_count;
    }

    public void setIsFollow(boolean isFollow){
        this.isFollow = isFollow;
    }
    public boolean getIsFollow(){
        return isFollow;
    }
}

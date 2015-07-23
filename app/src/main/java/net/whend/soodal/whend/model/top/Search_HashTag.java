package net.whend.soodal.whend.model.top;

import net.whend.soodal.whend.model.base.HashTag;

/**
 * Created by wonkyung on 2015-07-13.
 */
public class Search_HashTag {

    HashTag hashTag;
    private int id;                     // 고유 아이디 (WK)
    private String title;               // HashTag 이름 (WK)
    private String photo;               // 대표사진 : Download photo on cache directory (WK)
    private int follower_count;         // 댓글을 소유하고 있는 스케줄의 고유 아이디 (WK)
    private String content;             // 안에 무슨 일정들이 있는지 대충 보여주는 용도
    private boolean isFollow;           // 내가 이 태그를 받고 있는지 여부

    // Constructor
    public Search_HashTag(){
        this.isFollow = false;
    }
    public Search_HashTag(HashTag h){
       this.hashTag = h;
        this.isFollow = h.is_Follow();
    }

    public HashTag getHashTag() {
        return hashTag;
    }

    public void setHashTag(HashTag hashTag) {
        this.hashTag = hashTag;
    }

    // Functions
    public void clickFollow() {
        if (isFollow == true){
            isFollow = false;
            this.hashTag.setFollower_count(this.hashTag.getFollower_count() - 1);
        }else {
            isFollow = true;
            this.hashTag.setFollower_count(this.hashTag.getFollower_count()+1);
        }
    }
    // getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getFollower_count() {
        return follower_count;
    }

    public void setFollower_count(int follower_count) {
        this.follower_count = follower_count;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean getIsFollow() {
        return isFollow;
    }

    public void setIsFollow(boolean isFollow) {
        this.isFollow = isFollow;
    }
}

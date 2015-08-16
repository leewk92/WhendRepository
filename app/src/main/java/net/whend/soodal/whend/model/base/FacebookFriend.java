package net.whend.soodal.whend.model.base;

/**
 * Created by wonkyung on 15. 8. 16.
 */
public class FacebookFriend {

    private String facebook_username;
    private String whend_username;
    private int user_id;
    private String facebook_photo;
    private boolean isFollow;

    public FacebookFriend() {

    }


    public String getFacebook_username() {
        return facebook_username;
    }

    public void setFacebook_username(String facebook_username) {
        this.facebook_username = facebook_username;
    }

    public String getWhend_username() {
        return whend_username;
    }

    public void setWhend_username(String whend_username) {
        this.whend_username = whend_username;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getFacebook_photo() {
        return facebook_photo;
    }

    public void setFacebook_photo(String facebook_photo) {
        this.facebook_photo = facebook_photo;
    }

    public boolean isFollow() {
        return isFollow;
    }

    public void setIsFollow(boolean isFollow) {
        this.isFollow = isFollow;
    }

    // Functions
    public void clickFollow(){
        if(isFollow==true) {
            isFollow = false;

        }
        else {
            isFollow = true;

        }
    }

}

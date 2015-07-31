package net.whend.soodal.whend.model.top;


import net.whend.soodal.whend.model.base.Schedule;
import net.whend.soodal.whend.model.base.User;

/**
 * Created by wonkyung on 15. 7. 12.
 */
public class ScheduleFollow_User {
    private User user;
    private String username;
    private String photo_dir;
    private String photo_dir_fromweb;
    private boolean isFollow;

    // Constructors
    public ScheduleFollow_User(){
        this.isFollow = false;
    }
    public ScheduleFollow_User(User user, boolean isFollow){
        this.username = user.getUsername();
        this.photo_dir = user.getUser_photo();
        this.user = user;
        this.isFollow = isFollow;
    }
    public ScheduleFollow_User(String username, String photo_dir, boolean isFollow){
        this.username = username;
        this.photo_dir = photo_dir;
        this.isFollow = isFollow;
    }
    public ScheduleFollow_User(String username, boolean isFollow){
        this.username = username;
        this.isFollow = isFollow;
    }

    // Functions
    public void clickFollow(){
        if(isFollow == true)
            isFollow = false;
        else isFollow = true;
    }

    // Accessors
    public String getUsername() {
        return username;
    }
    public String getPhoto_dir() {
        return photo_dir;
    }
    public boolean getIsFollow() {
        return isFollow;
    }
    // Mutators
    public void setUsername(String username) {
        this.username = username;
    }
    public void setPhoto_dir(String photo_dir) {
        this.photo_dir = photo_dir;
    }
    public void setIsFollow(boolean isFollow) {
        this.isFollow = isFollow;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPhoto_dir_fromweb() {
        return photo_dir_fromweb;
    }

    public void setPhoto_dir_fromweb(String photo_dir_fromweb) {
        this.photo_dir_fromweb = photo_dir_fromweb;
    }

    public boolean isFollow() {
        return isFollow;
    }
}

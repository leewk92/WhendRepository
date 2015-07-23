package net.whend.soodal.whend.model.base;

/**
 * Created by wonkyung on 15. 7. 9.
 */
public class HashTag {

    // References : The HashTag Model on Server (WK)
    /*
    title = models.CharField(max_length=80)
    photo = models.ImageField(upload_to='hash/%Y/%m/%d', blank=True)
    follower_count = models.PositiveIntegerField(default=0)
    content = models.TextField()
    created_at = models.DateTimeField(auto_now_add=True)
    uploaded_at = models.DateTimeField(auto_now=True)
    follow_user = models.ManyToManyField(User,related_name="user_follows_hashtags")
    */

    private int id;                     // 고유 아이디 (WK)
    private String title;               // HashTag 이름 (WK)
    private String photo;               // 대표사진 : Download photo on cache directory (WK)
    private int follower_count;         //
    private String content;             //
    private int follow_user[];            // 나중에 User class 상속 받을 예정 (WK)
    private boolean is_Follow;
    private int count_schedule;
    private int count_upcoming_schedule;

    public boolean is_Follow() {
        return is_Follow;
    }

    public void setIs_Follow(boolean is_Follow) {
        this.is_Follow = is_Follow;
    }

    public int getCount_schedule() {
        return count_schedule;
    }

    public void setCount_schedule(int count_schedule) {
        this.count_schedule = count_schedule;
    }

    public int getCount_upcoming_schedule() {
        return count_upcoming_schedule;
    }

    public void setCount_upcoming_schedule(int count_upcoming_schedule) {
        this.count_upcoming_schedule = count_upcoming_schedule;
    }

    public HashTag(){

    }
    public HashTag(int id, String title, String photo, int follower_count, String content){
        this.id = id;
        this.title = title;
        this.photo = photo;
        this.follower_count = follower_count;
        this.content = content;
        //this.follow_user = follow_user;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPhoto() {
        return photo;
    }

    public int getFollower_count() {
        return follower_count;
    }

    public String getContent() {
        return content;
    }

    public int[] getFollow_user() {
        return follow_user;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setFollower_count(int follower_count) {
        this.follower_count = follower_count;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setFollow_user(int[] follow_user) {
        this.follow_user = follow_user;
    }
}

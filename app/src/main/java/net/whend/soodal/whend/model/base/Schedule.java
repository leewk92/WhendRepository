package net.whend.soodal.whend.model.base;

/**
 * Definition : 일정 모델
 * Created by wonkyung on 15. 7. 9.
 */
public class Schedule {

    // References : The Schedule Model on Server (WK)
    /*
    title = models.CharField(max_length=80)
    starttime = models.DateTimeField(blank=True)
    endtime = models.DateTimeField(blank=True)
    starttime_ms = models.PositiveIntegerField(blank=True)
    endtime_ms = models.PositiveIntegerField(blank=True)
    memo = models.TextField()
    location = models.CharField(max_length=80, blank=True)
    photo = models.ImageField(upload_to='schedule/%Y/%m/%d', blank=True)
    allday = models.BooleanField(default=False)
    timezone = models.CharField(max_length=80, blank=True)
    created_at = models.DateTimeField(auto_now_add=True)
    uploaded_at = models.DateTimeField(auto_now = True)
    like_count = models.PositiveIntegerField(default=0)
    follow_count = models.PositiveIntegerField(default=0)
            #    upload_userid = models.PositiveIntegerField()
    hashtag = models.ManyToManyField(HashTag,related_name="schedule_has_hashtags", blank=True)
    like_user = models.ManyToManyField(User,related_name="user_likes_schedule", blank=True)
    follow_user = models.ManyToManyField(User,related_name="user_follows_schedule", blank=True)
    user = models.ForeignKey(settings.AUTH_USER_MODEL, default=1)
    */

    private int id;                     // 고유 아이디 (WK)
    private String title;
    private String starttime;           // in String (DateTime Type, must be parsed by using Calendar Class) (WK)
    private String endtime;             //      "
    private int starttime_ms;           // in milliseconds (WK)
    private int endtime_ms;             //      "
    private String memo;                // Contains HashTags and memo (WK)
    private String location;            // It'll be adjusted to Google(or Daum) Map API soon (WK)
    private String photo_dir;           // Download photo on cache directory (WK)
    private boolean allday;
    private String timezone;
//    private String created_at;        // Client doesn't need this field (WK)각
//    private String uploaded_at;       // "
    private int like_count;             // 좋아요 누른 사람 수 (WK)
    private int follow_count;
    private HashTag hashtag;            // 나중에 생
    private int follow_user;            // 캘린더에 넣은사람 (WK)
    private User uploaded_user;          // 올린사람 (WK)





    // Accessors
    public int getId(){
        return id;
    }
    public String getTitle(){
        return title;
    }
    public String getStarttime(){
        return starttime;
    }
    public String getEndtime(){
        return endtime;
    }
    public int getStarttime_ms(){
        return starttime_ms;
    }
    public int getEndtime_ms(){
        return endtime_ms;
    }
    public String getMemo(){
        return memo;
    }
    public String getLocation(){
        return location;
    }
    public String getPhoto_dir(){
        return photo_dir;
    }
    public boolean getAllday(){
        return allday;
    }
    public String getTimezone(){
        return timezone;
    }
    public int getLike_count(){
        return like_count;
    }
    public int getFollow_count(){
        return follow_count;
    }
    public HashTag getHashtag(){
        return hashtag;
    }
    public int getFollow_user(){
        return follow_user;
    }
    public User getUploaded_user(){
        return uploaded_user;
    }

    // Mutators
    public void setId(int id){
        this.id = id;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public void setStarttime(String starttime){
        this.starttime=starttime;
    }
    public void setEndtime(String endtime){
        this.endtime=endtime;
    }
    public void setStarttime_ms(int starttime_ms){
        this.starttime_ms = starttime_ms;
    }
    public void setEndtime_ms(int endtime_ms){
        this.endtime_ms = endtime_ms;
    }
    public void setMemo(String memo){
        this.memo = memo;
    }
    public void setLocation(String location){
        this.location= location;
    }
    public void setPhoto_dir(String photo_dir){
        this.photo_dir = photo_dir;
    }
    public void setAllday(boolean allday){
        this.allday = allday;
    }
    public void setTimezone(String timezone){
        this.timezone = timezone;
    }
    public void setLike_count(int like_count){
        this.like_count = like_count;
    }
    public void setFollow_count(int follow_count){
        this.follow_count = follow_count;
    }
    public void setHashtag(HashTag hashtag){
        this.hashtag = hashtag;
    }
    public void setFollow_user(int follow_user){
        this.follow_user = follow_user;
    }
    public void setUploaded_user(User uploaded_user){
        this.uploaded_user = uploaded_user;
    }

}
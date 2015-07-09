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
//    private String created_at;        // Client doesn't need this field (WK)
//    private String uploaded_at;       // "
    private int like_count;             // 좋아요 누른 사람 수 (WK)
    private int follow_user;            // 캘린더에 넣은사람 (WK)
    private int uploaded_user;          // 올린사람 (WK)


}

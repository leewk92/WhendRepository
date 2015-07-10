package net.whend.soodal.whend.model.base;

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

    private String username;                // user 이름 (WK)
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
    private int following_people_count;     // 받아보고 있는 사람 수 (WK)
    private int following_hashtag_count;    // 받아보고 있는 hashtag 수 (WK)
    private int follower_count;             // 몇명이 나를 follow 하고 있는지, follower수 (WK)

    // Accessors
    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoto() {
        return photo;
    }

    public int getSchedule_count() {
        return schedule_count;
    }

    public int getFollowing_people_count() {
        return following_people_count;
    }

    public int getFollowing_hashtag_count() {
        return following_hashtag_count;
    }

    public int getFollower_count() {
        return follower_count;
    }

    // Mutators
    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setSchedule_count(int schedule_count) {
        this.schedule_count = schedule_count;
    }

    public void setFollowing_people_count(int following_people_count) {
        this.following_people_count = following_people_count;
    }

    public void setFollowing_hashtag_count(int following_hashtag_count) {
        this.following_hashtag_count = following_hashtag_count;
    }

    public void setFollower_count(int follower_count) {
        this.follower_count = follower_count;
    }
}
package net.whend.soodal.whend.model;

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
    private int follower_count;         // 댓글을 소유하고 있는 스케줄의 고유 아이디 (WK)
    private String content;             // 안에 무슨 일정들이 있는지 대충 보여주는 용도
    private int follow_user;            // 나중에 User class 상속 받을 예정 (WK)

}

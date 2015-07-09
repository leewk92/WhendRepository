package net.whend.soodal.whend.model;

/**
 * Definition : 댓글 모델
 * Created by wonkyung on 15. 7. 9.
 */
public class Comment {

    // References : The Comment Model on Server (WK)
    /*
    title = models.CharField(max_length=80)
    contents = models.CharField(max_length=400)
    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)
    schedule_id = models.PositiveIntegerField()
    write_user = models.OneToOneField(User,related_name="who_writes_comment")
    */
    private int id;                     // 고유 아이디 (WK)
    private String title;
    private String contents;            // 댓글 내용 (WK)
    private String schedule_id;         // 댓글을 소유하고 있는 스케줄의 고유 아이디 (WK)
    private int write_user;             // 나중에 User class 상속 받을 예정 (WK)

}

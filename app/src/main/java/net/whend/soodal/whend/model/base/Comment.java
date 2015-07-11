package net.whend.soodal.whend.model.base;

/**
 * Definition : 댓글 모델
 * Created by wonkyung on 15. 7. 9.
 */
public class Comment {

    // References : The Comment Model on Server (WK)
    /*
    title = models.CharField(max_length=80)             // 이건 왜있는걸까 나중에확인
    contents = models.CharField(max_length=400)
    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)
    schedule_id = models.PositiveIntegerField()
    write_user = models.OneToOneField(User,related_name="who_writes_comment")
    */
    private int id;                     // 고유 아이디 (WK)
    private String title;               // 나중에 확인 왜있는걸까
    private String contents;            // 댓글 내용 (WK)
    private String schedule_id;         // 댓글을 소유하고 있는 스케줄의 고유 아이디 (WK)
    private User write_user;             // 나중에 User class 상속 받을 예정 (WK)

    // Accessors
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
    public String getContents() {
        return contents;
    }

    // Mutators
    public void setContents(String contents) {
        this.contents = contents;
    }
    public String getSchedule_id() {
        return schedule_id;
    }
    public void setSchedule_id(String schedule_id) {
        this.schedule_id = schedule_id;
    }
    public User getWrite_user() {
        return write_user;
    }
    public void setWrite_user(User write_user) {
        this.write_user = write_user;
    }
}

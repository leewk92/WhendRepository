package net.whend.soodal.whend.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import net.whend.soodal.whend.R;
import net.whend.soodal.whend.form.WriteComment_Adapter;
import net.whend.soodal.whend.model.base.Comment;

import java.util.ArrayList;

/**
 * Created by wonkyung on 15. 7. 12.
 */
public class A6_WriteCommentActivity extends Activity {
    ArrayList<Comment> Comment_list;
    ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a6_writecomment_layout);

        Comment_list = new ArrayList<Comment>();
        Comment a= new Comment();
        Comment_list.add(a);
        Comment_list.add(a);
        Comment_list.add(a);
        Comment_list.add(a);


        WriteComment_Adapter adapter = new WriteComment_Adapter(this,R.layout.item_writecomments,Comment_list);
        listview = (ListView) findViewById(R.id.listview_comments);
        listview.setAdapter(adapter);

    }
}

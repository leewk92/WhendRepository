package net.whend.soodal.whend.form;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.text.method.LinkMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnItemClickListener;
import com.squareup.picasso.Picasso;

import net.whend.soodal.whend.R;
import net.whend.soodal.whend.model.top.Concise_Schedule;
import net.whend.soodal.whend.util.AppPrefs;
import net.whend.soodal.whend.util.CalendarProviderUtil;
import net.whend.soodal.whend.util.CircleTransform;
import net.whend.soodal.whend.util.HTTPRestfulUtilizer;
import net.whend.soodal.whend.util.PicassoImageTool;
import net.whend.soodal.whend.util.SpannableStringMaker;
import net.whend.soodal.whend.util.TextViewFixTouchConsume;
import net.whend.soodal.whend.view.A11_EditScheduleActivity;
import net.whend.soodal.whend.view.A2_UserProfileActivity;
import net.whend.soodal.whend.view.A5_WhoFollowsScheduleActivity;
import net.whend.soodal.whend.view.A6_WriteCommentActivity;
import net.whend.soodal.whend.view.MainActivity;

import java.util.ArrayList;

/**
 * Wall 에서 일정을 간단한 카드 형식 리스트로 보여주기 위한 어답터
 * Created by wonkyung on 15. 7. 9.
 */
public class Concise_Schedule_Adapter extends ArrayAdapter<Concise_Schedule> {

    private ArrayList<Concise_Schedule> CSchedule_list;
    private Context context;
    ImageView memo_photo, user_photo;
    // 리스너 함수들
    View user_clickableLayout;

    ImageView like_button ;
    ImageView follow_button;
    ImageView comment_button;
    ImageView full_screen;
    ImageView edit;
    View schedulefollow_user_clickablelayout;
    View schedulelike_user_clickablelayout;
    TextView like_count ;
    TextView follow_count;



    final float scale = getContext().getResources().getDisplayMetrics().density;
    final int dp25_in_pixels = (int) (25 * scale + 0.5f);
    final int dp75_in_pixels = (int) (75 * scale + 0.5f);



    public Concise_Schedule_Adapter(Context context, int textViewResourceId, ArrayList<Concise_Schedule> lists){
        super(context, textViewResourceId, lists);
        this.CSchedule_list = lists;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        ViewHolder holder = null;
        View v = convertView;
        if (v == null) {

            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = li.inflate(R.layout.item_concise_schedule,parent, false);
            holder = new ViewHolder();
            holder.memo_photo_vh = (ImageView) v.findViewById(R.id.memo_photo);
            holder.user_photo_vh = (ImageView) v.findViewById(R.id.user_photo);
            holder.edit_vh = (ImageView)v.findViewById(R.id.edit);
            holder.user_fullname_vh = (TextView)v.findViewById(R.id.user_fullname);
            holder.title_vh = (TextView)v.findViewById(R.id.title);
            holder.date_vh = (TextView)v.findViewById(R.id.date);
            holder.time_vh = (TextView)v.findViewById(R.id.time);
            holder.memo_vh = (TextView)v.findViewById(R.id.memo);
            holder.like_count_vh = (TextView)v.findViewById(R.id.like_count);
            holder.follow_count_vh = (TextView)v.findViewById(R.id.follow_count);
            holder.location_vh = (TextView)v.findViewById(R.id.location);
            holder.comment_count_vh = (TextView)v.findViewById(R.id.comment_count);
            holder.like_button_vh = (ImageView)v.findViewById(R.id.like_button);
            holder.follow_button_vh = (ImageView)v.findViewById(R.id.follow_button);
            holder.user_clickableLayout_vh = (View)v.findViewById(R.id.user_clickableLayout);
            holder.schedulefollow_user_clickablelayout_vh = (View)v.findViewById(R.id.schedulefollow_user_clickablelayout);
            holder.schedulelike_user_clickablelayout_vh = (View)v.findViewById(R.id.schedulelike_user_clickablelayout);
            holder.full_screen_vh = (ImageView) v.findViewById(R.id.fullscreen_image);

            v.setTag(holder);
        } else{
            holder = (ViewHolder) v.getTag();
        }


        // 리스너 함수들
        user_clickableLayout = (View)v.findViewById(R.id.user_clickableLayout);
        //View comment_writer = (View)v.findViewById(R.id.comment_writer);
        like_button = (ImageView)v.findViewById(R.id.like_button);
        follow_button = (ImageView)v.findViewById(R.id.follow_button);
        comment_button = (ImageView)v.findViewById(R.id.comment_button);
        memo_photo = (ImageView)v.findViewById(R.id.memo_photo);
        user_photo = (ImageView)v.findViewById(R.id.user_photo);
        schedulefollow_user_clickablelayout = (View)v.findViewById(R.id.schedulefollow_user_clickablelayout);
        schedulelike_user_clickablelayout = (View)v.findViewById(R.id.schedulelike_user_clickablelayout);
        like_count = (TextView)v.findViewById(R.id.like_count);
        follow_count = (TextView)v.findViewById(R.id.follow_count);
        edit = (ImageView)v.findViewById(R.id.edit);

        v.setTag(holder);

       // try{
        AdjustDataToLayout(v,position,holder);
        //}catch(Exception e){}


        return v;
    }
    // edit 버튼 리스너
    public void EditClieckListener(ImageView edit, final int position){
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogPlus dialog = DialogPlus.newDialog(context)
                        .setAdapter(new Popup_Menu_Adapter(context, false))
                        .setOnItemClickListener(new OnItemClickListener() {
                            @Override
                            public void onItemClick(DialogPlus dialog, Object item, View view, int position) {

                                switch (position) {
                                    case 0:
                                        Concise_Schedule cs = CSchedule_list.get(position);
                                        Intent intent = new Intent(context, A11_EditScheduleActivity.class);
                                        intent.putExtra("id", cs.getId());
                                        intent.putExtra("date_start", cs.getDate_start());
                                        intent.putExtra("date_end", cs.getDate_end());
                                        intent.putExtra("time_start", cs.getTime_start());
                                        intent.putExtra("time_end", cs.getTime_end());
                                        intent.putExtra("content", cs.getTitle());
                                        intent.putExtra("location", cs.getLocation());
                                        intent.putExtra("datetime_start", cs.getSchedule().getStarttime_ms());
                                        intent.putExtra("datetime_end", cs.getSchedule().getEndtime_ms());
                                        intent.putExtra("allday", cs.getSchedule().getAllday());
                                        intent.putExtra("memo", cs.getMemo());

                                        Activity activity = (Activity) context;
                                        activity.startActivity(intent);
                                        activity.overridePendingTransition(R.anim.abc_popup_enter, R.anim.abc_popup_exit);
                                        dialog.dismiss();
                                        break;
                                    case 1:
                                        String url_delete = "http://119.81.176.245/schedules/" + CSchedule_list.get(position).getId() + "/";
                                        HTTPRestfulUtilizerExtender_delete hd = new HTTPRestfulUtilizerExtender_delete(context, url_delete, "DELETE");
                                        hd.doExecution();
                                        dialog.dismiss();
                                        Intent intent2 = new Intent(context, MainActivity.class);
                                        intent2.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                        context.startActivity(intent2);
                                        break;
                                    case 2:
                                        dialog.dismiss();
                                        break;

                                }
                            }
                        })
                        .setCancelable(true)
                        .setExpanded(false, 400)
                        .setMargin(0, 0, 0, -15)
                        .setInAnimation(R.anim.push_up_in_menu)
                        .setOutAnimation(R.anim.push_down_out_menu)
                        .create();
                dialog.show();
            }
        });
    }

    // 유저 이름 누를 때 리스너
    public void UserProfileClickListener(View userview,int position){
        final int position_f = position;
        userview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            Intent intent = new Intent(context, A2_UserProfileActivity.class);
            intent.putExtra("id", CSchedule_list.get(position_f).getUser_id());
            Log.d("conciseUserClick","id : " + CSchedule_list.get(position_f).getSchedule().getUploaded_user_id() + " name : " + CSchedule_list.get(position_f).getUsername());
            Activity activity = (Activity) context;
            activity.startActivity(intent);

            activity.overridePendingTransition(R.anim.abc_popup_enter, R.anim.abc_popup_exit);
            }
        });
    }

    // 받아보기 15명 누를 때 리스너
    public void WhoFollowsScheduleClickListener(View schedulefollow_user_clickablelayout,final int position){
        schedulefollow_user_clickablelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, A5_WhoFollowsScheduleActivity.class);
                intent.putExtra("url", String.valueOf("http://119.81.176.245/schedules/" + CSchedule_list.get(position).getSchedule().getId() + "/followers/"));       // 나중에 해결
                context.startActivity(intent);
            }
        });

    }

    // 좋아요 15명 누를 때 리스너
    public void WhoLikesScheduleClickListener(View schedulelike_user_clickablelayout,final int position){
        schedulelike_user_clickablelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("clicked schedule",CSchedule_list.get(position).getSchedule().getTitle());
                Log.d("clicked position",position+"");
                Intent intent = new Intent(context, A5_WhoFollowsScheduleActivity.class);
                intent.putExtra("url", String.valueOf("http://119.81.176.245/schedules/"+ CSchedule_list.get(position).getSchedule().getId()+"/like_users/"));       // 나중에 해결
                context.startActivity(intent);
            }
        });

    }


    // 좋아요 누를 때 리스너
    public void LikeButtonClickListener(ImageView likebutton,TextView like_count, int position){

        final int pos = position;
        final ImageView iv = likebutton;
        final TextView lcv = like_count;


        likebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(getItem(pos).getIsLike() == false){
                //    Toast toast1 = Toast.makeText(context, "Like Button Clicked", Toast.LENGTH_SHORT);
                //    toast1.show();
                    String url = "http://119.81.176.245/schedules/"+getItem(pos).getSchedule().getId()+"/like/";
                    HTTPRestfulUtilizerExtender a = new HTTPRestfulUtilizerExtender(context, url,"PUT");
                    a.doExecution();
                    getItem(pos).clickLike();
                    lcv.setText(String.valueOf(getItem(pos).getLike_count()));
                    lcv.startAnimation(AnimationUtils.loadAnimation(context, R.anim.grow_from_middle));
                    iv.setImageResource(R.drawable.like_on);
                    iv.getLayoutParams().height = dp25_in_pixels;
                    iv.getLayoutParams().width = dp25_in_pixels;

                    iv.startAnimation(AnimationUtils.loadAnimation(context,R.anim.grow_from_middle));
    //                notifyDataSetChanged();
                }
                else if(CSchedule_list.get(pos).getIsLike() == true){
                //    Toast toast2 = Toast.makeText(context, "Like Button Unclicked", Toast.LENGTH_SHORT);
                //    toast2.show();
                    String url = "http://119.81.176.245/schedules/"+CSchedule_list.get(pos).getId()+"/like/";
                    HTTPRestfulUtilizerExtender a = new HTTPRestfulUtilizerExtender(context, url, "PUT");
                    a.doExecution();
                    CSchedule_list.get(pos).clickLike();
                    lcv.setText(String.valueOf(CSchedule_list.get(pos).getLike_count()));
                    lcv.startAnimation(AnimationUtils.loadAnimation(context, R.anim.abc_fade_in));
                    iv.setImageResource(R.drawable.interest);

                    LinearLayout.LayoutParams imageViewParams = new LinearLayout.LayoutParams(
                            dp75_in_pixels,
                            dp25_in_pixels);


                    iv.setLayoutParams(imageViewParams);

                    iv.startAnimation(AnimationUtils.loadAnimation(context, R.anim.grow_from_middle));

                    //                  notifyDataSetChanged();
                }

            }
        });

    }

    // 받아보기 누를 때 리스너
    public void FollowButtonClickListener(ImageView followbutton, TextView follow_count, int position){

//        final int pos = position;

       // final int pos = followbutton.getTag()
        final ImageView iv = followbutton;
        final TextView fcv = follow_count;
        followbutton.setOnClickListener(new View.OnClickListener() {

            CalendarProviderUtil cpu = new CalendarProviderUtil(context);
            @Override
            public void onClick(View v) {
                int pos = Integer.valueOf(v.getTag().toString());
                if(CSchedule_list.get(pos).getIsFollow() == false){
                //    Toast toast1 = Toast.makeText(context, "Follow Button Clicked", Toast.LENGTH_SHORT);
                //    toast1.show();
                    String url = "http://119.81.176.245/schedules/"+CSchedule_list.get(pos).getId()+"/follow/";
                    HTTPRestfulUtilizerExtender a = new HTTPRestfulUtilizerExtender(context, url,"PUT");
                    a.doExecution();
                    CSchedule_list.get(pos).clickFollow();
                    fcv.setText(String.valueOf(CSchedule_list.get(pos).getFollow_count()));
                    fcv.startAnimation(AnimationUtils.loadAnimation(context, R.anim.grow_from_middle));

                    iv.setImageResource(R.drawable.export_to_calendar_onclick);
                    Log.d("follow_start_time", CSchedule_list.get(pos).getSchedule().getStarttime_ms() + "");
                    Log.d("follow_end_time", CSchedule_list.get(pos).getSchedule().getEndtime_ms() + "");
                    Log.d("follow_allday", CSchedule_list.get(pos).getSchedule().getAllday() + "");
                    cpu.addScheduleToInnerCalendar(CSchedule_list.get(pos));
                    iv.getLayoutParams().height = dp25_in_pixels;
                    iv.getLayoutParams().width = dp25_in_pixels;

                    LinearLayout.LayoutParams margin = new LinearLayout.LayoutParams(iv.getLayoutParams());
                    margin.leftMargin = 10;
                    iv.setLayoutParams(margin);

                    iv.startAnimation(AnimationUtils.loadAnimation(context, R.anim.grow_from_middle));


                    AppPrefs appPrefs = new AppPrefs(context);
                    if (appPrefs.getAlarm_setting())
                        Toast.makeText(context, "캘린더에 추가되었습니다\n"+appPrefs.getAlarm_time_string()+"으로 알람이 설정되었습니다",Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(context, "캘린더에 추가되었습니다", Toast.LENGTH_SHORT).show();
 //                   notifyDataSetChanged();
                }
                else if(CSchedule_list.get(pos).getIsFollow() == true){
                //    Toast toast2 = Toast.makeText(context, "Follow Button Unclicked", Toast.LENGTH_SHORT);
                //    toast2.show();
                    String url = "http://119.81.176.245/schedules/"+CSchedule_list.get(pos).getId()+"/follow/";
                    HTTPRestfulUtilizerExtender a = new HTTPRestfulUtilizerExtender(context, url,"PUT");
                    a.doExecution();
                    CSchedule_list.get(pos).clickFollow();
                    fcv.setText(String.valueOf(CSchedule_list.get(pos).getFollow_count()));
                    fcv.startAnimation(AnimationUtils.loadAnimation(context, R.anim.abc_fade_in));
                    iv.setImageResource(R.drawable.add_calendar);

                    LinearLayout.LayoutParams imageViewParams = new LinearLayout.LayoutParams(
                            dp75_in_pixels,
                            dp25_in_pixels);

                    iv.setLayoutParams(imageViewParams);

                    iv.startAnimation(AnimationUtils.loadAnimation(context, R.anim.grow_from_middle));

                    cpu.deleteScheduleFromInnerCalendar(CSchedule_list.get(pos));
                    Toast.makeText(context, "캘린더에서 제거되었습니다", Toast.LENGTH_SHORT).show();
  //                  notifyDataSetChanged();
                }

            }
        });

    }

    // 댓글달기 아이콘 누를 때 리스너
    public void WriteCommentClickListener(ImageView comment_button,int position){
        final int pos = position;

        comment_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, A6_WriteCommentActivity.class);
                intent.putExtra("id", CSchedule_list.get(pos).getId());

                Activity activity = (Activity) context;
                activity.startActivity(intent);

                activity.overridePendingTransition(R.anim.push_left_in, R.anim.abc_popup_exit);
   //             notifyDataSetChanged();
            }
        });

    }

    static class ViewHolder {
        ImageView memo_photo_vh;
        ImageView user_photo_vh;
        ImageView edit_vh;
        TextView user_fullname_vh;
        TextView title_vh;
        TextView date_vh;
        TextView time_vh;
        TextView memo_vh;
        TextView like_count_vh;
        TextView follow_count_vh;
        TextView location_vh;
        TextView comment_count_vh;
        ImageView like_button_vh;
        ImageView follow_button_vh;
        ImageView full_screen_vh;

        View user_clickableLayout_vh;
        View schedulefollow_user_clickablelayout_vh;
        View schedulelike_user_clickablelayout_vh;

        int position;
    }


    // 레이아웃에 데이터 적용
    public void AdjustDataToLayout(final View v, final int position, final ViewHolder holder){
        Log.d("position_adjust", position + "");

        if(CSchedule_list.get(position).getSchedule().isMaster() == true){
            holder.edit_vh.setVisibility(View.VISIBLE);
            holder.edit_vh.setClickable(true);

        }else {
            holder.edit_vh.setVisibility(View.INVISIBLE);
            holder.edit_vh.setClickable(false);
        }
        holder.user_fullname_vh.setText(CSchedule_list.get(position).getUsername());
        holder.title_vh.setText(CSchedule_list.get(position).getTitle());
        holder.date_vh.setText(CSchedule_list.get(position).getDate());
        if(CSchedule_list.get(position).getSchedule().getAllday()==false)
            holder.time_vh.setText(CSchedule_list.get(position).getTime());
        else
            holder.time_vh.setText("하루 종일");


        SpannableStringMaker ssm = new SpannableStringMaker(context,CSchedule_list.get(position).getMemo());
        holder.memo_vh.setText(ssm.getSs());

        holder.memo_vh.setMovementMethod(TextViewFixTouchConsume.LocalLinkMovementMethod.getInstance());
        holder.memo_vh.setHighlightColor(Color.TRANSPARENT);
        holder.memo_vh.setFocusable(false);


        holder.like_count_vh.setText(String.valueOf(CSchedule_list.get(position).getLike_count()));
        holder.follow_count_vh.setText(String.valueOf(CSchedule_list.get(position).getFollow_count()));
        holder.location_vh.setText(String.valueOf(CSchedule_list.get(position).getLocation()) == null ? "" : CSchedule_list.get(position).getLocation());
        holder.comment_count_vh.setText(String.valueOf(CSchedule_list.get(position).getComment_count()));
        Log.d("location_view", String.valueOf(CSchedule_list.get(position).getLocation()));
        Log.d("like", String.valueOf(CSchedule_list.get(position).getIsLike()));
        if(CSchedule_list.get(position).getIsLike() == true) {
            holder.like_button_vh.setImageResource(R.drawable.like_on);
            holder.like_button_vh.getLayoutParams().height = dp25_in_pixels;
            holder.like_button_vh.getLayoutParams().width = dp25_in_pixels;
        }
        else{
            holder.like_button_vh.setImageResource(R.drawable.interest);
            LinearLayout.LayoutParams imageViewParams = new LinearLayout.LayoutParams(
                    dp75_in_pixels,
                    dp25_in_pixels);

            holder.like_button_vh.setLayoutParams(imageViewParams);
        }


        if(CSchedule_list.get(position).getIsFollow() == true) {
            holder.follow_button_vh.setImageResource(R.drawable.export_to_calendar_onclick);
            holder.follow_button_vh.getLayoutParams().height = dp25_in_pixels;
            holder.follow_button_vh.getLayoutParams().width = dp25_in_pixels;

            LinearLayout.LayoutParams margin = new LinearLayout.LayoutParams(holder.follow_button_vh.getLayoutParams());
            margin.leftMargin = 10;
            holder.follow_button_vh.setLayoutParams(margin);
        }
        else {
            holder.follow_button_vh.setImageResource(R.drawable.add_calendar);
            LinearLayout.LayoutParams imageViewParams = new LinearLayout.LayoutParams(
                    dp75_in_pixels,
                    dp25_in_pixels);

            holder.follow_button_vh.setLayoutParams(imageViewParams);

        }


        if(CSchedule_list.get(position).getPhoto_dir_fromweb()!="") {


            // textview의 배경으로 그림을 넣기위한 원경의 사투 but 안쓰임

            /*
            com.squareup.picasso.Target target = new com.squareup.picasso.Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    Drawable drawable = new BitmapDrawable(context.getResources(), bitmap);
                    v.findViewById(R.id.memo).setBackground(drawable);

                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                }
            };
            */

          // Picasso.with(context).load(CSchedule_list.get(position).getPhoto_dir_fromweb()).into((ImageView)v.findViewById(R.id.memo_photo));
            Picasso.with(context).load(CSchedule_list.get(position).getPhoto_dir_fromweb()).into(holder.memo_photo_vh);

        }else{
            // 기본이미지 로드.
          //  memo_photo.setBackgroundColor(Color.BLACK);
          // holder.memo_photo_vh.setBackgroundColor(Color.BLACK);

            // 6개의 기본이미지
            int color = position%5;
            switch (color){
                case 0:
                    holder.memo_photo_vh.setImageResource(R.drawable.memo_photo_default6);
                    break;
                case 1:
                    holder.memo_photo_vh.setImageResource(R.drawable.memo_photo_default2);
                    break;
                case 2:
                    holder.memo_photo_vh.setImageResource(R.drawable.memo_photo_default3);
                    break;
                case 3:
                    holder.memo_photo_vh.setImageResource(R.drawable.memo_photo_default4);
                    break;
                case 4:
                    holder.memo_photo_vh.setImageResource(R.drawable.memo_photo_default5);
                    break;

            }

        }


        holder.edit_vh.setTag(position);
        holder.follow_button_vh.setTag(position);
        holder.memo_photo_vh.setTag(position);
        holder.time_vh.setTag(position);
        holder.like_button_vh.setTag(position);
        holder.user_fullname_vh.setTag(position);
        holder.schedulefollow_user_clickablelayout_vh.setTag(position);
        holder.schedulelike_user_clickablelayout_vh.setTag(position);
        holder.full_screen_vh.setTag(position);

        EditClieckListener(holder.edit_vh, position);
        UserProfileClickListener(holder.user_clickableLayout_vh, position);
        //UserProfileClickListener(comment_writer,position);
        LikeButtonClickListener(holder.like_button_vh, like_count, position);
        WriteCommentClickListener(comment_button, position);
        FollowButtonClickListener(holder.follow_button_vh, follow_count, position);
        WhoFollowsScheduleClickListener(holder.schedulefollow_user_clickablelayout_vh, position);
        WhoLikesScheduleClickListener(holder.schedulelike_user_clickablelayout_vh, position);


        holder.full_screen_vh.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (CSchedule_list.get(position).getPhoto_full_fromweb() != "") {


                    AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.AppCompatAlertDialogStyle2));
                    LayoutInflater factory = LayoutInflater.from(context);
                    final View view = factory.inflate(R.layout.d2_fullscreen_image, null);

                    DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();

                    float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
                    float dpWidth = displayMetrics.widthPixels / displayMetrics.density;

                    int targetwidthpx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, (int)(dpWidth*0.9), context.getResources().getDisplayMetrics());
                    int targetheightpx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, (int) (dpHeight * 0.9), context.getResources().getDisplayMetrics());

                    ImageView temp = (ImageView) view.findViewById(R.id.image);
                    temp.getLayoutParams().width = targetwidthpx;
                    temp.getLayoutParams().height = targetheightpx;
                    Picasso.with(context).load(CSchedule_list.get(position).getPhoto_full_fromweb()).fit().centerInside().into(temp);

                    builder.setView(view);
                    builder.show();
                }
            }
        });



        if(CSchedule_list.get(position).getUser_photo()!="") {
         //   Picasso.with(context).load(CSchedule_list.get(position).getUser_photo()).transform(new CircleTransform()).into((ImageView) v.findViewById(R.id.user_photo));
            Picasso.with(context).load(CSchedule_list.get(position).getUser_photo()).transform(new CircleTransform()).into(holder.user_photo_vh);

        }else{
            // 기본이미지 로드.
            //user_photo.setImageResource(R.drawable.userimage_default);
            holder.user_photo_vh.setImageResource(R.drawable.userimage_default);
            Log.d("default_image","default_image");
        }


/*
        PicassoImageToolExtender a = new PicassoImageToolExtender(context,

                CSchedule_list.get(position).getPhoto_dir_fromweb(),
                CSchedule_list.get(position).getPhoto_dir());
        a.doSomething();
        ((TextView)v.findViewById(R.id.memo)).setBackgroundResource(a.getGeneratedId());
*/
  //      notifyDataSetChanged();
    }


    class PicassoImageToolExtender extends PicassoImageTool{
        public PicassoImageToolExtender(Context context, String photo_dir_fromweb, String photo_dir){
            setmContext(context);
            setPhoto_dir_fromweb(photo_dir_fromweb);
            setPhoto_dir(photo_dir);
        }

    }

    class HTTPRestfulUtilizerExtender extends HTTPRestfulUtilizer {

        public HTTPRestfulUtilizerExtender(Context mContext, String url, String HTTPRestType) {
            setmContext(mContext);
            setUrl(url);
            setHTTPRestType(HTTPRestType);
            task = new HttpAsyncTaskExtenders();
            Log.d("HTTP Constructor url", url);
            // new HttpAsyncTask().execute(url,HTTPRestType);
        }

        @Override
        public void doExecution(){
            task.execute(getUrl(), getHTTPRestType());
        }
        class HttpAsyncTaskExtenders extends HTTPRestfulUtilizer.HttpAsyncTask{
            @Override
            protected String doInBackground(String... strings) {
                String url = strings[0];
                String sHTTPRestType = strings[1];
                setOutputString(PUT(url, getInputBundle()));

                return getOutputString();
            }
            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);

            }
        }
    }

    class HTTPRestfulUtilizerExtender_delete extends HTTPRestfulUtilizer {

        public HTTPRestfulUtilizerExtender_delete(Context mContext, String url, String HTTPRestType) {
            setmContext(mContext);
            setUrl(url);
            setHTTPRestType(HTTPRestType);
            task = new HttpAsyncTaskExtenders();
            Log.d("HTTP Constructor url", url);
            // new HttpAsyncTask().execute(url,HTTPRestType);
        }

        @Override
        public void doExecution(){
            task.execute(getUrl(), getHTTPRestType());
        }
        class HttpAsyncTaskExtenders extends HTTPRestfulUtilizer.HttpAsyncTask{
            @Override
            protected String doInBackground(String... strings) {
                String url = strings[0];
                String sHTTPRestType = strings[1];
                setOutputString(DELETE(url));

                return getOutputString();
            }
            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);

            }
        }
    }


}

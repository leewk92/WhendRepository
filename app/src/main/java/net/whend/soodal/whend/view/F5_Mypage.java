package net.whend.soodal.whend.view;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.whend.soodal.whend.R;
import net.whend.soodal.whend.model.base.User;
import net.whend.soodal.whend.util.AppPrefs;
import net.whend.soodal.whend.util.HTTPRestfulUtilizer;
import net.whend.soodal.whend.util.RoundedImage;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class F5_Mypage extends Fragment {
    // TODO: Rename parameter arguments, choose names that match

    private FragmentTabHost mTabHost;
    private int TAKE_FROM_CAMERA = 1;
    private int TAKE_FROM_GALLERY = 2;

    private Uri mImageCaptureUri;
    private String ImageAbsolutePath;


    TextView mainactivity_title;
    LinearLayout search_layout, setting_layout;
    ImageView search_btn, back_btn, setting_btn, user_photo;
    EditText search_text;
    JSONObject outputSchedulesJson;
    private View rootView;
    User u = new User();
    public F5_Mypage() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View.OnClickListener photo_add;
        ((MainActivity)getActivity()).getSupportActionBar().setDisplayUseLogoEnabled(false);

        rootView = inflater.inflate(R.layout.f5_mypage_layout, container, false);
        mainactivity_title = (TextView) getActivity().findViewById(R.id.mainactivity_title);
        mainactivity_title.setText("마이 페이지");

        search_layout = (LinearLayout) getActivity().findViewById(R.id.search_layout);
        setting_layout = (LinearLayout) getActivity().findViewById(R.id.setting_layout);

        search_btn = (ImageView) getActivity().findViewById(R.id.search_btn);
        search_text = (EditText) getActivity().findViewById(R.id.search_text);
        back_btn = (ImageView) getActivity().findViewById(R.id.back_btn);
        setting_btn = (ImageView) getActivity().findViewById(R.id.setting_btn);

        user_photo = (ImageView) rootView.findViewById(R.id.user_photo);

        search_layout.setVisibility(View.GONE);
        setting_layout.setVisibility(View.VISIBLE);
        back_btn.setVisibility(View.GONE);

        setting_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), A8_SettingActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
            }
        });

        photo_add = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final CharSequence[] items = {"카메라로 찍기", "갤러리에서 불러오기"};

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle);
                // 각 항목을 설정하고 클릭했을 때 동작을 지정함

                builder.setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        if (item == 0) { //카메라에서 찍기

                            // 카메라 호출
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT,
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString());

                            // 이미지 잘라내기 위한 크기


                            intent.putExtra("crop", "true");
                            intent.putExtra("aspectX", 1);
                            intent.putExtra("aspectY", 1);
                            intent.putExtra("outputX", 100);
                            intent.putExtra("outputY", 100);

                            try {
                                intent.putExtra("return-data", true);
                                startActivityForResult(Intent.createChooser(intent,
                                        "Complete action using"), TAKE_FROM_CAMERA);
                            } catch (ActivityNotFoundException e) {
                                // Do nothing for now
                            }

                        } else if (item == 1) { //갤러리에서 가져오기


                            Intent intent = new Intent();
                            // Gallery 호출
                            intent.setType("image/*");
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            // 잘라내기 셋팅

                            intent.putExtra("crop", "true");
                            intent.putExtra("aspectX", 1);
                            intent.putExtra("aspectY", 1);

                            intent.putExtra("outputX", 100);
                            intent.putExtra("outputY",  100);

                            try {
                                intent.putExtra("return-data", true);
                                startActivityForResult(Intent.createChooser(intent,
                                        "Complete action using"), TAKE_FROM_GALLERY);
                            } catch (ActivityNotFoundException e) {
                                // Do nothing for now
                            }
                        }

                    }
                });

                builder.show();

            }
        };

        user_photo.setOnClickListener(photo_add);

        mTabHost = (FragmentTabHost)rootView.findViewById(android.R.id.tabhost);
        mTabHost.setTop(120);
        mTabHost.setup(getActivity(), getChildFragmentManager(), R.id.realtabcontent);

        mTabHost.addTab(mTabHost.newTabSpec("fragmentb").setIndicator("MY"),
                F5_1_MyTimeline.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("fragmentc").setIndicator("관심"),
                F5_2_MyLikeSchedules.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("fragmentd").setIndicator("분석"),
                F5_3_Analysis.class, null);

        String url = "http://119.81.176.245/userinfos/";

        HTTPRestfulUtilizerExtender a = new HTTPRestfulUtilizerExtender(getActivity(),rootView,url,"GET");
        a.doExecution();

        View schedule_count_clickablelayout = rootView.findViewById(R.id.schedule_count_clickablelayout);
        View follower_count_clickablelayout = rootView.findViewById(R.id.follower_count_clickablelayout);
        View following_count_clickablelayout = rootView.findViewById(R.id.following_count_clickablelayout);
        ScheduleClickListener(schedule_count_clickablelayout);
        FollowerClickListener(follower_count_clickablelayout);
        FollowingClickListener(following_count_clickablelayout);
        return rootView;
    }
    // 게시물 누를때 리스너
    public void ScheduleClickListener(View schedule_count_clickablelayout){
        schedule_count_clickablelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), A10_ShowSchedulesActivity.class);
                intent.putExtra("id", u.getId());
                startActivity(intent);
            }
        });

    }
    // 팔로워 누를때 리스너
    public void FollowerClickListener(View follower_count_clickablelayout){
        follower_count_clickablelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), A5_WhoFollowsScheduleActivity.class);
                intent.putExtra("url", "http://119.81.176.245/userinfos/"+u.getId()+"/followers/");
                startActivity(intent);
            }
        });

    }
    // 팔로잉 누를때 리스너
    public void FollowingClickListener(View follower_count_clickablelayout){
        follower_count_clickablelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), A9_ShowFollowingObjectsActivity.class);
                intent.putExtra("id", u.getId());
                startActivity(intent);
            }
        });

    }


    class HTTPRestfulUtilizerExtender extends HTTPRestfulUtilizer {
        private View v;

        // Constructor for GET
        public HTTPRestfulUtilizerExtender(Context mContext,View rootView, String url, String HTTPRestType) {
            this.v = rootView;
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
                setOutputString(GET(url));

                return getOutputString();
            }
            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);

                try{
                    outputSchedulesJson = getOutputJsonObject();
                    JSONObject tmp_ith = outputSchedulesJson;


                    tmp_ith = outputSchedulesJson;
                    u.setId(tmp_ith.getInt("user_id"));
                    u.setUsername(tmp_ith.getString("user_name"));
                    u.setUser_photo(tmp_ith.getString("photo") == "null" ? "" : tmp_ith.getString("photo").substring(0, tmp_ith.getString("photo").length() - 4) + ".100x100.jpg");
                    u.setCount_following_user(tmp_ith.getInt("count_following_user"));
                    u.setCount_following_hashtag(tmp_ith.getInt("count_following_hashtag"));
                    u.setCount_follower(tmp_ith.getInt("count_follower"));
                    u.setCount_uploaded_schedule(tmp_ith.getInt("count_uploaded_schedule"));
/*                    JSONArray tmpjsonarray = tmp_ith.getJSONArray("following_user");
                    if(tmpjsonarray!=null) {
                        int[] following_user = new int[tmpjsonarray.length()];
                        // Extract numbers from JSON array.
                        for (int i = 0; i < tmpjsonarray.length(); ++i) {
                            following_user[i] = tmpjsonarray.optInt(i);
                            Log.d("following_user[i]",following_user[i]+"");
                        }
                        u.setFollowing_user(following_user);
                    }

                    tmpjsonarray = tmp_ith.getJSONArray("following_hashtag");

                    if(tmpjsonarray!=null) {
                        int[] following_hashtag = new int[tmpjsonarray.length()];
                        // Extract numbers from JSON array.
                        for (int i = 0; i < tmpjsonarray.length(); ++i) {
                            following_hashtag[i] = tmpjsonarray.optInt(i);
                        }
                        u.setFollowing_hashtag(following_hashtag);
                    }

                    tmpjsonarray = tmp_ith.getJSONArray("like_schedule");

                    if(tmpjsonarray!=null) {
                        int[] like_schedule = new int[tmpjsonarray.length()];
                        // Extract numbers from JSON array.
                        for (int i = 0; i < tmpjsonarray.length(); ++i) {
                            like_schedule[i] = tmpjsonarray.optInt(i);
                        }
                        u.setLike_schedule(like_schedule);
                    }

                    tmpjsonarray = tmp_ith.getJSONArray("following_schedule");
                    if(tmpjsonarray!=null) {
                        int[] following_schedule = new int[tmpjsonarray.length()];
                        // Extract numbers from JSON array.
                        for (int i = 0; i < tmpjsonarray.length(); ++i) {
                            following_schedule[i] = tmpjsonarray.optInt(i);
                        }
                        u.setFollowing_schedule(following_schedule);
                    }*/
                }catch(Exception e){

                }
                ((TextView)v.findViewById(R.id.username)).setText(u.getUsername());
                ((TextView)v.findViewById(R.id.follower_count)).setText(u.getCount_follower() + "");
                ((TextView)v.findViewById(R.id.schedule_count)).setText(u.getCount_uploaded_schedule() + "");
                ((TextView)v.findViewById(R.id.following_count)).setText(String.valueOf(u.getCount_following_hashtag() + u.getCount_following_user()));
                if(u.getUser_photo()!="") {
                    Picasso.with(getActivity()).load(u.getUser_photo()).into((ImageView) v.findViewById(R.id.user_photo));

                }else{
                    // 기본이미지 로드.
                    user_photo.setImageResource(R.drawable.userimage_default);
                }
            }
        }
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == TAKE_FROM_GALLERY) {
                Bundle extras = data.getExtras();
                mImageCaptureUri = data.getData(); // Get data from selected photo

                if (extras != null) {
                    Bitmap photo = extras.getParcelable("data");
                    RoundedImage temp = new RoundedImage(photo);
                    user_photo.setImageDrawable(temp);

                    ImageAbsolutePath = createImageFromBitmap(photo);
                    AppPrefs appPrefs = new AppPrefs(getActivity());
                    String url = "http://119.81.176.245/userinfos/"+appPrefs.getUser_id();
                    Bundle inputBundle = new Bundle();
//                    inputBundle.putIntegerArrayList("following_hashtag", u.getFollowing_hashtag_AL());
//                    inputBundle.putIntegerArrayList("following_schedule",u.getFollowing_schedule_AL());
//                    inputBundle.putIntegerArrayList("following_user", u.getFollowing_user_AL());
//                    inputBundle.putIntegerArrayList("like_schedule", u.getLike_schedule_AL());
                    HTTPRestfulUtilizerExtender2 a = new HTTPRestfulUtilizerExtender2(getActivity(),url,"PUT",inputBundle,ImageAbsolutePath);
                    a.doExecution();

                    //HTTPRestfulUtilizerExtender2 a = new HTTPRestfulUtilizerExtender2(getActivity(),rootView,"url","POST","ImageAbsolutePath);
                    //a.doExecution();

                }
            }else if (requestCode == TAKE_FROM_CAMERA) {
                Bundle extras2 = data.getExtras();
                if (extras2 != null) {
                    Bitmap photo = extras2.getParcelable("data");
                    RoundedImage temp = new RoundedImage(photo);
                    user_photo.setImageDrawable(temp);

                    ImageAbsolutePath = createImageFromBitmap(photo);


                }
            }
        }
    }

// 프로필 올리기
    class HTTPRestfulUtilizerExtender2 extends HTTPRestfulUtilizer {

        //Constructor
        HTTPRestfulUtilizerExtender2(Context mContext, String url, String HTTPRestType, Bundle inputBundle, String photo){
            this.setPhoto(photo);
            setInputBundle(inputBundle);
            setmContext(mContext);
            setUrl(url);
            setHTTPRestType(HTTPRestType);
            setPhoto(photo);

            task = new HttpAsyncTaskExtenders();
            Log.d("HTTP Constructor2 url", url);
        }

        public void doExecution(){
            task.execute(getUrl(), getHTTPRestType());
        }
        private class HttpAsyncTaskExtenders extends HTTPRestfulUtilizer.HttpAsyncTask {
            protected void onPreExecute() {
                super.onPreExecute();
            }

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

    public String createImageFromBitmap(Bitmap bmp) {

        long currentTime = 0;
        FileOutputStream fileOutputStream = null;

        try {

            // create a File object for the parent directory
            File wallpaperDirectory = new File(this.getActivity().getCacheDir().getPath());

            // have the object build the directory structure, if needed.
            wallpaperDirectory.mkdirs();

            //Capture is folder name and file name with date and time
            fileOutputStream = new FileOutputStream(String.format(
                    this.getActivity().getCacheDir().getPath()+"/whend%d.jpg",

                    currentTime = System.currentTimeMillis()));

            // Here we Resize the Image ...
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 100,
                    byteArrayOutputStream); // bm is the bitmap object
            byte[] bsResized = byteArrayOutputStream.toByteArray();


            try {
                fileOutputStream.write(bsResized);
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }

        return this.getActivity().getCacheDir().getPath()+"/whend"+ currentTime + ".jpg";
    }


    //
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}

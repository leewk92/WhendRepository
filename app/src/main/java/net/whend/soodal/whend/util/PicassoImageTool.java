package net.whend.soodal.whend.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import net.whend.soodal.whend.R;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by wonkyung on 15. 7. 19.
 */
public class PicassoImageTool {

    //	private ProgressDialog mDlg;
    private Context mContext;
    private String photo_dir_fromweb;
    private String photo_dir;
    private int generatedId;

    public int getGeneratedId() {
        return generatedId;
    }

    public void setGeneratedId(int generatedId) {
        this.generatedId = generatedId;
    }

    public PicassoImageTool(){

    }
    public PicassoImageTool(Context context, String photo_dir_fromweb, String photo_dir) {
        mContext = context;
        this.photo_dir_fromweb = photo_dir_fromweb;
        this.photo_dir = photo_dir;

    }

    public Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public void doSomething(){
        //String url = CSchedule_list.get(position).getPhoto_dir_fromweb();
        //Log.d("fileURL", url);
        //final String fileName = url.substring(url.toString().lastIndexOf('/'), url.toString().length());
        //Log.d("fileName", fileName);

        final String fileName = photo_dir_fromweb.substring(photo_dir_fromweb.toString().lastIndexOf('/'), photo_dir_fromweb.toString().length());
        Log.d("fileName", fileName);
        final String filePath = mContext.getCacheDir() + "/imgs"  + fileName;
        Log.d("filePath", filePath);

        if(photo_dir == ""){
            Log.d("ifif", "ifif");

            Target target = new Target() {
                @Override
                public void onPrepareLoad(Drawable arg0) {

                    return;
                }

                @Override
                public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom arg1) {


                    File file = null;
                    file = new File(filePath);

                    Log.d("filepath in bitmap", filePath);

                    try {
                        file.createNewFile();
                        FileOutputStream ostream = new FileOutputStream(file);

                        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, ostream);
                        ostream.close();
                    }catch(Exception e){

                    }

                }
                @Override
                public void onBitmapFailed(Drawable arg0) {
                    return;
                }
            };
            Log.d("photo_dir_fromweb",photo_dir_fromweb);
            Picasso.with(mContext).load(photo_dir_fromweb).into(target);

        }
        else {
            Log.d("else filepath", filePath);
        }

        try{
            generatedId = Picasso.with(mContext).load(new File(photo_dir)).get().getGenerationId();
        }catch (Exception e){
            e.printStackTrace();
        }


    }


    public String getPhoto_dir_fromweb() {
        return photo_dir_fromweb;
    }

    public void setPhoto_dir_fromweb(String photo_dir_fromweb) {
        this.photo_dir_fromweb = photo_dir_fromweb;
    }

    public String getPhoto_dir() {
        return photo_dir;
    }

    public void setPhoto_dir(String photo_dir) {
        this.photo_dir = photo_dir;
    }
}

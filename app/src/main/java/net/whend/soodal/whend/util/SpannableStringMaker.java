package net.whend.soodal.whend.util;

import android.content.Context;
import android.content.Intent;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import net.whend.soodal.whend.view.A7_SpecificHashTagActivity;
import net.whend.soodal.whend.view.setting.S2_Version;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2015-08-26.
 */
public class SpannableStringMaker {

    private String[] hashtags_title;
    private int[] hashtags_index;
    private SpannableString ss;
    private String input_string;
    private Context mContext;


    public SpannableStringMaker(Context mContext, String input_string){
        this.mContext = mContext;
        this.input_string = input_string;

        parseMemo(input_string);
        parseIndex(input_string, hashtags_title);

        ss = SpannableString.valueOf(input_string);

        for (int i=0; i<hashtags_title.length; i++) {
            int j=i*2;
            ss.setSpan(new hashtagSpan(mContext, hashtags_title[i]), hashtags_index[j], hashtags_index[j + 1]+1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

    }

    public SpannableString getSs() {
        return ss;
    }

    public void setSs(SpannableString ss) {
        this.ss = ss;
    }

    public void parseMemo(String memo_text) {
        memo_text = memo_text.replaceAll("\n", " ");
        memo_text = memo_text.replaceAll("#", " #");
        String tmpArray[] = memo_text.split("#");
        if (tmpArray != null) {

            hashtags_title = new String[tmpArray.length - 1];

            for (int i = 1; i < tmpArray.length; i++)
                hashtags_title[i - 1] = tmpArray[i].split(" ")[0];
        }
    }

    public void parseIndex(String text, String[] tags){
        hashtags_index = new int[tags.length*2];        //인덱스 배열의 짝수번째에는 시작 인덱스, 홀수번째는 끝 인덱스.
        for (int i=0; i<tags.length; i++){
            int j= i*2;
            hashtags_index[j] = text.indexOf("#"+tags[i]);
            hashtags_index[j+1] = hashtags_index[j] + tags[i].length();

        }
    }




}

package net.whend.soodal.whend.view.setting;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import net.whend.soodal.whend.R;
import net.whend.soodal.whend.util.HTTPRestfulUtilizer;
import net.whend.soodal.whend.util.MultiAutoCompleteTextViewSharp;
import net.whend.soodal.whend.util.SpannableStringMaker;
import net.whend.soodal.whend.util.hashtagSpan;
import net.whend.soodal.whend.view.A7_SpecificHashTagActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static net.whend.soodal.whend.util.SpannableStringMaker.*;

public class S7_Test extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_s7__test);

        TextView tv =(TextView)findViewById(R.id.test_tv);
        EditText et = (EditText)findViewById(R.id.test_et);

        MultiAutoCompleteTextViewSharp mactv = (MultiAutoCompleteTextViewSharp)findViewById(R.id.test_mtac);
        ArrayAdapter<String> aa2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line,
                new String[] {"연세대학교", "수달", "회의", "유포니아", "이원철하우스", "테스트", "테이프", "유글레나", "연세대" });

        mactv.setAdapter(aa2);

        mactv.setTokenizer(new SpaceTokenizer());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_s7__test, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    public class SpaceTokenizer implements MultiAutoCompleteTextView.Tokenizer {

        public int findTokenStart(CharSequence text, int cursor) {
            int i = cursor;

            while (i > 0 && text.charAt(i - 1) != ' ') {
                i--;
            }
            while (i < cursor && text.charAt(i) == ' ') {
                i++;
            }

            return i;
        }

        public int findTokenEnd(CharSequence text, int cursor) {
            int i = cursor;
            int len = text.length();

            while (i < len) {
                if (text.charAt(i) == ' ') {
                    return i;
                } else {
                    i++;
                }
            }

            return len;
        }

        public CharSequence terminateToken(CharSequence text) {
            int i = text.length();

            while (i > 0 && text.charAt(i - 1) == ' ') {
                i--;
            }

            if (i > 0 && text.charAt(i - 1) == ' ') {
                return text;
            } else {
                    SpannableString sp = new SpannableString("#"+text + " ");
                    sp.setSpan(new ForegroundColorSpan(Color.parseColor("#0288D1")), 0, text.length()+2,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    return sp;
            }
        }
    }



}

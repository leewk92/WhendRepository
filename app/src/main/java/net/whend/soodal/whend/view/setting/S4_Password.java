package net.whend.soodal.whend.view.setting;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import net.whend.soodal.whend.R;
import net.whend.soodal.whend.util.AppPrefs;
import net.whend.soodal.whend.util.HTTPRestfulUtilizer;

public class S4_Password extends AppCompatActivity {

    private EditText password1, password2;
    private ImageView password1_check, password2_check;
    private Button change_button;

    private Context mContext;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.s4_password);

        mContext = getApplicationContext();

        // password1과 password2의 동일 체크
        password1 = (EditText) findViewById(R.id.change_password);
        password2 = (EditText) findViewById(R.id.change_password2);

        password1_check = (ImageView) findViewById(R.id.password_check);
        password2_check = (ImageView) findViewById(R.id.password2_check);

        change_button = (Button) findViewById(R.id.change_button);

        dialog = new ProgressDialog(this);

        password1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(password1.getText().toString().length() >= 6)
                    password1_check.setImageDrawable(getResources().getDrawable(R.drawable.ic_done_white_24dp));
                else
                    password1_check.setImageDrawable(null);
            }
        });
        password2.addTextChangedListener(new TextWatcher(){

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(password1.getText().toString().equals(password2.getText().toString()))
                    password2_check.setImageDrawable(getResources().getDrawable(R.drawable.ic_done_white_24dp));
                else
                    password2_check.setImageDrawable(null);
            }
        });


        change_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(password1.getText().length() >= 6 && password1.getText().toString().equals(password2.getText().toString())){
                    String url = "http://119.81.176.245/rest-auth/password/change/";
                    Bundle password = new Bundle();

                    password.putCharSequence("new_password1",password1.getText());
                    password.putCharSequence("new_password2",password2.getText());

                    HTTPRestfulUtilizerExtender a = new HTTPRestfulUtilizerExtender(mContext, url,"POST",password);
                    a.doExecution();
                }

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_s4__password, menu);
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

    private class HTTPRestfulUtilizerExtender extends HTTPRestfulUtilizer {

        public HTTPRestfulUtilizerExtender(Context mContext, String url, String HTTPRestType, Bundle inputBundle) {
            setmContext(mContext);
            setUrl(url);
            setHTTPRestType(HTTPRestType);
            setInputBundle(inputBundle);
            task = new HttpAsyncTaskExtenders();
            Log.d("HTTP Constructor url", url);
            // new HttpAsyncTask().execute(url,HTTPRestType);
        }

        public void doExecution() {
            task.execute(getUrl(), getHTTPRestType());
        }

        private class HttpAsyncTaskExtenders extends HttpAsyncTask {

            protected void onPreExecute() {

                super.onPreExecute();


                if (dialog.isShowing() == false)
                    dialog = ProgressDialog.show(S4_Password.this, "",
                            "변경 중입니다.", true);
            }

            @Override
            protected String doInBackground(String... strings) {
                String url = strings[0];
                String sHTTPRestType = strings[1];
                setOutputString(POST(url, getInputBundle()));
                return getOutputString();

            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                //              result_view.setText(result);

                AppPrefs appprefs = new AppPrefs(mContext);
                appprefs.setPassword(password2.getText().toString());

                if (dialog.isShowing() == true)
                    dialog.dismiss();

                finish();
                overridePendingTransition(R.anim.abc_fade_out,R.anim.abc_fade_in);
            }

        }
    }
}

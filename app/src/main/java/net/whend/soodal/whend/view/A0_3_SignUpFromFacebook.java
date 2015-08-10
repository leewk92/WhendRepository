package net.whend.soodal.whend.view;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import net.whend.soodal.whend.R;

public class A0_3_SignUpFromFacebook extends AppCompatActivity {

    private String data;
    private String fb_id;
    private String fb_name;
    private String fb_picture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a0_3_sign_up_from_facebook);

        Intent intent = new Intent(this.getIntent());
        intent.getStringExtra("data");
        intent.getStringExtra("fb_id");
        intent.getStringExtra("fb_name");
        intent.getStringExtra("fb_picture");





    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_a0_3__sign_up_from_facebook, menu);
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
}

package ca.mhowatnait.lab1_oscarreviews;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class OscarSendActivity extends AppCompatActivity implements View.OnClickListener, SharedPreferences.OnSharedPreferenceChangeListener {
    SharedPreferences settings;
    View mainView;


//    OVERRIDE METHODS

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oscar_send);
        settings = PreferenceManager.getDefaultSharedPreferences(this);

        mainView = findViewById(R.id.layout_send_activity);
        String bgColor = settings.getString("main_bg_color_list", "#FFFFFF" );

        mainView.setBackgroundColor(Color.parseColor(bgColor));
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        String bgColor = settings.getString("main_bg_color_list", "#FFFFFF" );
        mainView.setBackgroundColor(Color.parseColor(bgColor));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);


        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.menu_item_send_review:
            {
                Intent intent = new Intent(this, OscarSendActivity.class);
                this.startActivity(intent);
                break;
            }
            case R.id.menu_item_preferences:
            {
                Intent intent = new Intent(this, PrefsActivity.class);
                this.startActivity(intent);
                break;
            }

        }
        return true;
    }

    @Override
    public void onClick(View view) {


    }

//    METHODS

    private void postToChatter()
    {
        EditText editTextReview = (EditText)findViewById(R.id.edit_text_review);
        EditText editTextNominee = (EditText)findViewById(R.id.edit_text_nominee);
        String chatReview = editTextReview.getText().toString();
        String chatNominee = editTextNominee.getText().toString();
        String userName = settings.getString("reviewer_name", "Harvey Weinstein");
        String password = settings.getString("user_password", "none");
        try
        {
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost("http://www.youcode.ca/Lab01Servlet");
            List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
            postParameters.add(new BasicNameValuePair("DATA", chat));
            postParameters.add(new BasicNameValuePair("LOGIN_NAME", userName));
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postParameters);
            post.setEntity(formEntity);
            client.execute(post);

        }
        catch(Exception e)
        {
            Toast.makeText(this, "Error:" + e, Toast.LENGTH_LONG).show();
        }
        edittext.getText().clear();
    }



}

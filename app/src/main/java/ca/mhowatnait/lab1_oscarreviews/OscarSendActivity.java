package ca.mhowatnait.lab1_oscarreviews;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class OscarSendActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    SharedPreferences settings;
    View mainView;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private Button sendButton;


//    OVERRIDE METHODS

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oscar_send);
        addListenerOnButton();

        settings = PreferenceManager.getDefaultSharedPreferences(this);
        settings.registerOnSharedPreferenceChangeListener(this);


        mainView = findViewById(R.id.layout_send_activity);
        String bgColor = settings.getString("main_bg_color_list", "#FFFFFF" );
        mainView.setBackgroundColor(Color.parseColor(bgColor));





        if(Build.VERSION.SDK_INT > 9){
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
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
            case R.id.menu_item_list_review:
            {
                Intent intent = new Intent(this, OscarCustomListActivity.class);
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

    private void addListenerOnButton() {
        radioGroup = (RadioGroup) findViewById(R.id.radio_group_category_choice);
        sendButton = (Button) findViewById(R.id.oscar_send_button);
        sendButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                int selectedID = radioGroup.getCheckedRadioButtonId();
                radioButton = (RadioButton) findViewById(selectedID);
                postToChatter();
                }
            });
    }

//    METHODS


    public String categoryConversion(){
        String radioStringReturn = "";
        switch(radioButton.getId()) {
            case R.id.radio_button_best_picture:
                {
                    radioStringReturn = "picture" ;
                    break;
                }
            case R.id.radio_button_best_actor:
                {
                    radioStringReturn = "actor";
                    break;
                }
            case R.id.radio_button_best_actress:
                {
                    radioStringReturn = "actress";
                    break;
                }
            case R.id.radio_button_film_editing:
                {
                    radioStringReturn = "editing";
                    break;
                }
            case R.id.radio_button_visual_effects:;
                {
                    radioStringReturn = "effects";
                    break;
                }
        }
        return radioStringReturn;
    }


    private void postToChatter()
    {
        EditText editTextReview = (EditText)findViewById(R.id.edit_text_review);
        EditText editTextNominee = (EditText)findViewById(R.id.edit_text_nominee);
        String chatReview = editTextReview.getText().toString();
        String chatNominee = editTextNominee.getText().toString();
        String category = categoryConversion();
        String userName = settings.getString("reviewer_name", "Matthew");
        String password = settings.getString("user_password", "*****");
        String webServer = settings.getString("web_server_address", "http://www.youcode.ca/Lab01Servlet");
        try
        {
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(webServer);
            List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
            postParameters.add(new BasicNameValuePair("REVIEW", chatReview));
            postParameters.add(new BasicNameValuePair("REVIEWER", userName));
            postParameters.add(new BasicNameValuePair("NOMINEE", chatNominee));
            postParameters.add(new BasicNameValuePair("CATEGORY", category));
            postParameters.add(new BasicNameValuePair("PASSWORD", password));
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postParameters);
            post.setEntity(formEntity);
            client.execute(post);

        }
        catch(Exception e)
        {
            Toast.makeText(this, "Error:" + e, Toast.LENGTH_LONG).show();
        }
        editTextReview.getText().clear();
        editTextNominee.getText().clear();
    }

}

package ca.mhowatnait.lab1_oscarreviews;


import android.app.ListActivity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;

public class OscarCustomListActivity extends ListActivity implements AdapterView.OnItemSelectedListener, SharedPreferences.OnSharedPreferenceChangeListener {

    static String categorySelected;
    ArrayList<HashMap<String, String>> chats = new ArrayList<HashMap<String, String>>();
    SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oscar_custom_list);
        Spinner categorySpinner = (Spinner) findViewById(R.id.category_spinner);
        settings = PreferenceManager.getDefaultSharedPreferences(this);
        settings.registerOnSharedPreferenceChangeListener(this);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.category_names, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);
        categorySpinner.setOnItemSelectedListener(this);

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        String[] categoryValue = getResources().getStringArray(R.array.category_values);
            categorySelected = (categoryValue[pos]);
            if (pos == 0){
                displayOscar();// film, actor, actress etc.
            }
            else{
                categorySelected = "?CATEGORY=" + categorySelected;
                displayOscar();
            }

    }


    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }




    private void displayOscar() {
        String[] fields = new String[]{"REVIEWER", "DATE", "CATEGORY", "NOMINEE", "REVIEW"};
        int [] ids = new int[]{R.id.custom_list_cell_reviewer, R.id.custom_list_cell_date_time,
                R.id.custom_list_cell_category, R.id.custom_list_cell_nominee, R.id.custom_list_cell_review};
        SimpleAdapter adapter = new SimpleAdapter(this, chats, R.layout.activity_oscar_list_cell, fields, ids);
        populateList();
        setListAdapter(adapter);
    }

    private void populateList() {
        chats.clear();
        BufferedReader in;
        try
        {
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet();
            request.setURI(new URI( settings.getString("web_server_address", "http://www.youcode.ca/Lab01Servlet") + categorySelected));
            HttpResponse response = client.execute(request);
            in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            String line;
            while((line = in.readLine()) != null)
            {
                HashMap<String,String> temp = new HashMap<String, String>();

                line = getString(R.string.view_list_date) + line;
                temp.put("DATE", line);
                line = in.readLine();
                line = getString(R.string.view_list_reviewer) + line;
                temp.put("REVIEWER", line);
                line = in.readLine();
                line = categoryConversion(line);
                line = getString(R.string.view_list_category) + line;
                temp.put("CATEGORY", line);
                line = in.readLine();
                line = getString(R.string.view_list_nominee) + line;
                temp.put("NOMINEE", line);
                line = in.readLine();
                line = getString(R.string.view_list_review)  + line;
                temp.put("REVIEW", line);
                chats.add(temp);

            }
            in.close();

        }
        catch (Exception e)
        {
            Toast.makeText(this, "Error:" + e, Toast.LENGTH_LONG).show();
        }
    }

    public String categoryConversion(String inputText){
        String radioStringReturn = "";
        switch(inputText) {
            case "film":
            {
                radioStringReturn ="Best Picture";
                break;
            }
            case "actor":
            {
                radioStringReturn = "Best Actor";
                break;
            }
            case "actress":
            {
                radioStringReturn = "Best Actress";
                break;
            }
            case "editing":
            {
                radioStringReturn = "Film Editing";
                break;
            }
            case "effects":
            {
                radioStringReturn = "Visual Effects";
                break;
            }
        }
        return radioStringReturn;
    }



}

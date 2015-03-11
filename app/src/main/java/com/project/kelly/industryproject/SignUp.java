package com.project.kelly.industryproject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class SignUp extends ActionBarActivity {
    Activity activity = this;
    private static final String TAG = "checking" ;
    private static final String newURL = "http://xxx/addUser.php";
    String name;
    String password;
    String email;
    String result = null;
    InputStream is = null;
    HttpResponse response;
    int code;
    private Button registerButton;
    private EditText userNameEditText, userPasswordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText e_name = (EditText) findViewById(R.id.UserName_edit_text);
        final EditText e_pword = (EditText) findViewById(R.id.Password_edit_text);
        final EditText e_email = (EditText) findViewById(R.id.Email_edit_text);
        registerButton = (Button) findViewById(R.id.register_btn);

        // onclick send new user info to db
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name = e_name.getText().toString();
                password = e_pword.getText().toString();
                email = e_email.getText().toString();

                Connection c = new Connection();
                c.execute();

            }
        });
    }
    public class Connection extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] params) {
            insert();

            return null;
        }
    }
    private void insert() {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(newURL);
        List<BasicNameValuePair> users = new ArrayList<BasicNameValuePair>();

        users.add(new BasicNameValuePair("username", name));
        users.add(new BasicNameValuePair("user_email", email));
        users.add(new BasicNameValuePair("user_password", password));

        BufferedReader reader = null;
        StringBuilder sb = new StringBuilder();

        try {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(users);
            httppost.setEntity(entity);
            HttpResponse response = httpclient.execute(httppost);

            /*String line = "";
            String lineSep = System.getProperty("line.separator");
            while ((line = reader.readLine()) != null) {
                Log.w("Line: ", line);
                    sb.append(line + lineSep);
                }*/
            result = EntityUtils.toString(response.getEntity());
            // only read in the json string to get result
            result = result.substring(22);
            //result = sb.toString();
            Log.e("Success 2", result);
            // Log.w("final result string reads: ", result);
        } catch (Exception e) {

            Log.e("Fail 2", e.toString());

        }
        // is this side of things necessary?
        // it's for the toast to say success
        try {
            // need to parse result to a json object??
            JSONObject json_data = new JSONObject(result);
            code = (json_data.getInt("code"));

            activity.runOnUiThread(new Runnable() {
                public void run() {
                    if (code == 1) {
                        Toast.makeText(getBaseContext(), "Inserted Successfully",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getBaseContext(), "Sorry, Try Again",
                                Toast.LENGTH_LONG).show();
                    }
                }
            });
        } catch (Exception e) {
            Log.e("Fail 3 in json object side", e.toString());
            e.printStackTrace();
        }
    }
    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item){
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
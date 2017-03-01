package datacollection.iitd.mavi.datacollectionmavi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class helper extends AppCompatActivity {

    Boolean isLoggedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_helper);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE    ); // 0 - for private mode
       // SharedPreferences.Editor editor = pref.edit();
//        editor.putBoolean("key_name", true); // Storing boolean - true/false
//        editor.putString("key_name", "string value"); // Storing string
            isLoggedIn=pref.getBoolean("isLoggedIn", false);
        Intent intent;

        if(isLoggedIn)
        {
                intent=new Intent(this,DataCollectActivity.class);
        }
        else
        {
                intent= new Intent(this,LoginActivity.class);
        }
        startActivity(intent);
        finish();




    }
}

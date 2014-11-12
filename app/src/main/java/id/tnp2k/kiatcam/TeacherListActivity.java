package id.tnp2k.kiatcam;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.*;

import java.util.ArrayList;


public class TeacherListActivity extends Activity {
    ListView listView ;
    String currentTeacher;
    private static final int TAKE_PHOTO_REQ = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_list);

        // Get ListView object from xml
        listView = (ListView) findViewById(R.id.listView2);

        // Defined Array values to show in ListView
        String[] values = new String[] {
                "Ramda Yanurzha",
                "Prima Setiawan",
                "Hendri Yulius",
                "Kurniawati",
                "Jan Priebe",
                "Dewi Susanti",
                "Deny Sambodo",
                "Sharon Kanthy",

        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);
        // Assign adapter to ListView
        listView.setAdapter(adapter);

        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition     = position;

                // ListView Clicked item value
                String  itemValue    = (String) listView.getItemAtPosition(position);
                currentTeacher = itemValue;
                TakePhoto(view);
                /*
                // Show Alert
                Toast.makeText(getApplicationContext(),
                        "Position :" + itemPosition + "  ListItem : " + itemValue, Toast.LENGTH_LONG)
                        .show();*/
            }

        });
    }


    public void TakePhoto(View view) {
        Intent intent = new Intent(this, CameraActivity.class);
        Bundle b  = new Bundle();
        b.putString("current",currentTeacher);
        intent.putExtras(b);
        startActivityForResult(intent,TAKE_PHOTO_REQ);
        // Do something in response to button
    }

}
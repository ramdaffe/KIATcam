package id.tnp2k.kiatcam;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.util.*;

import java.util.ArrayList;


public class TeacherListActivity extends Activity implements LoaderManager.LoaderCallbacks<Cursor> {
    ListView listView ;
    String currentTeacher;
    private static final int TAKE_PHOTO_REQ = 100;
    SimpleCursorAdapter mAdapter;

    // These are the Contacts rows that we will retrieve
    static final String[] PROJECTION = new String[] {ContactsContract.Data._ID,
            ContactsContract.Data.DISPLAY_NAME};

    // This is the select criteria
    static final String SELECTION = "((" +
            ContactsContract.Data.DISPLAY_NAME + " NOTNULL) AND (" +
            ContactsContract.Data.DISPLAY_NAME + " != '' ))";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
        ProgressBar progressBar = new ProgressBar(this);
        progressBar.setIndeterminate(true);
        getgetListView().setEmptyView(progressBar);
        String[] fromColumns = {ContactsContract.Data.DISPLAY_NAME};
        int[] toViews = {android.R.id.text1}; // The TextView in simple_list_item_1

        // Create an empty adapter we will use to display the loaded data.
        // We pass null for the cursor, then update it in onLoadFinished()
        mAdapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_1, null,
                fromColumns, toViews, 0);*/
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
                String  itemValue    = (String) listView.getItemAtPosition(position);
                currentTeacher = itemValue;
                TakePhoto(view);
            }

        });

    }




    public void TakePhoto(View view) {
        Intent intent = new Intent(this, CameraActivity.class);
        Bundle b  = new Bundle();
        b.putString("current",currentTeacher);
        if (intent.getExtras() == null) {
            intent.putExtras(b);
        } else {
            intent.replaceExtras(b);
        }

        startActivityForResult(intent,TAKE_PHOTO_REQ);
        // Do something in response to button
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);

    }
}
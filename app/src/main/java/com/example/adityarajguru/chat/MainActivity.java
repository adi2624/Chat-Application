package com.example.adityarajguru.chat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
    @Override public boolean onCreateOptionsMenu(Menu menu) { MenuInflater menuInflater = getMenuInflater(); menuInflater.inflate(R.menu.search_menu, menu);
        return true;
    }
    @Override public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search_icon:
                Log.e("Search requested","fd");
                onSearchRequested();
                return true;
            default:
                return super.onOptionsItemSelected(item); }
    }
}

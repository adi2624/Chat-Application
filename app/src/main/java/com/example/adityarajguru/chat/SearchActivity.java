package com.example.adityarajguru.chat;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private ListView lv;
    DatabaseReference users;
    List<String> usernames = new ArrayList<>();
    List<String> results = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        lv = (ListView)findViewById(R.id.search_list);
        Intent intent = getIntent();
        if(Intent.ACTION_SEARCH.equals(intent.getAction())){
            final String query = intent.getStringExtra(SearchManager.QUERY);
           // Toast.makeText(SearchActivity.this, query,Toast.LENGTH_SHORT).show();
            users = FirebaseDatabase.getInstance().getReference("username");
            ValueEventListener valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot ds : dataSnapshot.getChildren())
                    {

                            usernames.add(ds.child("email").getValue().toString());
                            Log.e("VALUE: ",ds.child("email").getValue().toString());

                    }
                    for(int i=0;i<usernames.size();i++)
                    {
                        if(usernames.get(i).equals(query))
                        {
                            results.add(usernames.get(i));
                            Log.e("MATCH: ",usernames.get(i));
                        }
                    }
                    lv.setAdapter(new ArrayAdapter<String>(SearchActivity.this,
                            android.R.layout.simple_list_item_1,results));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };
            users.addListenerForSingleValueEvent(valueEventListener);

        }
    }
}

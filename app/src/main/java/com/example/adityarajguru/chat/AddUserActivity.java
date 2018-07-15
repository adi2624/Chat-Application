package com.example.adityarajguru.chat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
import java.util.ArrayList;

public class AddUserActivity extends AppCompatActivity {


    private final List <String> list = new ArrayList<>();
    String listArray[] = {};
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        lv = (ListView)findViewById(R.id.userslist);
        ListUsers();

    }
    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }
    private void ListUsers()
    {
        DatabaseReference dataref = FirebaseDatabase.getInstance().getReference();
        DatabaseReference searchref = dataref.child("username");
        final String[] selected = new String[1];
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //List<String> list = new ArrayList<>();
                for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                    for(DataSnapshot email: ds.getChildren()){
                        list.add(email.getValue().toString());
                    }
                }
                lv.setAdapter(new ArrayAdapter<String>(AddUserActivity.this,
                        android.R.layout.simple_list_item_1,list));
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    selected[0] = (String)adapterView.getItemAtPosition(position);
                    //Toast.makeText(getApplicationContext(),"Selected : "+ selected[0],Toast.LENGTH_LONG).show();
                         Intent intent = new Intent(AddUserActivity.this,ChatActivity.class);
                        intent.putExtra("Email", selected[0]);
                        startActivity(intent);


                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        searchref.addListenerForSingleValueEvent(valueEventListener);


    }

}

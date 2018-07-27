package com.example.adityarajguru.chat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OldChatsActivity extends AppCompatActivity {

    class thread
    {
        public String last_msg;
        public String receiver;
        public String rating;


    }


    private ListView lv;
    private ArrayList<String> chats = new ArrayList<>();
    private ArrayList<String> ratings = new ArrayList<>();
    public DatabaseReference threads_reference;
    public DatabaseReference messages_reference;
    public DatabaseReference ratings_reference;
    public List<thread> msg_list;
    public String temp_msg;
    public String temp_receiver;
    public String temp_rating;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_old_chats);
        messages_reference=FirebaseDatabase.getInstance().getReference().child("messages");
        threads_reference=FirebaseDatabase.getInstance().getReference().child("threads");
        ratings_reference=FirebaseDatabase.getInstance().getReference().child("messages");
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                dataSnapshot.getKey();
                Log.e("UID:",dataSnapshot.getKey());
                attach_msg_listener(dataSnapshot.getKey());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        messages_reference.addChildEventListener(childEventListener);


        lv=findViewById(R.id.list);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OldChatsActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });


    }
    private void indirect_sort()
    {
        Log.e("RATINGS_ARRAY",ratings.toString());
        Log.e("MSGS_ARRAY",chats.toString());
    }
    private void attach_msg_listener(String uid)
    {


        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        Log.e("DataSnapshotChildren",dataSnapshot.getChildrenCount()+"") ;
                        temp_msg=dataSnapshot.getValue().toString();
                        Log.e("ADDED",dataSnapshot.getValue().toString());

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                temp_receiver=dataSnapshot.getValue().toString();
                Log.e("RECEIVER",dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        ChildEventListener ratings_listener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                temp_rating=dataSnapshot.getValue().toString();
                //Log.e("RATING", dataSnapshot.getValue().toString());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        messages_reference.child(uid).child("msg").addChildEventListener(childEventListener);
        threads_reference.child(uid).child("members").child("receiver").addListenerForSingleValueEvent(valueEventListener);
        messages_reference.child(uid).child("rating").addChildEventListener(ratings_listener);




    }
    private void rating_listener() {

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ratings.add(dataSnapshot.getValue().toString());
                Log.e("RATINGS", ratings.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };


        //ratings_reference.addListenerForSingleValueEvent(valueEventListener);
    }


}

package com.example.adityarajguru.chat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

    private ListView lv;
    private DatabaseReference threads_reference;
    private DatabaseReference messages_reference;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private ArrayList<MessageThread> emails = new ArrayList<MessageThread>();
    private String temp_string;
    private String last_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_old_chats);
        lv=findViewById(R.id.list);
        threads_reference=FirebaseDatabase.getInstance().getReference("threads");

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot uid: dataSnapshot.getChildren()) {
                    Log.e("KEY", uid.getKey());
                    messages_reference=FirebaseDatabase.getInstance().getReference("messages").child(uid.getKey()).child("msg");
                    final MessageThread messageThread = new MessageThread();
                    if(uid.child("members").child("sender").getValue().toString().equals(user.getEmail())){
                        Log.e("RECEIVER EMAIL:",uid.child("members").child("receiver").getValue().toString());
                        messageThread.setReceiver(uid.child("members").child("receiver").getValue().toString());
                       messages_reference.addListenerForSingleValueEvent(new ValueEventListener() {
                           @Override
                           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                               for(DataSnapshot msg: dataSnapshot.getChildren()) {
                                   last_message=msg.getValue().toString();

                               }
                               Log.e("MESSAGE: ",last_message);
                               messageThread.setMessage(last_message);
                               messageThread.setCount("0");
                               emails.add(messageThread);
                               lv.setAdapter(new CustomListAdapter(OldChatsActivity.this, emails));
                           }

                           @Override
                           public void onCancelled(@NonNull DatabaseError databaseError) {

                           }
                       });

                       // emails.add(uid.child("members").child("receiver").getValue().toString());
                    }
                    else if(uid.child("members").child("receiver").getValue().toString().equals(user.getEmail()))
                    {
                        Log.e("SENDER EMAIL:",uid.child("members").child("sender").getValue().toString());
                        messageThread.setReceiver(uid.child("members").child("sender").getValue().toString());
                        messages_reference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for(DataSnapshot msg: dataSnapshot.getChildren()) {
                                    last_message=msg.getValue().toString();

                                }
                                Log.e("MESSAGE: ",last_message);
                                messageThread.setMessage(last_message);
                                messageThread.setCount("0");
                                emails.add(messageThread);
                                lv.setAdapter(new CustomListAdapter(OldChatsActivity.this, emails));
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                       ;
                       // emails.add(uid.child("members").child("sender").getValue().toString());
                    }
                }

                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        Object o =  lv.getItemAtPosition(position);
                        MessageThread messageData = (MessageThread) o;
                        //Toast.makeText(getApplicationContext(),"Selected : "+ selected[0],Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(OldChatsActivity.this, ChatActivity.class);
                        intent.putExtra("Email", messageData.getReceiver());
                        startActivity(intent);


                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        threads_reference.addListenerForSingleValueEvent(valueEventListener);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OldChatsActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });


    }




}

package com.example.adityarajguru.chat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ChatActivity extends AppCompatActivity {

    public DatabaseReference ref3;
    public DatabaseReference ref4;
    public DatabaseReference ref2;
    public DatabaseReference listen;
    String id="";
    Button send;
    int i=0;
    private List<String> list = new ArrayList<>();
    ListView lv;
    TextInputEditText text_object;
    LinearLayout layout;
    String current_user;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        current_user="";
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null)
        {
            current_user = user.getEmail();
        }
        Intent myIntent = getIntent();
        email = myIntent.getStringExtra("Email");
        ref2 = FirebaseDatabase.getInstance().getReference();
        ref3 = FirebaseDatabase.getInstance().getReference().child("threads");
        ref4 = FirebaseDatabase.getInstance().getReference().child("messages");
        ref3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren())
                {
                    Log.e("KEY RECEIVER : ",ds.child("members").child("receiver").getValue().toString());
                    Log.e("KEY SENDER : ",ds.child("members").child("sender").getValue().toString());
                    if(email.equals(ds.child("members").child("receiver").getValue().toString()) && current_user.equals(ds.child("members").child("sender").getValue().toString()))
                    {
                        id = ds.getKey();
                        break;
                    }
                    else if(email.equals(ds.child("members").child("sender").getValue().toString()) && current_user.equals(ds.child("members").child("receiver").getValue().toString()))
                    {
                        id=ds.getKey();
                        break;
                    }

                }
                if(id=="")
                {
                    Random r = new Random();
                    int Low=1080;
                    int High=1920;
                    int Result = r.nextInt(High-Low)+Low;
                    id = Result+"";

                }
                listen = ref4.child(id).child("msg");
                updateUI();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        lv =(ListView)findViewById(R.id.list);
   //     Toast.makeText(getApplicationContext(),"Selected : "+ email,Toast.LENGTH_LONG).show();
        send=(Button)findViewById(R.id.button);
        text_object = (TextInputEditText)findViewById(R.id.text);
        // Use this : ref3.addChildEventListener()
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String messagetext =text_object.getText().toString();
                Toast.makeText(getApplicationContext(),"Sent : "+ messagetext,Toast.LENGTH_LONG).show();
                Log.e("ID :",id+"");
                CreateInstance(email,messagetext,id);


            }
        });



    }

    private void CreateInstance(String email,String messagetext,String id)
    {
        Log.e("ON CREATE WITH ID :",id+"");

        ref3.child(id).child("members").child("receiver").setValue(email);
        ref3.child(id).child("members").child("sender").setValue(current_user);
        ref4.child(id).child("sender").push().setValue(current_user);
        String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
        //---------------THIS IS THE PROBLEM. UNTIL A VALUE IS SET, NO DATABASE ENTRY IS CREATED AND HENCE CHECKTHREAD DOESNT WORK-----------
        ref4.child(id).child("msg").push().setValue(messagetext);
        ref4.child(id).child("time").push().setValue(mydate);

        //listen.removeEventListener(child_listen);



    };

    private void updateUI()
    {
        ChildEventListener child_listen = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                list.add(dataSnapshot.getValue().toString());
                Log.e("LIST:",list.toString());
                //i++;

                lv.setAdapter(new ArrayAdapter<String>(ChatActivity.this,android.R.layout.simple_list_item_1,list));
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
        listen.addChildEventListener(child_listen);
    }

}

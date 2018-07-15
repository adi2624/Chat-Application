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

    DatabaseReference ref3;
    DatabaseReference ref4;
    DatabaseReference ref2;
    String id="100";
    Button send;
    private final List<String> list = new ArrayList<>();
    List<String> debug = new ArrayList<>();
    ListView lv;
    String ListArray[]={};
    TextInputEditText text_object;
    LinearLayout layout;
    String current_user;
    String email;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ref2 = FirebaseDatabase.getInstance().getReference();
        ref3 = ref2.child("threads");
        ref4 = FirebaseDatabase.getInstance().getReference("messages");
        lv =(ListView)findViewById(R.id.list);
        textView = (TextView)findViewById(R.id.test);
        Intent myIntent = getIntent();
        email = myIntent.getStringExtra("Email");
   //     Toast.makeText(getApplicationContext(),"Selected : "+ email,Toast.LENGTH_LONG).show();
        current_user="";
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null)
        {
            current_user = user.getEmail();
        }
        CheckThread();

        send=(Button)findViewById(R.id.button);
        Toast.makeText(getApplicationContext(),"Size: "+ debug.size(),Toast.LENGTH_LONG).show();
        text_object = (TextInputEditText)findViewById(R.id.text);
        // Use this : ref3.addChildEventListener()
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String messagetext =text_object.getText().toString();
                Toast.makeText(getApplicationContext(),"Sent : "+ messagetext,Toast.LENGTH_LONG).show();
                CreateInstance(email,messagetext,id);


            }
        });







    }
    private void CheckThread()
    {
        ref3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i=0;
                for(DataSnapshot ds: dataSnapshot.getChildren())
                {
                    String sender = ds.child("members").child("sender").getValue().toString();
                    String receiver = ds.child("members").child("receiver").getValue().toString();

                    Log.e("TAG","Sender: "+sender + "Receiver"+receiver);
                    if(current_user.equals(sender) && receiver.equals(email))
                    {
                        id = ds.getKey();
                        Toast.makeText(getApplicationContext()," OLD THREAD RESUMED with ID: "+id,Toast.LENGTH_LONG).show();
                        break;
                    }
                    else
                    {
                        Random r = new Random();
                        int Low=1080;
                        int High=1920;
                        int Result = r.nextInt(High-Low)+Low;
                        id = Result+"";
                        // Toast.makeText(getApplicationContext()," NEW THREAD RESUMED",Toast.LENGTH_LONG).show();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void CreateInstance(String email,String messagetext,String id)
    {
        //---------------THIS IS THE PROBLEM. UNTIL A VALUE IS SET, NO DATABASE ENTRY IS CREATED AND HENCE CHECKTHREAD DOESNT WORK-----------
        ref3.child(id).child("members").child("receiver").setValue(email);
        ref3.child(id).child("members").child("sender").setValue(current_user);
        ref4.child(id).child("sender").push().setValue(current_user);
        String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
        ref4.child(id).child("msg").push().setValue(messagetext);
        ref4.child(id).child("time").push().setValue(mydate);
        DatabaseReference listen = ref2.child("messages").child(id).child("msg");
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren())
                {
                   list.add(ds.getValue().toString());
                }
                lv.setAdapter(new ArrayAdapter<String>(ChatActivity.this,
                        android.R.layout.simple_list_item_1,list));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        listen.addListenerForSingleValueEvent(valueEventListener);
    };
}

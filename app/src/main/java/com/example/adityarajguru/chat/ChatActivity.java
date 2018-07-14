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
import android.widget.Button;
import android.widget.LinearLayout;
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

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ChatActivity extends AppCompatActivity {

    DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference();
    DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference();
    DatabaseReference ref3 = FirebaseDatabase.getInstance().getReference("threads");
    DatabaseReference ref4 = FirebaseDatabase.getInstance().getReference("messages");
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    String id;
    Button send;
    TextInputEditText text_object;
    LinearLayout layout;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        textView = (TextView)findViewById(R.id.test);
        Random r = new Random();
        int Low=1080;
        int High=1920;
        int Result = r.nextInt(High-Low)+Low;
        id = Result+"";
        Intent myIntent = getIntent();
        final String email = myIntent.getStringExtra("Email");
        Toast.makeText(getApplicationContext(),"Selected : "+ email,Toast.LENGTH_LONG).show();
        String current_user="";
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null)
        {
            current_user = user.getEmail();
        }
        ref3.child(id).child("members").push().setValue(email);
        ref3.child(id).child("members").push().setValue(current_user);
        ref4.child(id).child("sender").push().setValue(current_user);
        send=(Button)findViewById(R.id.button);
        text_object = (TextInputEditText)findViewById(R.id.text);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String messagetext =text_object.getText().toString();
                Toast.makeText(getApplicationContext(),"Sent : "+ messagetext,Toast.LENGTH_LONG).show();
                CreateInstance(email,messagetext,id);


            }
        });







    }
    private void CreateInstance(String email,String messagetext,String id)
    {

        String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
        ref4.child(id).child("msg").push().setValue(messagetext);
        ref4.child(id).child("time").push().setValue(mydate);
        DatabaseReference listen = ref.child("messages").child(id).child("msg");
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren())
                {
                   textView.setText(ds.getValue().toString());
                   Log.e("MSG", ds.getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        listen.addListenerForSingleValueEvent(valueEventListener);
    };
}

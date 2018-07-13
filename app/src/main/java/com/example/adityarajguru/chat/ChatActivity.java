package com.example.adityarajguru.chat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class ChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Intent myIntent = getIntent();
        String email = myIntent.getStringExtra("Email");
        Toast.makeText(getApplicationContext(),"Selected : "+ email,Toast.LENGTH_LONG).show();
    }
}

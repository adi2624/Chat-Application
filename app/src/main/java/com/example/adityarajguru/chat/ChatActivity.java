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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
    public DatabaseReference sender;
    public DatabaseReference time;


    String id="";
    int i=0;
    private List<String> list = new ArrayList<>();
    private List<String> is_sender = new ArrayList<>();
    private List<String> time_list = new ArrayList<>();
    String current_user;
    String email;
    private EditText messageET;
    private ListView messagesContainer;
    private Button sendBtn;
    private ChatAdapter adapter;
    private ArrayList<ChatMessage> chatHistory;



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
        messagesContainer=(ListView)findViewById(R.id.messagesContainer);
        messageET=(EditText)findViewById(R.id.messageEdit);
        sendBtn=(Button)findViewById(R.id.chatSendButton);
        TextView meLabel = (TextView)findViewById(R.id.meLbl);
        TextView companionlabel = (TextView)findViewById(R.id.friendLabel);
        RelativeLayout container = (RelativeLayout) findViewById(R.id.container);
        companionlabel.setText(email);
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
                        Log.e("d","IAM IN");
                        break;
                    }
                    else if(email.equals(ds.child("members").child("sender").getValue().toString()) && current_user.equals(ds.child("members").child("receiver").getValue().toString()))
                    {
                        id=ds.getKey();
                        Log.e("d","IAM IN");
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
                ref3.child(id).child("members").child("receiver").setValue(email);
                ref3.child(id).child("members").child("sender").setValue(current_user);
                sender=ref4.child(id).child("sender");
                listen = ref4.child(id).child("msg");
                time = ref4.child(id).child("time");
                CheckSender();
                addtime();
                attachListener();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String messagetext =messageET.getText().toString();
                Toast.makeText(getApplicationContext(),"Sent : "+ messagetext,Toast.LENGTH_LONG).show();
                Log.e("ID :",id+"");
                CreateInstance(email,messagetext,id);


            }
        });



    }

    private void CreateInstance(String email,String messagetext,String id)
    {
        Log.e("ON CREATE WITH ID :",id+"");


        ref4.child(id).child("sender").push().setValue(current_user);
        String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
        ref4.child(id).child("time").push().setValue(mydate);   //PUSHED FIRST DUE TO LISTENER CONFLICTS
        ref4.child(id).child("msg").push().setValue(messagetext);
    };

    private void attachListener()
    {

        ChildEventListener child_listen = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                list.add(dataSnapshot.getValue().toString());
                Log.e("LIST:",list.toString());
                //i++;
                chatHistory=new ArrayList<ChatMessage>();
                for(int i=0;i<list.size();i++)
                {
                    ChatMessage msg = new ChatMessage();
                   if(is_sender.get(i).equals(current_user))
                    {
                        msg.setMe(true);
                    }
                    else
                    {
                        msg.setMe(false);
                    }
                    String temp_message = is_sender.get(i)+"\n";
                   temp_message=temp_message+list.get(i);
                    msg.setMessage(temp_message);
                    msg.setSender(is_sender.get(i));
                    msg.setDate(time_list.get(i));

                    chatHistory.add(msg);
                }
                adapter = new ChatAdapter(ChatActivity.this,new ArrayList<ChatMessage>());
                messagesContainer.setAdapter(adapter);
                for(int i=0;i<chatHistory.size();i++)
                {
                    ChatMessage message = chatHistory.get(i);
                    displayMessage(message);

                }
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
    public void displayMessage(ChatMessage message) {
        adapter.add(message);
        adapter.notifyDataSetChanged();
        scroll();
    }

    private void scroll() {
        messagesContainer.setSelection(messagesContainer.getCount() - 1);
    }
    private void CheckSender()
    {
        ChildEventListener child_listen = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                is_sender.add(dataSnapshot.getValue().toString());
                Log.e("Senders : ",list.toString());
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
        sender.addChildEventListener(child_listen);
    }

    private void addtime()
    {
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.e("TIME: ",dataSnapshot.getValue().toString());
                time_list.add(dataSnapshot.getValue().toString());

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
        time.addChildEventListener(childEventListener);
    }



}

package com.example.adityarajguru.chat;

public class ChatMessage {
    private String message;
    private boolean isMe;
    private String dateTime;


    public boolean getIsme() {
        return isMe;
    }
    public void setMe(boolean isMe) {
        this.isMe = isMe;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getDate()
    {
        return dateTime;
    }
    public void setDate(String date)
    {
        dateTime=date;
    }


}

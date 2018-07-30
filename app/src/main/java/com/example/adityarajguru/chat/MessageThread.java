package com.example.adityarajguru.chat;

public class MessageThread {
    private String receiver;
    private String message;
    private String count;
    private float rating;
    public String getMessage() {

        return message;
    }
    public float getRating()
    {
        return rating;
    }
    public void setRating(float rating_input)
    {
        rating=rating_input;
    }
    public void setMessage(String message_input)
    {
        message=message_input;
    }
    public String getReceiver(){
        return receiver;
    }
    public void setReceiver(String receiver_input)
    {
        receiver=receiver_input;
    }

    public void setCount(String count_input)
    {
        count=count_input;
    }

    public String getCount()
    {
        return count;
    }






}

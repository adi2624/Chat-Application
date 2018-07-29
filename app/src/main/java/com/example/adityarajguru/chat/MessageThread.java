package com.example.adityarajguru.chat;

public class MessageThread {
    private String receiver;
    private String message;
    private String count;
    public String getMessage() {

        return message;
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

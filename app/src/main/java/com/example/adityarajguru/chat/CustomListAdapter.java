package com.example.adityarajguru.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomListAdapter extends BaseAdapter {
    private ArrayList<MessageThread> listData;
    private LayoutInflater layoutInflater;

    public CustomListAdapter(Context aContext, ArrayList<MessageThread> listData) {
        this.listData = listData;
        layoutInflater = LayoutInflater.from(aContext);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_row_layout, null);
            holder = new ViewHolder();
            holder.SenderView = (TextView) convertView.findViewById(R.id.sender);
            holder.MessageView = (TextView) convertView.findViewById(R.id.message);
            holder.CountView = (TextView) convertView.findViewById(R.id.count);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.SenderView.setText(listData.get(position).getReceiver());
        holder.MessageView.setText( listData.get(position).getMessage());
        holder.CountView.setText(listData.get(position).getCount());
        return convertView;
    }

    static class ViewHolder {
        TextView SenderView;
        TextView MessageView;
        TextView CountView;
    }
}

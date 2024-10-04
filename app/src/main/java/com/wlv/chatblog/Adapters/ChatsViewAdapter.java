package com.wlv.chatblog.Adapters;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wlv.chatblog.ItemClickListener;
import com.wlv.chatblog.LastMessageModel;
import com.wlv.chatblog.R;

import java.io.ByteArrayInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ChatsViewAdapter extends RecyclerView.Adapter<ChatsViewAdapter.MyViewHolder> {

    private List<LastMessageModel> messagesList;
    private ItemClickListener mListener;




    public void setClickListener(ItemClickListener itemClickListener) {
        this.mListener = itemClickListener;
    }


    //Set chat details
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public   TextView          senderName,lastMessage,messageTime;
        public   ImageView         contactPic;
        private  ItemClickListener mListener;

        MyViewHolder(View itemView, ItemClickListener listener) {
            super(itemView);
            senderName = (TextView) itemView.findViewById(R.id.senderName);
            lastMessage = (TextView) itemView.findViewById(R.id.lastMessage);
            contactPic = (ImageView) itemView.findViewById(R.id.senderPicture);
            messageTime = (TextView) itemView.findViewById(R.id.messageTime);

            mListener = listener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            shareblog(v,lastMessage );
        }
    }

    //share the chat text to the youtube app
    private void shareblog(View v, TextView lastmessage) {
        String messageText = lastmessage.getText().toString().trim();
        Intent intent = new Intent(Intent.ACTION_SEARCH);
        intent.setPackage("com.google.android.youtube");
        intent.putExtra(SearchManager.QUERY, messageText);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        v.getContext().startActivity(intent);

    }



    public ChatsViewAdapter(List<LastMessageModel> messagesList,ItemClickListener listener) {
        this.messagesList = messagesList;
        mListener = listener;
    }
    @Override
    public ChatsViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_row, parent, false);

        return new MyViewHolder(itemView,mListener);
    }

    @Override
    public void onBindViewHolder(ChatsViewAdapter.MyViewHolder holder, int position) {
        LastMessageModel contact = messagesList.get(position);
        holder.senderName.setText(contact.getUsername());
        holder.lastMessage.setText(contact.getLastMessage());
        holder.messageTime.setText(formatDate(contact.getTimestamp()));

        Bitmap u = openPhoto(contact.getProfilePic(),holder);
        if (u != null) {

            holder.contactPic.setImageBitmap(u);
        } else {
            holder.contactPic.setImageResource(R.drawable.ic_person_black_48dp);
        }
    }

    @Override
    public int getItemCount() {
        return messagesList.size();
    }

    public Bitmap openPhoto(String contactId,ChatsViewAdapter.MyViewHolder holder) {
        Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, new Long(contactId));
        Uri photoUri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
        Cursor cursor = holder.itemView.getContext().getContentResolver().query(photoUri,
                new String[]{ContactsContract.Contacts.Photo.PHOTO}, null, null, null);
        if (cursor == null) {
            return null;
        }
        try {
            if (cursor.moveToFirst()) {
                byte[] data = cursor.getBlob(0);
                if (data != null) {
                    return BitmapFactory.decodeStream(new ByteArrayInputStream(data));
                }
            }
        } finally {
            cursor.close();
        }
        return null;

    }




    private String formatDate(String dateStr) {
        try {
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = fmt.parse(dateStr);
            SimpleDateFormat fmtOut = new SimpleDateFormat("HH:mm");
            return fmtOut.format(date);
        } catch (ParseException e) {

        }

        return "";
    }


    
}
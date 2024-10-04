package com.wlv.chatblog;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.wlv.chatblog.Adapters.DatabaseAdapter;
import com.wlv.chatblog.Adapters.MessagesAdapter;


import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private String             username;
    private String             userNumber;
    private String             contactID;
    private EditText           messageContent;
    private ImageButton        sendMessage;
    private ImageButton        searchMessage;
    private DatabaseAdapter    helper;
    private List<MessageModel> messages = new ArrayList<>();
    private RecyclerView       messagesView;
    private MessagesAdapter    mAdapter;
    private TextView           mTextView;
    private TextView           mTextView1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //Showing back button on toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        username       = intent.getStringExtra("Contact name");
        userNumber     = intent.getStringExtra("Contact number");
        contactID      = intent.getStringExtra("Contact id");
        messageContent = (EditText) findViewById(R.id.messageContent);
        sendMessage    = (ImageButton) findViewById(R.id.sendMessage);
        searchMessage  = (ImageButton) findViewById(R.id.searchMessage);
        messagesView = (RecyclerView) findViewById(R.id.chat_view);
        helper         = new DatabaseAdapter(this);


        if(userNumber.length() == 10) {
            userNumber = "94"+userNumber.substring(1); //remove the first zero and add 94 instead
        } else {
            userNumber = userNumber.replaceAll("[\\D]", ""); //remove all special characters from phone number
        }

        sendMessage.setEnabled(false);
        searchMessage.setEnabled(false);
        sendMessage.setVisibility(View.VISIBLE);
        messageContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().equals("")) {
                    sendMessage.setEnabled(false);
                    sendMessage.setVisibility(View.VISIBLE);
                } else {
                    searchMessage.setEnabled(true);
                    sendMessage.setEnabled(true);
                    sendMessage.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //We will set username as title for acitivity
        getSupportActionBar().setTitle(username);


        SQLiteDatabase db = helper.getWritableDatabase();
        helper.createDb(db, userNumber);
        messages.addAll(helper.getAllMessages(userNumber));
        mAdapter = new MessagesAdapter(messages);
        RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,true);
        messagesView.setLayoutManager(mLayoutManager2);
        messagesView.setItemAnimator(new DefaultItemAnimator());

        messagesView.setAdapter(mAdapter);
        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessageClick(v);
            }
        });

        searchMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchMessageClick(v);
            }
        });



    }

    //When sendMessage button is clicked we add entered message to the chat table with this user
    public void sendMessageClick(View v) {
        String messageText = messageContent.getText().toString().trim();
        String userN = "me";
        helper.insertMessage(userN, userNumber, userNumber,messageText);
        helper.insertLastMessage(username,userNumber,messageText,contactID);
        messageContent.setText("");
        checker();
        giveReply();
    }
    //search message function
    public void searchMessageClick(View v) {
        String messageText = messageContent.getText().toString().trim();
        String userN = "me";
        searchChecker(messageText);
        messageContent.setText("");
    }

    //Checking if new message was added and refreshing our RecyclerView
    public void checker() {
        if (helper.getMessagesCount(userNumber) > messages.size()) {
            messages.clear();
            messages.addAll(helper.getAllMessages(userNumber));
            mAdapter.notifyDataSetChanged();
        }
    }

    public void searchChecker(String message) {
                      messages.clear();
            messages.addAll(helper.getSearchAllMessages(userNumber,message));
            mAdapter.notifyDataSetChanged();

    }

    //Simple reply system from contact
    public void giveReply() {
        String messageText = "Hello";
        helper.insertMessage(username,userNumber,userNumber,messageText);
        helper.insertLastMessage(username,userNumber,messageText,contactID);
        checker();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void onClick(View v)
    {
        mTextView=(TextView) v.findViewById(R.id.textView3);

        String message=mTextView.getText().toString();
          DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:

                        helper.deleteMessage(message,userNumber);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setMessage("Are you want to delete the message?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    //Delete message
    public void onClicked(View v)
    {
        mTextView1=(TextView) v.findViewById(R.id.textView1);

      System.out.println(userNumber);
        String message=mTextView1.getText().toString();
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:

                        helper.deleteMessage(message,userNumber);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setMessage("Are you want to delete the message?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }
}
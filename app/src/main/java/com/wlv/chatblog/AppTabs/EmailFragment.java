package com.wlv.chatblog.AppTabs;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.Toast;
import android.content.Context;

import com.wlv.chatblog.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EmailFragment extends Fragment {

    private static final int REQUEST_PHOTO = 1;
    private static final int RESULT_OK = 2;
    private File mPhotoFile;
    private ImageButton mImageButton;
    private ImageButton mSendButton;
    private EditText mailContent;
    private  String Fpath;
    public EmailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_email, container, false);

        mImageButton=(ImageButton) v.findViewById(R.id.addImage);
        mSendButton=(ImageButton) v.findViewById(R.id.shareEmail);
        mailContent = (EditText) v.findViewById(R.id.mailContent);
        mImageButton.setEnabled(true);
        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, REQUEST_PHOTO);
            }
        });
        mSendButton.setEnabled(true);
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            email(v);
            }
        });

        // Inflate the layout for this fragment
        return v;
    }

    //Add image file location
    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
         Fpath = data.getDataString();
        Toast yourToast = Toast.makeText(this.getActivity().getApplicationContext(), "Image file added", Toast.LENGTH_SHORT);
        yourToast.show();


    }

    //send email
    public void email(View view){

        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.setType("application/image");
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"Subject");
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, mailContent.getText().toString().trim());
        emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(Fpath));
        startActivity(Intent.createChooser(emailIntent, "Send mail..."));
    }
}

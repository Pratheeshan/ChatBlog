package com.wlv.chatblog;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.wlv.chatblog.Adapters.DatabaseAdapter;
import com.wlv.chatblog.Adapters.FragmentAdapter;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 1;
    SQLiteDatabase myDb;
    DatabaseAdapter myHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

            setContentView(R.layout.activity_main);

            getSupportActionBar().setElevation(0);
            //Finding ViewPager for swiping between tabs then creating adapter to know
            //which fragment should be shown on each page
            ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
            FragmentAdapter adapter = new FragmentAdapter(this, getSupportFragmentManager());

            // Set the adapter onto the view pager
            viewPager.setAdapter(adapter);

            // Give the TabLayout the ViewPager
            TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs_slide);
            tabLayout.setupWithViewPager(viewPager);
    }
}
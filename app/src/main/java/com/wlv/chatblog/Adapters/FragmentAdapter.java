package com.wlv.chatblog.Adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.wlv.chatblog.AppTabs.EmailFragment;
import com.wlv.chatblog.AppTabs.ChatsFragment;
import com.wlv.chatblog.AppTabs.ContactsFragment;
import com.wlv.chatblog.R;


public class FragmentAdapter extends FragmentPagerAdapter {

    private Context mContext;

    public FragmentAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    // This determines the fragment for each tab
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new EmailFragment();
        }
        if (position== 1) {
            return new ContactsFragment();
        }
        else{
            return new ChatsFragment();
        }
    }

    // Number of tabs
    @Override
    public int getCount() {
        return 3;
    }

    // Titles for tabs
    @Override
    public CharSequence getPageTitle(int position) {

        switch (position) {
            case 0:
                return "Email";
            case 1:
                return mContext.getString(R.string.contacts_tab_title) ;
            case 2:
                return mContext.getString(R.string.chats_tab_title);
            default:
                return null;
        }
    }

}

package com.wlv.chatblog.AppTabs;


import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wlv.chatblog.Adapters.ChatsViewAdapter;
import com.wlv.chatblog.Adapters.DatabaseAdapter;
import com.wlv.chatblog.ChatActivity;
import com.wlv.chatblog.DividerItemDecoration;
import com.wlv.chatblog.ItemClickListener;
import com.wlv.chatblog.LastMessageModel;
import com.wlv.chatblog.R;

import java.util.ArrayList;
import java.util.List;

public class ChatsFragment extends Fragment{

    private DatabaseAdapter helper;
    private ChatsViewAdapter viewAdapter;
    private List<LastMessageModel> messages = new ArrayList<>();
    private RecyclerView chatsView;
    private ChatsViewAdapter.MyViewHolder holder;
    private TextView contactName;
    private SQLiteDatabase db;



    public ChatsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View chatsFragment = inflater.inflate(R.layout.fragment_chats, container, false);


        // Inflate the layout for this fragment
        return chatsFragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        chatsView = (RecyclerView) getActivity().findViewById(R.id.chatsList);
        RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(getActivity().getApplicationContext());
        chatsView.setHasFixedSize(true);
        chatsView.setLayoutManager(mLayoutManager2);
        DividerItemDecoration decor = new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL);
        chatsView.addItemDecoration(decor);
        chatsView.setItemAnimator(new DefaultItemAnimator());

        helper   = new DatabaseAdapter(getActivity());
        db       = helper.getReadableDatabase();



        final ItemClickListener listener = new ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra("Contact name",messages.get(position).getUsername());
                intent.putExtra("Contact number",messages.get(position).getUsernumber());
                intent.putExtra("Contact id",messages.get(position).getProfilePic());
                startActivity(intent);
            }
        };

        messages.clear();
        messages.addAll(helper.getLastMessages());
        viewAdapter = new ChatsViewAdapter(messages,listener);

        chatsView.setAdapter(viewAdapter);
        viewAdapter.notifyDataSetChanged();
    }


}

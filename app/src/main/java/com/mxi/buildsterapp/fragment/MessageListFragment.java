package com.mxi.buildsterapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mxi.buildsterapp.R;
import com.mxi.buildsterapp.adapter.ActionItemAdapter;
import com.mxi.buildsterapp.adapter.MessageAdapter;
import com.mxi.buildsterapp.model.ActionItemData;
import com.mxi.buildsterapp.model.MessageData;

import java.util.ArrayList;

public class MessageListFragment extends Fragment {

    ImageView iv_back;

    RecyclerView rc_message;
    ArrayList<MessageData> message_list;
    MessageAdapter mAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.message_list_fragment, container, false);

        init(rootView);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void init(View rootView) {

        iv_back = (ImageView) rootView.findViewById(R.id.iv_back);

        rc_message = (RecyclerView) rootView.findViewById(R.id.rc_message);
        rc_message.setLayoutManager(new LinearLayoutManager(getActivity()));
        rc_message.setItemAnimator(new DefaultItemAnimator());

        message_list = new ArrayList<>();
        message_list.add(new MessageData("Mark Taylor"));
        message_list.add(new MessageData("Aronn"));
        message_list.add(new MessageData("Steve H"));
        message_list.add(new MessageData("Smith M"));
        message_list.add(new MessageData("Jolly"));
        message_list.add(new MessageData("Avenger"));
        message_list.add(new MessageData("Throne"));
        message_list.add(new MessageData("Marvel"));
        message_list.add(new MessageData("Albert"));
        message_list.add(new MessageData("Tony Lee"));

        mAdapter = new MessageAdapter(message_list, R.layout.rc_message_item, getContext());
        rc_message.setAdapter(mAdapter);


        clickListner();
    }

    private void clickListner() {

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getActivity().overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                getActivity().onBackPressed();
            }
        });

    }

}


package com.mxi.buildsterapp.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.mxi.buildsterapp.R;
import com.mxi.buildsterapp.activity.EditprofileActivity;
import com.mxi.buildsterapp.adapter.ActionItemAdapter;
import com.mxi.buildsterapp.adapter.CountryAdapter;
import com.mxi.buildsterapp.model.ActionItemData;
import com.mxi.buildsterapp.model.Country;
import com.mxi.buildsterapp.model.MyProjectData;

import java.util.ArrayList;

public class ActionItemFragment extends Fragment {

    ImageView iv_back;

    EditText ed_search;

    RecyclerView rc_action_item;
    ArrayList<ActionItemData> screen_list;
    ActionItemAdapter slAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.action_item_fragment, container, false);

        init(rootView);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void init(View rootView) {

        iv_back = (ImageView) rootView.findViewById(R.id.iv_back);

        ed_search = (EditText) rootView.findViewById(R.id.ed_search);

        rc_action_item = (RecyclerView) rootView.findViewById(R.id.rc_action_item);
        rc_action_item.setLayoutManager(new LinearLayoutManager(getActivity()));
        rc_action_item.setItemAnimator(new DefaultItemAnimator());


        screen_list = new ArrayList<>();
        screen_list.add(new ActionItemData("Screen 1"));
        screen_list.add(new ActionItemData("Screen 2"));
        screen_list.add(new ActionItemData("Screen 3"));
        screen_list.add(new ActionItemData("Screen 4"));
        screen_list.add(new ActionItemData("Screen 5"));
        screen_list.add(new ActionItemData("Screen 6"));
        screen_list.add(new ActionItemData("Screen 7"));
        screen_list.add(new ActionItemData("Screen 8"));
        screen_list.add(new ActionItemData("Screen 9"));
        screen_list.add(new ActionItemData("Screen 10"));

        slAdapter = new ActionItemAdapter(screen_list, R.layout.rc_action_item, getContext());
        rc_action_item.setAdapter(slAdapter);

        ed_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

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

    private void filter(String text) {


        ArrayList<ActionItemData> screen_list_filtered = new ArrayList<>();


        for (ActionItemData s : screen_list_filtered) {

            if (s.getScreen_name().toLowerCase().contains(text.toLowerCase())) {

                screen_list_filtered.add(s);
            }
        }

        slAdapter.filterList(screen_list_filtered);
    }

}

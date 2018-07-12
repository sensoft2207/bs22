package com.mxi.buildsterapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.mxi.buildsterapp.R;
import com.mxi.buildsterapp.adapter.MyProjectAdapter;
import com.mxi.buildsterapp.comman.CommanClass;
import com.mxi.buildsterapp.model.MyProjectData;

import java.util.ArrayList;

public class MyprojectFragment extends Fragment {

    RecyclerView rc_my_project;

    MyProjectAdapter mProjectAdapter;

    ArrayList<MyProjectData> project_list;

    CommanClass cc;

    EditText ed_search;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.myproject_fragment, container, false);

        cc = new CommanClass(getActivity());

        ed_search = (EditText) rootView.findViewById(R.id.ed_search);

        rc_my_project = (RecyclerView)rootView.findViewById(R.id.rc_my_project);
        rc_my_project.setLayoutManager(new LinearLayoutManager(getContext()));
        rc_my_project.setItemAnimator(new DefaultItemAnimator());

        project_list = new ArrayList<MyProjectData>();
        project_list.add(new MyProjectData("Abhishree avenue"));
        project_list.add(new MyProjectData("Akshat tower"));
        project_list.add(new MyProjectData("Balaji avenue"));
        project_list.add(new MyProjectData("Vishwash residency"));

        mProjectAdapter = new MyProjectAdapter(project_list, R.layout.rc_myproject_item,getContext());
        rc_my_project.setAdapter(mProjectAdapter);


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

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void filter(String text) {


        ArrayList<MyProjectData> project_list_filtered = new ArrayList<>();


        for (MyProjectData s : project_list) {

            if (s.getProject_name().toLowerCase().contains(text.toLowerCase())) {

                project_list_filtered.add(s);
            }
        }

        mProjectAdapter.filterList(project_list_filtered);
    }
}


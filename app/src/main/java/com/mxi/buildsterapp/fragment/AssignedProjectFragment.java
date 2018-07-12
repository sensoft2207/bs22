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
import com.mxi.buildsterapp.adapter.AssignedProjectAdapter;
import com.mxi.buildsterapp.adapter.MyProjectAdapter;
import com.mxi.buildsterapp.model.MyProjectData;

import java.util.ArrayList;

public class AssignedProjectFragment extends Fragment {

    RecyclerView rc_assigned_project;

    AssignedProjectAdapter mProjectAdapter;

    ArrayList<MyProjectData> project_list;

    EditText ed_search;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.assignedproject_fragment, container, false);

        rc_assigned_project = (RecyclerView)rootView.findViewById(R.id.rc_assigned_project);
        rc_assigned_project.setLayoutManager(new LinearLayoutManager(getContext()));
        rc_assigned_project.setItemAnimator(new DefaultItemAnimator());

        ed_search = (EditText) rootView.findViewById(R.id.ed_search);

        project_list = new ArrayList<MyProjectData>();
        project_list.add(new MyProjectData("Abhishree avenue"));
        project_list.add(new MyProjectData("Akshat tower"));
        project_list.add(new MyProjectData("Balaji avenue"));
        project_list.add(new MyProjectData("Vishwash residency"));

        mProjectAdapter = new AssignedProjectAdapter(project_list, R.layout.rc_assignedpro_item,getContext());
        rc_assigned_project.setAdapter(mProjectAdapter);

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


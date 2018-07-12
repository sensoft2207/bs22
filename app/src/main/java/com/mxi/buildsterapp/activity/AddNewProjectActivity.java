package com.mxi.buildsterapp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.mxi.buildsterapp.R;
import com.mxi.buildsterapp.comman.CommanClass;

public class AddNewProjectActivity extends AppCompatActivity {

    CommanClass cc;

    EditText ed_projectname,ed_projectaddress,ed_projectmanager,ed_companyname;

    String projectname,projectaddress,projectmanager,companyname,manager_fname,manager_lname;

    ImageView iv_back, iv_profile_pic;

    Button btn_next;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_project_activity);

        init();
    }

    private void init() {

        cc = new CommanClass(this);

        manager_fname = cc.loadPrefString("firstname");
        manager_lname = cc.loadPrefString("lastname");

        ed_projectname = (EditText) findViewById(R.id.ed_projectname);
        ed_projectaddress = (EditText) findViewById(R.id.ed_projectaddress);
        ed_projectmanager = (EditText) findViewById(R.id.ed_projectmanager);
        ed_companyname = (EditText) findViewById(R.id.ed_companyname);

        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_profile_pic = (ImageView) findViewById(R.id.iv_profile_pic);

        btn_next = (Button) findViewById(R.id.btn_next);

        ed_projectmanager.setText(manager_fname+" "+manager_lname);

        clickListener();

    }

    private void clickListener() {

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                onBackPressed();
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addProjectValidation();

            }
        });
    }

    private void addProjectValidation() {

        projectname = ed_projectname.getText().toString();
        projectaddress = ed_projectaddress.getText().toString();
        projectmanager = ed_projectmanager.getText().toString();
        companyname = ed_companyname.getText().toString();

        if (!cc.isConnectingToInternet()){

            cc.showToast(getString(R.string.no_internet));
        }else if (projectname.equals("")){

            cc.showToast(getString(R.string.pname_vali));
            ed_projectname.requestFocus();

        }else if (projectaddress.equals("")){

            cc.showToast(getString(R.string.paddress_vali));
            ed_projectaddress.requestFocus();
        }else if (projectmanager.equals("")){

            cc.showToast(getString(R.string.pmanager_vali));
            ed_projectmanager.requestFocus();
        }else if (companyname.equals("")){

            cc.showToast(getString(R.string.pcompany_vali));
            ed_companyname.requestFocus();
        }else {

            Intent intentUploadPdf = new Intent(AddNewProjectActivity.this, UploadPdfActivity.class);
            startActivity(intentUploadPdf);
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
}

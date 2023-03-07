package com.testapp.duplicatefileremover;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.testapp.duplicatefileremover.utilts.SharePreferenceUtils;
import com.testapp.duplicatefileremover.utilts.Utils;

import java.util.Locale;



public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    Toolbar toolbar;
    TextView tvLanguage;
    RelativeLayout rlMoreApp,rlRate,rlShare,rlPolicy,rlLanguage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setLocale(this);
        setContentView(R.layout.activity_dupicate_setting);
        intView();
        intData();
        intEvent();
    }
    public void intView(){
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.setting));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        tvLanguage = (TextView)findViewById(R.id.tvLanguage);
        rlMoreApp = (RelativeLayout)findViewById(R.id.rlMoreApp);
        rlRate = (RelativeLayout)findViewById(R.id.rlRate);
        rlShare = (RelativeLayout)findViewById(R.id.rlShare);
        rlPolicy = (RelativeLayout)findViewById(R.id.rlPolicy);
        rlLanguage = (RelativeLayout)findViewById(R.id.rlLanguage);
    }
    public void intData(){

        tvLanguage.setText(getResources().getStringArray(R.array.arrLanguage)[SharePreferenceUtils.getInstance(SettingActivity.this).getLanguageIndex()]);

    }
    public void intEvent(){
        rlMoreApp.setOnClickListener(this);
        rlRate.setOnClickListener(this);
        rlShare.setOnClickListener(this);
        rlPolicy.setOnClickListener(this);
        rlLanguage.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        int id = view.getId();
        if (id == R.id.rlLanguage) {
            showDialogLanguage();
        } else if (id == R.id.rlRate) {
        } else if (id == R.id.rlShare) {
        } else if (id == R.id.rlPolicy) {
        }
    }
    public void showDialogLanguage() {
        final String [] arrLanguage = getResources().getStringArray(R.array.arrLanguage) ;
        AlertDialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);

        builder.setTitle(R.string.language_setting);
        String[] items = getResources().getStringArray(R.array.arrLanguage);

        builder.setSingleChoiceItems(items, SharePreferenceUtils.getInstance(SettingActivity.this).getLanguageIndex(),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tvLanguage.setText(arrLanguage[which]);
                        setSaveLanguage(which);
                        dialog.dismiss();
                    }
                });



        String negativeText = getString(android.R.string.cancel);
        builder.setNegativeButton(negativeText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // negative button logic
                    }
                });

        dialog = builder.create();
        // display dialog
        dialog.show();
    }
    public void setSaveLanguage(int index){
        if(index==0) {
            SharePreferenceUtils.getInstance(SettingActivity.this).saveLanguageIndex(0);
            setLocale("cs");
        }else if(index==1) {
            SharePreferenceUtils.getInstance(SettingActivity.this).saveLanguageIndex(1);
            setLocale("de");
        } else if(index==2){
            SharePreferenceUtils.getInstance(SettingActivity.this).saveLanguageIndex(2);
            setLocale("en");
        }else if(index==3){
            SharePreferenceUtils.getInstance(SettingActivity.this).saveLanguageIndex(3);
            setLocale("es");
        }else if(index==4){
            SharePreferenceUtils.getInstance(SettingActivity.this).saveLanguageIndex(4);
            setLocale("fr");
        }else if(index==5){
            SharePreferenceUtils.getInstance(SettingActivity.this).saveLanguageIndex(5);
            setLocale("in");
        }else if(index==6){
            SharePreferenceUtils.getInstance(SettingActivity.this).saveLanguageIndex(6);
            setLocale("it");
        }else if(index==7){
            SharePreferenceUtils.getInstance(SettingActivity.this).saveLanguageIndex(7);
            setLocale("pl");
        }else if(index==8){
            SharePreferenceUtils.getInstance(SettingActivity.this).saveLanguageIndex(8);
            setLocale("pt");
        }else if(index==9){
            SharePreferenceUtils.getInstance(SettingActivity.this).saveLanguageIndex(9);
            setLocale("ru");
        }else if(index==10){
            SharePreferenceUtils.getInstance(SettingActivity.this).saveLanguageIndex(10);
            setLocale("tr");
        }else if(index==11){
            SharePreferenceUtils.getInstance(SettingActivity.this).saveLanguageIndex(11);
            setLocale("vi");
        }else if(index==12){
            SharePreferenceUtils.getInstance(SettingActivity.this).saveLanguageIndex(12);
            setLocale("ar");
        }else if(index==13){
            SharePreferenceUtils.getInstance(SettingActivity.this).saveLanguageIndex(13);
            setLocale("th");
        }else if(index==14){
            SharePreferenceUtils.getInstance(SettingActivity.this).saveLanguageIndex(14);
            setLocale("bn");
        }else if(index==15){
            SharePreferenceUtils.getInstance(SettingActivity.this).saveLanguageIndex(15);
            setLocale("hi");
        }else if(index==16){
            SharePreferenceUtils.getInstance(SettingActivity.this).saveLanguageIndex(16);
            setLocale("ta");
        }

    }

    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        finish();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}

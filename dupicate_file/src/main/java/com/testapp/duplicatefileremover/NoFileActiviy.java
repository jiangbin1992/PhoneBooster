package com.testapp.duplicatefileremover;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.testapp.duplicatefileremover.utilts.Utils;


public class NoFileActiviy extends AppCompatActivity implements View.OnClickListener {
    @Override
    public void onClick(View view) {

    }

    Toolbar toolbar;

    // CardView cvImage,cvAudio,cvVideo,cvDoc,cvOther;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setLocale(this);
        setContentView(R.layout.activity_dupicate_row);
        intView();
        intEvent();
        intData();
    }

    public void intView() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setTitle(getIntent().getStringExtra("title_tool_bar"));
    }

    public void intEvent() {
    }

    public void intData() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

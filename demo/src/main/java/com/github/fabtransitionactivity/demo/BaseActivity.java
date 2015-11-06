package com.github.fabtransitionactivity.demo;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

/**
 * Created by Cristopher on 11/6/15.
 */
public class BaseActivity extends AppCompatActivity {

    protected void setUpToolbarWithTitle(String title, boolean hasBackButton){
        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(toolBar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
            getSupportActionBar().setDisplayShowHomeEnabled(hasBackButton);
            getSupportActionBar().setDisplayHomeAsUpEnabled(hasBackButton);
        }
    }

    protected void enterFromBottomAnimation(){
        overridePendingTransition(R.anim.activity_open_translate_from_bottom, R.anim.activity_no_animation);
    }

    protected void exitToBottomAnimation(){
        overridePendingTransition(R.anim.activity_no_animation, R.anim.activity_close_translate_to_bottom);
    }
}

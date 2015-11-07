package com.github.fabtransitionactivity.demo;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import butterknife.ButterKnife;


public class AfterFabAnimationActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        enterFromBottomAnimation();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_fab_animation);
        ButterKnife.bind(this);
        setUpToolbarWithTitle(getString(R.string.COMPOSE), true);
    }

    @Override
    protected void onPause() {
        exitToBottomAnimation();
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_send, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_send:
                Toast.makeText(getApplicationContext(), "Yeah!", Toast.LENGTH_SHORT).show();
                break;
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

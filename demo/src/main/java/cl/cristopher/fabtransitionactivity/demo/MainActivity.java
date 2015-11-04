package cl.cristopher.fabtransitionactivity.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;


import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cl.cristopher.fabtransitionactivity.BottomSheetLayout;


public class MainActivity extends AppCompatActivity implements BottomSheetLayout.OnFabAnimationEndListener {

    @Bind(R.id.bottom_sheet)
    BottomSheetLayout mBottomSheetLayout;

    @Bind(R.id.fab)
    FloatingActionButton mFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mBottomSheetLayout.setFab(mFab);
        mBottomSheetLayout.setFabAnimationEndListener(this);
    }

    @OnClick(R.id.fab)
    void onFabClick() {
        mBottomSheetLayout.expandFab();
    }

    @Override
    public void onFabAnimationEnd() {
        Intent intent = new Intent(this, AfterFabAnimationActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStop (){
        mBottomSheetLayout.contractFab();
        super.onStop();
    }
}

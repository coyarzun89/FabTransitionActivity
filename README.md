# FabTransitionActivity
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-FabTransitionActivity-green.svg?style=true)](https://android-arsenal.com/details/1/2763)

It is based on [FabTransitionLayout](https://github.com/bowyer-app/FabTransitionLayout)

![transitionactivity](https://github.com/coyarzun89/FabTransitionActivity/blob/master/art/fabTransitionActivity.gif)

Usage
====
### build.gradle

```
repositories {
    mavenCentral()
}

defaultConfig {
    minSdkVersion 14
}

dependencies {
    compile 'com.github.coyarzun89:fabtransitionactivity:0.1.1'
}


```

### Layout XML
```
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent">
    
    <android.support.v7.widget.Toolbar
        android:id="@+id/toolbar_actionbar"
        xmlns:app="http://schemas.android.com/apk/res-auto"
        style="@style/ToolBarStyle"
        xmlns:android="http://schemas.android.com/apk/res/android"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:background="@color/primary"
        app:titleTextAppearance="@style/ToolbarTitle"
        android:minHeight="56dp"
        android:paddingLeft="36dp"
        android:elevation="2dp" />
        
    <ListView
        android:id="@+id/list_mails"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:layout_below="@id/toolbar_actionbar"/>

    <android.support.design.widget.FloatingActionButton
        android:id="@+id/fab"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_alignParentBottom="true"
        android:layout_alignParentRight="true"
        android:layout_marginBottom="16dp"
        android:layout_marginRight="16dp"
        android:src="@drawable/ic_edit_white_24dp"
        app:borderWidth="0dp"
        app:fabSize="normal"
        app:rippleColor="@color/primary"/>

    <com.github.fabtransitionactivity.SheetLayout
        android:id="@+id/bottom_sheet"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:layout_gravity="bottom"
        app:ft_container_gravity="center"
        app:ft_color="@color/primary"
        android:elevation="2dp"/>

</RelativeLayout>

```

### Set up

```java
public class MainActivity extends BaseActivity implements SheetLayout.OnFabAnimationEndListener {

    @Bind(R.id.bottom_sheet) SheetLayout mSheetLayout;
    @Bind(R.id.fab) FloatingActionButton mFab;
    
    private static final int REQUEST_CODE = 1;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mSheetLayout.setFab(mFab);
        mSheetLayout.setFabAnimationEndListener(this);
    }
    
    @OnClick(R.id.fab)
    void onFabClick() {
        mSheetLayout.expandFab();
    }

    @Override
    public void onFabAnimationEnd() {
        Intent intent = new Intent(this, AfterFabAnimationActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

   @Override
   public void onActivityResult(int requestCode, int resultCode, Intent data) {
       super.onActivityResult(requestCode, resultCode, data);
       if(requestCode == REQUEST_CODE){
           mSheetLayout.contractFab();
       }
   }
```

# Credits
This library use following libraries.
* [CircularReveal](https://github.com/ozodrukh/CircularReveal)

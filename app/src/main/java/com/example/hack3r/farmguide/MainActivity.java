package com.example.hack3r.farmguide;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.hack3r.farmguide.fragments.CalculateFragment;
import com.example.hack3r.farmguide.fragments.CropInfo;
import com.example.hack3r.farmguide.fragments.MainFragment;

public class MainActivity extends AppCompatActivity{
    AppBarLayout appBarLayout;
    Toolbar toolbar;
    boolean b = true;
    MainFragment mainFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        appBarLayout = findViewById(R.id.appbar);

        mainFragment = new MainFragment();
        loadFragment(mainFragment);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    showMenu(true);
                    loadFragment(mainFragment);
                    return true;
                case R.id.navigation_dashboard:
                    appBarLayout.setExpanded(false);
                    showMenu(false);
                    CalculateFragment calculate = new CalculateFragment();
                    loadFragment(calculate);
                    return true;
                case R.id.navigation_notifications:
                    appBarLayout.setExpanded(false);
                    showMenu(false);
                    CropInfo info = new CropInfo();
                    loadFragment(info);
                    return true;
            }
            return false;
        }
    };

    public void loadFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(this.b) {
            getMenuInflater().inflate(R.menu.menu_main, menu);
            return true;
        }else {
            return false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.changeDate:
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    void showMenu(boolean bool){
        if (bool){
            this.b = true;
            invalidateOptionsMenu();
        }else {
            this.b = false;
            invalidateOptionsMenu();
        }
    }
    //action when you set a date

}

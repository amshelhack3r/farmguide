package com.example.hack3r.farmguide;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
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
                    loadFragment(mainFragment);
                    return true;
                case R.id.navigation_dashboard:
                    appBarLayout.setExpanded(false);
                    CalculateFragment calculate = new CalculateFragment();
                    loadFragment(calculate);
                    return true;
                case R.id.navigation_notifications:
                    appBarLayout.setExpanded(false);
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
            getMenuInflater().inflate(R.menu.menu_main, menu);
            return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.changeDate:
                showNotification();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    void showNotification(){
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "notify")
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("PLANT PLANT NOW")
                .setContentText("Get into the app to know more")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(1, mBuilder.build());
    }
}

package com.tuinite.timemanagement;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;


public class MainActivity extends AppCompatActivity {
    private LinearLayout linearLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        linearLayout = findViewById(R.id.splashScreen);
        TextView appName = findViewById(R.id.textView10);
        TextView moto = findViewById(R.id.textView12);
        ImageView imageView = findViewById(R.id.imageView5);

        SharedPreferences sharedPreferences = this.getSharedPreferences(
                "sharedPrefs", MODE_PRIVATE);
        final SharedPreferences.Editor editor
                = sharedPreferences.edit();
        final boolean isDarkModeOn
                = sharedPreferences
                .getBoolean(
                        "isDarkModeOn", false);

        // When user reopens the app
        // after applying dark/light mode
        if (isDarkModeOn) {
            AppCompatDelegate
                    .setDefaultNightMode(
                            AppCompatDelegate
                                    .MODE_NIGHT_YES);

        } else {
            AppCompatDelegate
                    .setDefaultNightMode(
                            AppCompatDelegate
                                    .MODE_NIGHT_NO);

        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#121212"));
        }
        imageView.animate().translationY(0).alpha(1).setDuration(1000).setListener(new AnimatorListenerAdapter() {


            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                appName.animate().alpha(1).setDuration(500);
                moto.animate().alpha(1).translationX(0).setDuration(500);
            }
        });


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(MainActivity.this,
                        Second.class);
                //Intent is used to switch from one activity to another.

                startActivity(i);
                //invoke the SecondActivity.

                finish();
                //the current activity will get finished.
            }
        }, 2000);

      /*  Set<String> listnerSet = NotificationManagerCompat.getEnabledListenerPackages(this);
        boolean haveAccess = false;
        for (String sd : listnerSet) {
            if (sd.equals("com.tuinite.timemanagement")) {
                haveAccess = true;
            }else{
                startActivity(new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS));
            }
        }*/


        //    if (!haveAccess) {

        //    }


     /*   bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);



        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnNavigationItemSelectedListener((BottomNavigationView.OnNavigationItemSelectedListener) this);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();


      

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        selectedFragment=null;

        switch (item.getItemId()){
            case R.id.home:
                selectedFragment=new HomeFragment();
                break;
            case R.id.routine:
                selectedFragment=new RoutineFragment();
                break;

            case R.id.event:
                selectedFragment=new ReminderFragment();
                break;
            case R.id.profile:
                selectedFragment=new MenuFragment();
                break;


        }
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();


        return true;*/
    }
}



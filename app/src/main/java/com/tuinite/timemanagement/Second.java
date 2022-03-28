package com.tuinite.timemanagement;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.tuinite.timemanagement.fragments.MenuFragment;
import com.tuinite.timemanagement.fragments.ReminderFragment;
import com.tuinite.timemanagement.fragments.RoutineFragment;

public class Second extends AppCompatActivity {
    static Ringtone ringtone;
    private Fragment selectedFragment;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        ringtone = RingtoneManager.getRingtone(this, notification);


        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ReminderFragment()).commit();
        MeowBottomNavigation meowBottomNavigation = findViewById(R.id.bottom_navigation);

        meowBottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.ic_notifications_icon));
        meowBottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.clock_icon));
        meowBottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.ic_menu_icon));

        //


        meowBottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {
                selectedFragment = null;

                switch (item.getId()) {
                    case 1:
                        selectedFragment = new ReminderFragment();
                        break;
                    case 2:
                        selectedFragment = new RoutineFragment();
                        break;

                    case 3:
                        selectedFragment = new MenuFragment();
                        break;

                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            }
        });


        meowBottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
                String name;

                switch (item.getId()) {
                    case 1:
                        name = "1";
                        break;

                    case 2:
                        name = "2";
                        break;
                    case 3:
                        name = "3";
                        break;

                    case 4:
                        name = "4";
                        break;

                    default:
                        name = "";

                }


            }
        });

        meowBottomNavigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {
                selectedFragment = null;

                switch (item.getId()) {
                    case 1:
                        selectedFragment = new ReminderFragment();
                        break;
                    case 2:
                        selectedFragment = new RoutineFragment();
                        break;

                    case 3:
                        selectedFragment = new MenuFragment();
                        break;


                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            }
        });
        meowBottomNavigation.show(1, true);


    }

}

package com.anupom.covidinfo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.anupom.covidinfo.ui.main.SectionsPagerAdapter;

public class MainActivity extends AppCompatActivity {

    private ImageView infoIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);

        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setSelectedTabIndicatorColor(getResources().getColor(R.color.tab_colour));
        tabs.setupWithViewPager(viewPager);

        //----------------------------------------------------------
        infoIcon = findViewById(R.id.infoIcon);
        infoIcon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                /*PopupMenu popupMenu = new PopupMenu(MainActivity.this, infoIcon);
                popupMenu.getMenuInflater().inflate(R.menu.menu, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Toast.makeText(MainActivity.this,"You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });
                popupMenu.show();*/
                //registering popup with OnMenuItemClickListener

                Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
                intent.putExtra("info_click", "info_click");
                startActivity(intent);
            }
        });

        //-----------------------------------------------------------
    }
}
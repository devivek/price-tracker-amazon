package com.devivek.price_tracker_amazon;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.devivek.price_tracker_amazon.utilities.Background;
import com.devivek.price_tracker_amazon.utilities.FragmentPagerAdapter;
import com.devivek.price_tracker_amazon.utilities.ItemDatabase;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static ViewPager viewPager;

    public static void SetViewPager(String url) {
        viewPager.setCurrentItem(0);
        WebViewFragment.SetWebView(url);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //start service
        this.startService(new Intent(this, Background.class));

        Log.e(TAG, "onCreate: Started");

        //Database
        ItemDatabase itemDatabase = ItemDatabase.getInstance(this);

        //Declarations
        viewPager = findViewById(R.id.ViewPager);
        TabLayout tabLayout = findViewById(R.id.Tabs);

        FragmentPagerAdapter fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager());
        fragmentPagerAdapter.addFragment(new ListViewFragment()); //Index 0
        fragmentPagerAdapter.addFragment(new WebViewFragment()); //Index 1

        viewPager.setAdapter(fragmentPagerAdapter);

        //Tabs
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.white));
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_round_format_list_bulleted_24);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_round_add_circle_24);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.startService(new Intent(this, Background.class));
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.startService(new Intent(this, Background.class));
    }
}

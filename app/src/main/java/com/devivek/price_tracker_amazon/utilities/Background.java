package com.devivek.price_tracker_amazon.utilities;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import com.devivek.price_tracker_amazon.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.ContentValues.TAG;

public class Background extends Service {

    public static final int notify = 1800000;  //interval between two services(Here Service run every 30 Minute)
    private Handler mHandler = new Handler();
    private Timer mTimer = null;
    private ItemViewModel itemViewModel;
    private Notifications notifications;

    //Query
    private final String[] QUERY = {"#priceblock_dealprice", "#priceblock_saleprice", "#price_inside_buybox", "#priceblock_ourprice"};

    //List
    List<Item> itemsLive;

    //Var
    String priceString;
    int DurationSelected;

    private SharedPreferences sharedPreferences;


    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        //SharedPref
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        DurationSelected = sharedPreferences.getInt(getString(R.string.DurationTime),30);
        if (mTimer != null) // Cancel if already existed
            mTimer.cancel();
        else
            mTimer = new Timer();   //recreate new
        mTimer.scheduleAtFixedRate(new TimeDisplay(), 0, 60000*DurationSelected);   //Schedule task

        itemsLive = new ArrayList<>();
        itemViewModel = new ItemViewModel(getApplication());
        itemViewModel.getAllItem().observeForever(items -> itemsLive = items);
        notifications = new Notifications(getApplication());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mTimer.cancel();    //For Cancel Timer
        Log.e(TAG, "Service: Stopped");
    }

    //class TimeDisplay for handling task
    class TimeDisplay extends TimerTask {
        @Override
        public void run() {
            // run on another thread
            mHandler.post(() -> {
                Log.e(TAG, "Service: Running");
                Log.e(TAG, "run: Duration: "+DurationSelected);
                    for(Item item : itemsLive){
                        Log.e(TAG, "onClick: Item name: "+item.getName());
                        new CheckForPrices().BackgroundWork(item.getUrl(), item);
                    }
            });
        }
    }


    class CheckForPrices{
        void BackgroundWork(final String currentPage, final Item item){
            new Thread(() -> {

                try {
                    Document document;
                    document = Jsoup.connect(currentPage).get();


                    for( String PRICE_QUERY : QUERY) {
                        String Price = String.valueOf(document.select(PRICE_QUERY).text());

                        if(currentPage .contains("www.amazon.in")){
                            priceString = Price.trim();
                            priceString = "INR "+priceString;
                        }else {
                            priceString = Price;
                        }
                        int priceInt = 0;
                        try {

                            priceInt = Integer.parseInt(Price.replaceAll("\\D+","").trim());

                        } catch (Exception e) {
                            Log.e(TAG, "getPrice: Error: " + e.getMessage());
                        }

                        if (priceInt > 0) {
                            Log.e(TAG, "getPrice: title: " + item.getName());
                            Log.e(TAG, "getPrice: URL: " + currentPage);
                            Log.e(TAG, "getPrice: Price String: " + priceString);
                            Log.e(TAG, "getPrice: Price Int: " + priceInt);
                            if(priceInt != item.getCurrent_price_int()){
                                item.setCurrent_price_string(priceString);
                                item.setCurrent_price_int(priceInt);
                                itemViewModel.updateItem(item);
                                notifications.sendNotification(item.getName());
                            }
                            break;
                        }
                    }
                } catch (IOException e) {
                    Log.e(TAG, "getPrice: Error: "+e.getMessage());
                }

            }).start();
        }
    }
}
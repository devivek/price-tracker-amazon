package com.devivek.price_tracker_amazon.utilities;

import android.app.Activity;
import android.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

import com.devivek.price_tracker_amazon.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class OnlineWork {

    //Const
    private static final String TAG = "OnlineWork";
    private Activity context;
    private String priceString;

    //Query
    private final String[] QUERY = {"#priceblock_dealprice", "#priceblock_saleprice", "#price_inside_buybox", "#priceblock_ourprice"};

    //Classes
    private ItemViewModel itemViewModel;
    private Notifications notifications;

    //Progress dialogue
    private AlertDialog AddItemDialog;
    private AlertDialog UpdateItemDialog;

    private boolean Exists;

    private List<Item> itemlist;


    public OnlineWork(Activity context) {
        this.context = context;

        itemViewModel = ViewModelProviders.of((FragmentActivity) context).get(ItemViewModel.class);
        notifications = new Notifications(context);

        AddItemDialog = new  SpotsDialog.Builder()
                .setContext(context)
                .setMessage("Adding the Product...")
                .setTheme(R.style.Custom)
                .build();
        UpdateItemDialog = new  SpotsDialog.Builder()
                .setContext(context)
                .setMessage("Updating Products....")
                .setTheme(R.style.Custom)
                .build();

        itemViewModel.getAllItem().observeForever(items -> itemlist = items);
    }


    public void getPrice(final String currentPage) {

        AddItemDialog.show();
        new Thread(() -> {

            try {

                Document document;
                document = Jsoup.connect(currentPage).get();


                String title = String.valueOf(document.select("#productTitle").html().replace("&quot;"," inch").trim());
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

//                        priceInt = Integer.parseInt(Price.replace("EUR", "").replaceAll("\\D+","").replace("£","").replace("&#xFFE5;", "").replace("TL", "").replace(",", "").replace("$", "").replace("R", "").replace("CDN", "").replace("￥", "").replace(".", "").replace("&#xA0;", "").trim());

                    } catch (Exception e) {
                        Log.e(TAG, "getPrice: Error: " + e.getMessage());
                    }

                    if (priceInt > 0) {
                        Log.e(TAG, "getPrice: title: " + title);
                        Log.e(TAG, "getPrice: URL: " + currentPage);
                        Log.e(TAG, "getPrice: Price String: " + priceString);
                        Log.e(TAG, "getPrice: Price Int: " + priceInt);

                        Item item = new Item();
                        item.setName(title);
                        item.setUrl(currentPage);
                        item.setOriginal_price_string(priceString);
                        item.setOriginal_price_int(priceInt);
                        item.setCurrent_price_string(priceString);
                        item.setCurrent_price_int(priceInt);

                        if(!CheckifExisits(item)){
                            itemViewModel.addItem(item);
                            break;
                        }else{
                            Log.e(TAG, "getPrice: Item Already Exists");
                            context.runOnUiThread(() -> Toast.makeText(context, "Item is already added", Toast.LENGTH_LONG).show());
                            break;
                        }
                    }
                }
                AddItemDialog.dismiss();


            } catch (IOException e) {
                Log.e(TAG, "getPrice: Error: "+e.getMessage());
            }

        }).start();
    }

    public void updatePrice(final String currentPage, final Item item) {

        UpdateItemDialog.show();
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
                UpdateItemDialog.dismiss();
            } catch (IOException e) {
                Log.e(TAG, "getPrice: Error: "+e.getMessage());
            }

        }).start();
    }

    private boolean CheckifExisits(Item itemOutSide){
        Log.e(TAG, "CheckifExisits: Method triggered");
        Exists = false;
        int i =0;
            for (Item itemToCheck: itemlist) {
                    Log.e(TAG, "CheckifExisits: Loop number:"+i);
                    Log.e(TAG, "CheckifExisits: Current loop item name: "+itemToCheck.getName());
                    if (itemOutSide.getName().equals(itemToCheck.getName())) {
                        Exists = true;
                        Log.e(TAG, "CheckifExisits: Item Already Exists");
                        break;
                    }
                    i++;
                }
        return Exists;
    }
}

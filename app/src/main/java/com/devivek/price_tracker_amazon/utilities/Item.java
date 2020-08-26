package com.devivek.price_tracker_amazon.utilities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Item {
    @NonNull
    @PrimaryKey
    private String url;

    private String name;

    private int Original_price_int;

    private String Original_price_string;
    private int Current_price_int;
    private String Current_price_string;

    public Item(){}

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getOriginal_price_int() {
        return Original_price_int;
    }

    public void setOriginal_price_int(int original_price_int) {
        Original_price_int = original_price_int;
    }

    public String getOriginal_price_string() {
        return Original_price_string;
    }

    public void setOriginal_price_string(String original_price_string) {
        Original_price_string = original_price_string;
    }

    public int getCurrent_price_int() {
        return Current_price_int;
    }

    public void setCurrent_price_int(int current_price_int) {
        Current_price_int = current_price_int;
    }

    public String getCurrent_price_string() {
        return Current_price_string;
    }

    public void setCurrent_price_string(String current_price_string) {
        Current_price_string = current_price_string;
    }
}

package com.devivek.price_tracker_amazon.utilities;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ItemDAO {

    @Insert
    public void addItem(Item item);

    @Update
    public void updateItem(Item item);

    @Delete
    public void deleteItem(Item item);

    @Query("SELECT * FROM Item")
    LiveData<List<Item>> getAllItemsLive();

    @Query("SELECT * FROM Item")
    List<Item> getAllItems();

    @Query("DELETE FROM Item")
    public void nukeTable();
}

package com.devivek.price_tracker_amazon.utilities;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ItemViewModel extends AndroidViewModel {
    private ItemRepo itemRepo;
    private LiveData<List<Item>> itemsLive;
    private List<Item> itemsList;

    public ItemViewModel(@NonNull Application application) {
        super(application);
        itemRepo = new ItemRepo(application);
        itemsLive = itemRepo.getAllItem();
        itemsList = itemRepo.itemsList();
    }

    public void addItem(Item item){
        itemRepo.addItem(item);
    }

    public void updateItem(Item item){
        itemRepo.updateItem(item);
    }

    public void deleteItem(Item item){
        itemRepo.deleteItem(item);
    }

    public void deleteAllItems(){
        itemRepo.deleteAllItems();
    }

    public List<Item> itemsList(){
        return itemsList;
    }

    public LiveData<List<Item>> getAllItem(){
        return itemsLive;
    }
}

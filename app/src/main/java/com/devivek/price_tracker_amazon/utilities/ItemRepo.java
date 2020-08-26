package com.devivek.price_tracker_amazon.utilities;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class ItemRepo {
    private ItemDAO itemDAO;
    private LiveData<List<Item>> itemsLive;
    private List<Item> items;

    public ItemRepo (Application application){
        ItemDatabase itemDatabase = ItemDatabase.getInstance(application);
        itemDAO = itemDatabase.userDao();
        itemsLive = itemDAO.getAllItemsLive();
        items = itemDAO.getAllItems();
    }

    public void addItem(Item item){
        new InsertNoteAsyncTask(itemDAO).execute(item);
    }

    public void updateItem(Item item){
        new UpdateNoteAsyncTask(itemDAO).execute(item);
    }

    public void deleteItem(Item item){
        new DeleteNoteAsyncTask(itemDAO).execute(item);
    }

    public void deleteAllItems(){
        new DeleteAllNotesAsyncTask(itemDAO).execute();
    }

    public LiveData<List<Item>> getAllItem(){
        return itemsLive;
    }

    public List<Item> itemsList(){
        return items;
    }

    private static class InsertNoteAsyncTask extends AsyncTask<Item, Void, Void> {
        private ItemDAO itemDAO;

        private InsertNoteAsyncTask(ItemDAO itemDAO) {
            this.itemDAO = itemDAO;
        }


        @Override
        protected Void doInBackground(Item... items) {
            itemDAO.addItem(items[0]);
            return null;
        }
    }

    private static class UpdateNoteAsyncTask extends AsyncTask<Item, Void, Void> {
        private ItemDAO itemDAO;

        private UpdateNoteAsyncTask(ItemDAO itemDAO) {
            this.itemDAO = itemDAO;
        }


        @Override
        protected Void doInBackground(Item... items) {
            itemDAO.updateItem(items[0]);
            return null;
        }
    }

    private static class DeleteNoteAsyncTask extends AsyncTask<Item, Void, Void> {
        private ItemDAO itemDAO;

        private DeleteNoteAsyncTask(ItemDAO itemDAO) {
            this.itemDAO = itemDAO;
        }


        @Override
        protected Void doInBackground(Item... items) {
            itemDAO.deleteItem(items[0]);
            return null;
        }
    }

    private static class DeleteAllNotesAsyncTask extends AsyncTask<Item, Void, Void> {
        private ItemDAO itemDAO;

        private DeleteAllNotesAsyncTask(ItemDAO itemDAO) {
            this.itemDAO = itemDAO;
        }


        @Override
        protected Void doInBackground(Item... items) {
            itemDAO.nukeTable();
            return null;
        }
    }
}

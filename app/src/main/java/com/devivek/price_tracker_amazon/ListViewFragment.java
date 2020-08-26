package com.devivek.price_tracker_amazon;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devivek.price_tracker_amazon.utilities.Item;
import com.devivek.price_tracker_amazon.utilities.ItemAdapter;
import com.devivek.price_tracker_amazon.utilities.ItemViewModel;

import java.util.List;

public class ListViewFragment extends Fragment {

    //Const
    private static final String TAG = "ListViewFragment";
    private Context context = getActivity();
    private ItemViewModel itemViewModel;

    private ItemAdapter itemAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.listview_fragment, container, false);
        Log.e(TAG, "onCreateView: Started");
        //ViewModel
        //Classes
        itemViewModel = ViewModelProviders.of(this).get(ItemViewModel.class);
        itemViewModel.getAllItem().observe(this, items1 -> itemAdapter.setList(items1));

 /*       ImageView refresh = view.findViewById(R.id.refresh);
        ImageView removeall = view.findViewById(R.id.removeall);*/
        RecyclerView recyclerView = view.findViewById(R.id.RecyclerView);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        final List<Item> items = itemViewModel.itemsList();
        itemAdapter = new ItemAdapter(getActivity());
        recyclerView.setAdapter(itemAdapter);

        ImageView settings = view.findViewById(R.id.settings);
        settings.setOnClickListener(view13 -> {
            Intent intent = new Intent(getActivity(), SettingsActivity.class);
            startActivity(intent);
        });



        /*removeall.setOnClickListener(view1 -> itemViewModel.deleteAllItems());*/

   /*     refresh.setOnClickListener(view12 -> {
            OnlineWork onlineWork = new OnlineWork(getActivity());
           for(Item item : items){
               Log.e(TAG, "onClick: URL: "+item.getUrl());
                onlineWork.updatePrice(item.getUrl(),item);
            }
        });*/

        return view;
    }
}

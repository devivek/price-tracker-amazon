package com.devivek.price_tracker_amazon.utilities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.devivek.price_tracker_amazon.MainActivity;
import com.devivek.price_tracker_amazon.R;

import java.util.ArrayList;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {


    private Context context;
    private List<Item> items = new ArrayList<>();
    private ItemViewModel itemViewModel;

    public void setList(List<Item> items){
        this.items = items;
        notifyDataSetChanged();
    }

    public ItemAdapter(Context context) {
        this.context = context;
        itemViewModel = ViewModelProviders.of((FragmentActivity) context).get(ItemViewModel.class);
    }



    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_layout, null);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        final Item item = items.get(position);

        holder.title.setText(item.getName());
        holder.newPrice.setText(item.getCurrent_price_string());
        holder.originalPrice.setText(item.getOriginal_price_string());

        holder.itemView.setOnClickListener(view -> MainActivity.SetViewPager(item.getUrl()));
        holder.removeProduct.setOnClickListener(view -> itemViewModel.deleteItem(item));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder{

        TextView title, originalPrice, newPrice;
        ImageView removeProduct;

        ItemViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.textViewTitle);
            originalPrice = itemView.findViewById(R.id.textViewOriginalPrice);
            newPrice = itemView.findViewById(R.id.textViewNewPrice);
            removeProduct = itemView.findViewById(R.id.RemoveOneProduct);

        }
    }
}

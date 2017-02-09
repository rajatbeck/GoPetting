package com.rajat.gopetting;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;


/**
 * Created by priyanka on 2/9/2017.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ItemViewHolder> {

    private List<Product> mProductList;
    private ImageLoader mImageLoader;
    private Context context;
    private SparseBooleanArray sparseBooleanArray = new SparseBooleanArray();
    private ONClickButton onClickButton;

    RecyclerViewAdapter(Context context, List<Product> mProductList, ONClickButton onClickButton) {
        this.context = context;
        this.mProductList = mProductList;
        this.onClickButton = onClickButton;
        this.mImageLoader = MySingleton.getInstance(context).getImageLoader();
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        Product products = mProductList.get(position);
        holder.networkImageView.setImageUrl(products.getImage(), mImageLoader);
        holder.name.setText(products.getName());
        holder.startTime.setText(products.getStartdate());
        holder.endTime.setText(products.getEndDate());
        if (sparseBooleanArray.get(position, false)) {
            holder.btnAdd.setText("Remove");
        } else {
            holder.btnAdd.setText("Add");
        }
    }

    @Override
    public int getItemCount() {
        return mProductList.size();
    }

    public void toggleSelection(int pos) {
        if (sparseBooleanArray.get(pos, false)) {
            sparseBooleanArray.delete(pos);
            if (onClickButton != null)
                onClickButton.action(mProductList.get(pos), false);
        } else {
            sparseBooleanArray.put(pos, true);
            if (onClickButton != null)
                onClickButton.action(mProductList.get(pos), true);
        }
        notifyItemChanged(pos);
    }

    public void setSelection(int pos) {
        if (sparseBooleanArray.get(pos, false)) {
            sparseBooleanArray.delete(pos);
        } else {
            sparseBooleanArray.put(pos, true);

        }
    }


    public void getItemClicked(int position) {
        toggleSelection(position);
    }

    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private static final String TAG = "ITEM_VIEW_HOLDER";
        NetworkImageView networkImageView;
        TextView name;
        TextView startTime;
        TextView endTime;
        Button btnAdd;

        public ItemViewHolder(View itemView) {
            super(itemView);
            networkImageView = (NetworkImageView) itemView.findViewById(R.id.network_image);
            name = (TextView) itemView.findViewById(R.id.name);
            startTime = (TextView) itemView.findViewById(R.id.start_time);
            endTime = (TextView) itemView.findViewById(R.id.end_time);
            btnAdd = (Button) itemView.findViewById(R.id.btn_add);
            btnAdd.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.btn_add) {
                Log.d("check", String.valueOf(btnAdd.getText()));
                getItemClicked(getAdapterPosition());
            }
        }
    }

    interface ONClickButton {
        Void action(Product product, boolean add);
    }
}

package com.rajat.gopetting;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by rajatbeck on 2/9/2017.
 */

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<Product> mProductList;
    private OnDeleteClicked onDeleteClicked;

    CartAdapter(List<Product> mProductList, OnDeleteClicked onDeleteClicked) {
        this.mProductList = mProductList;
        this.onDeleteClicked = onDeleteClicked;
    }

    @Override
    public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CartViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_row_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(CartViewHolder holder, int position) {
        Product product = mProductList.get(position);
        holder.name.setText(product.getName());
    }

    @Override
    public int getItemCount() {
        return mProductList.size();
    }

    private void deleteAtPosition(int pos) {
        if (onDeleteClicked != null) {
            if (pos != -1)
                onDeleteClicked.onDelete(mProductList.get(pos), pos);
        }
    }

    public void removeAt(int position) {
        if (mProductList.size() != 0) {
            mProductList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, mProductList.size());
        }
    }

    class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView name;
        ImageView deleteButton;

        public CartViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.cart_text);
            deleteButton = (ImageView) itemView.findViewById(R.id.delete_button);
            deleteButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.delete_button) {
                deleteAtPosition(getAdapterPosition());
            }
        }
    }

    interface OnDeleteClicked {
        Void onDelete(Product product, int pos);
    }
}

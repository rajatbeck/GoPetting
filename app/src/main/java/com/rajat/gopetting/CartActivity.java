package com.rajat.gopetting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

/**
 * Created by rajatbeck on 2/9/2017.
 */

public class CartActivity extends AppCompatActivity implements CartAdapter.OnDeleteClicked {

    private RecyclerView mRecyclerView;
    private MyDatabaseAdapter myDatabaseAdapter;
    private CartAdapter cartAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_layout);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_final_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        myDatabaseAdapter = new MyDatabaseAdapter(this);
        List<Product> mProductList = myDatabaseAdapter.getItems();
        if (mProductList.size() != 0) {
            cartAdapter = new CartAdapter(mProductList, CartActivity.this);
            mRecyclerView.setAdapter(cartAdapter);
        }
    }

    @Override
    public Void onDelete(Product product, int pos) {
        myDatabaseAdapter.deleteProduct(product.getUrl());
        cartAdapter.removeAt(pos);

        return null;
    }
}

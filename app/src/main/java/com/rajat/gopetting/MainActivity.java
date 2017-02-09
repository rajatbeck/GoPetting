package com.rajat.gopetting;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.LayerDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ResponseInterface, RecyclerViewAdapter.ONClickButton {

    private static final String TAG = "MAIN_ACTIVITY";
    private static int mNotificationsCount = 0;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private List<Product> mProductList;
    private RecyclerViewAdapter mRecyclerAdapter;
    private MyDatabaseAdapter myDatabaseAdapter;
    private int FLAG_CHECK = 0;
    private MyPrefernces myPrefernces;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        myDatabaseAdapter = new MyDatabaseAdapter(getApplicationContext());
        /*SQLiteDatabase sqLiteDatabase = myDatabaseAdapter.productDatabase.getWritableDatabase();
        myDatabaseAdapter.productDatabase.onCreate(sqLiteDatabase);*/
        myPrefernces = new MyPrefernces(this);
        initailze();

    }

    void initailze() {
        List<Product> prevProductList = myDatabaseAdapter.getItems();
        if (prevProductList.size() > 0) {
            FLAG_CHECK = 1;
            mNotificationsCount = prevProductList.size();
            updateNotificationsBadge();
        } else {
            mNotificationsCount = 0;
            updateNotificationsBadge();
        }
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mProgressBar = (ProgressBar) findViewById(R.id.loading);
        new APIManager(this, MainActivity.this).callAPI();
        mProductList = new ArrayList<>();
        mRecyclerAdapter = new RecyclerViewAdapter(this, mProductList, MainActivity.this);
        mRecyclerView.setAdapter(mRecyclerAdapter);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public Void parseResponse(String response) {
        mProgressBar.setVisibility(View.GONE);
        Log.d(TAG, response);
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (myPrefernces.getTotal() == Integer.parseInt(jsonObject.getString("total"))) {
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    Product products = new Product();
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    products.setImage(jsonObject1.getString("icon"));
                    products.setName(jsonObject1.getString("name"));
                    products.setStartdate(jsonObject1.getString("startDate"));
                    products.setEndDate(jsonObject1.getString("endDate"));
                    String removeSlashes = jsonObject1.getString("url").replaceAll("\\/", "");
                    products.setUrl(removeSlashes);
                    if (FLAG_CHECK == 1) {
                        if (myDatabaseAdapter.findProduct(removeSlashes)) {
                            mRecyclerAdapter.setSelection(i);
                        }
                    }
                    mProductList.add(products);
                }
                mRecyclerAdapter.notifyDataSetChanged();
            } else {
                myPrefernces.addTotal(Integer.parseInt(jsonObject.getString("total")));
                myDatabaseAdapter.dropTable();
                mNotificationsCount = 0;
                updateNotificationsBadge();
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    Product products = new Product();
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    products.setImage(jsonObject1.getString("icon"));
                    products.setName(jsonObject1.getString("name"));
                    products.setStartdate(jsonObject1.getString("startDate"));
                    products.setEndDate(jsonObject1.getString("endDate"));
                    String removeSlashes = jsonObject1.getString("url").replaceAll("\\/", "");
                    products.setUrl(removeSlashes);
                 /*   if (FLAG_CHECK == 1) {
                        if (myDatabaseAdapter.findProduct(removeSlashes)) {
                            mRecyclerAdapter.setSelection(i);
                        }
                    }*/
                    mProductList.add(products);
                }
                mRecyclerAdapter.notifyDataSetChanged();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        MenuItem item = menu.findItem(R.id.action_notifications);
//        MenuItem item1 = menu.findItem(R.id.logout);

        LayerDrawable icon = (LayerDrawable) item.getIcon();

        // Update LayerDrawable's BadgeDrawable
        Utils.setBadgeCount(this, icon, mNotificationsCount);

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_notifications) {
            startActivity(new Intent(this, CartActivity.class));

        }
        if (id == R.id.logout) {

        }
        return super.onOptionsItemSelected(item);
    }

    public void updateNotificationsBadge() {
//        mNotificationsCount++;

        invalidateOptionsMenu();
    }

    @Override
    public Void action(Product product, boolean add) {
        if (add) {
            long check = myDatabaseAdapter.insertData(product.getUrl(), product.getName());
            if (check < 0) {
                Log.d(TAG, "unsucess");
            } else {
                Log.d(TAG, "success");
            }
            mNotificationsCount++;
            updateNotificationsBadge();
        } else {
            myDatabaseAdapter.deleteProduct(product.getUrl());
            mNotificationsCount--;
            updateNotificationsBadge();
        }

        return null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "on Destroy called");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "on Restart called");
        initailze();
    }
}

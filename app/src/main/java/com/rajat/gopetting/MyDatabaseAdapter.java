package com.rajat.gopetting;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by rajatbeck on 2/9/2017.
 */

public class MyDatabaseAdapter {
    public ProductDatabase productDatabase;

    MyDatabaseAdapter(Context context) {
        productDatabase = new ProductDatabase(context);
        productDatabase.getWritableDatabase();
    }

    public void dropTable() {
        SQLiteDatabase sqLiteDatabase = productDatabase.getWritableDatabase();
        String sql = "delete from " + productDatabase.TABLE_NAME;
        sqLiteDatabase.execSQL(sql);
    }

    public long insertData(String id, String name) {
        SQLiteDatabase db = productDatabase.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(productDatabase.COLUMN_ID, id);
        contentValues.put(productDatabase.COLUMN_NAME, name);
        long row = db.insert(productDatabase.TABLE_NAME, null, contentValues);
        return row;
    }

    public ArrayList<Product> getItems() {
        SQLiteDatabase sqLiteDatabase = productDatabase.getWritableDatabase();
        ArrayList<Product> productList = new ArrayList<>();
        String[] columns = {productDatabase.COLUMN_NAME, productDatabase.COLUMN_ID};
        Cursor cursor = sqLiteDatabase.query(productDatabase.TABLE_NAME, columns, null, null, null, null, null);
        while (cursor.moveToNext()) {
            Product product = new Product();
            product.setName(cursor.getString(0));
            product.setUrl(cursor.getString(1));
            productList.add(product);
        }
        cursor.close();
        return productList;
    }

    public void deleteProduct(String iD) {
        SQLiteDatabase sqLiteDatabase = productDatabase.getWritableDatabase();
        sqLiteDatabase.delete(productDatabase.TABLE_NAME, productDatabase.COLUMN_ID + " = ?", new String[]{iD});

    }

    public boolean findProduct(String iD) {
        SQLiteDatabase sqLiteDatabase = productDatabase.getWritableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery("select 1 from " + productDatabase.TABLE_NAME + " where" + productDatabase.COLUMN_ID + " = ?",
                new String[]{iD});
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }
}
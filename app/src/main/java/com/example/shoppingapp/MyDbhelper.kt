package com.example.shoppingapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper


class MyDbhelper(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    // create table sql query
    private val CREATE_PRODUCT_TABLE = ("CREATE TABLE " + TABLE_PRODUCT + "("
            + COLUMN_PRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_PRODUCT_TITLE + " TEXT,"
            + COLUMN_PRODUCT_CART + " INTEGER" + ")")

    // drop table sql query
    private val DROP_PRODUCT_TABLE = "DROP TABLE IF EXISTS " + TABLE_PRODUCT
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_PRODUCT_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        //Drop User Table if exist
        db.execSQL(DROP_PRODUCT_TABLE)
        // Create tables again
        onCreate(db)
    }
    fun addProduct(product:Product) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_PRODUCT_TITLE, product.name)
        values.put(COLUMN_PRODUCT_CART,product.cart)
        // Inserting Row
        db.insert(TABLE_PRODUCT, null, values)
        db.close()
    }
    fun deleteCart() {

        val db: SQLiteDatabase = this.readableDatabase
        try {
            db.execSQL("DELETE FROM " + TABLE_PRODUCT)
        } catch (e: SQLiteException) {
            e.printStackTrace()
        }
    }

    fun updateProduct(product: Product) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_PRODUCT_CART, product.cart)
        // updating row
        db.update(
            TABLE_PRODUCT,
            values,
            COLUMN_PRODUCT_TITLE + " = ?",
            arrayOf(product.name)
        )
        db.close()
    }

    companion object {
        // Database Version
        private const val DATABASE_VERSION = 1

        // Database Name
        private const val DATABASE_NAME = "UserManager.db"

        // User table name
        private const val TABLE_PRODUCT = "product"

        // User Table Columns names
        private const val COLUMN_PRODUCT_ID = "product_id"
        private const val COLUMN_PRODUCT_TITLE = "product_title"
        private const val COLUMN_PRODUCT_CART = "product_cart"
    }
}

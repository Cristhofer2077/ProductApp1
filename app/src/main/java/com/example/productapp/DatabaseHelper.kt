package com.example.productapp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "product_database.db"
        private const val DATABASE_VERSION = 1
        const val TABLE_PRODUCTS = "products"
        const val COLUMN_ID = "id"
        const val COLUMN_CODE = "code"
        const val COLUMN_DESCRIPTION = "description"
        const val COLUMN_PRICE = "price"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = """
            CREATE TABLE $TABLE_PRODUCTS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_CODE TEXT,
                $COLUMN_DESCRIPTION TEXT,
                $COLUMN_PRICE REAL
            )
        """
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_PRODUCTS")
        onCreate(db)
    }


    fun insertProduct(code: String, description: String, price: Double): Boolean {
        val db = writableDatabase
        val values = ContentValues()
        values.put(COLUMN_CODE, code)
        values.put(COLUMN_DESCRIPTION, description)
        values.put(COLUMN_PRICE, price)

        val result = db.insert(TABLE_PRODUCTS, null, values)
        return result != -1L
    }


    fun getAllProducts(): Cursor {
        val db = readableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_PRODUCTS", null)
    }


    fun updateProduct(id: Int, code: String, description: String, price: Double): Boolean {
        val db = writableDatabase
        val values = ContentValues()
        values.put(COLUMN_CODE, code)
        values.put(COLUMN_DESCRIPTION, description)
        values.put(COLUMN_PRICE, price)

        val result = db.update(TABLE_PRODUCTS, values, "$COLUMN_ID = ?", arrayOf(id.toString()))
        return result > 0
    }


    fun deleteProduct(id: Int): Boolean {
        val db = writableDatabase
        val result = db.delete(TABLE_PRODUCTS, "$COLUMN_ID = ?", arrayOf(id.toString()))
        return result > 0
    }
}

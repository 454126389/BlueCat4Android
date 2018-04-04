package com.tongge.common.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * Created by stai on 2017/10/30.
 *
 */

var VERSION = 1
class STDatabaseHelper(context: Context, name: String,
                       factory: SQLiteDatabase.CursorFactory,
                       version: Int) : SQLiteOpenHelper(context,name,factory,version) {
    init {

    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(" ")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

}
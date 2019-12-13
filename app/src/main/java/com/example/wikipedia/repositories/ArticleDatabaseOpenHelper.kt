package com.example.wikipedia.repositories

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*
import org.jetbrains.anko.db.ManagedSQLiteOpenHelper
import org.jetbrains.anko.db.createTable

class ArticleDatabaseOpenHelper(context: Context) : ManagedSQLiteOpenHelper(context, "ArticlesDatabase.db", null, 2) {
    override fun onCreate(db: SQLiteDatabase?) {

        // define the tables in this database
        db?.createTable("Favorites", true,
            "id" to INTEGER,
            "title" to TEXT,
            "url" to TEXT,
            "thumbnailJson" to TEXT,
            "category" to TEXT)

        db?.createTable("History", true,
            "id" to INTEGER,
            "title" to TEXT,
            "url" to TEXT,
            "thumbnailJson" to TEXT,
            "category" to TEXT)

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // use to upgrade the schema of the table if needed
    }

}
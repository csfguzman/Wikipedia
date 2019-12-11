package com.example.wikipedia.repositories

import android.util.Log
import com.example.wikipedia.models.Category
import com.example.wikipedia.models.WikiPage
import com.example.wikipedia.models.WikiThumbnail
import com.google.gson.Gson
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.rowParser
import org.jetbrains.anko.db.select


class HistoryRepository(val databaseHelper: ArticleDatabaseOpenHelper) {
    private val TABLE_NAME: String = "History"

    fun addFavorite(page: WikiPage){
        page.categories?.forEach{
            it.title = it.title?.replace("Category:","")

            databaseHelper.use {
                insert(TABLE_NAME,
                    "id" to page.pageid,
                    "title" to page.title,
                    "url" to page.fullurl,
                    "thumbnailJson" to Gson().toJson(page.thumbnail),
                    "category" to Gson().toJson(it))
            }
        }
    }

    fun removePageById(pageId: Int){
        databaseHelper.use {
            delete(TABLE_NAME, "id = {pageId}", "pageId" to pageId)
        }
    }

    fun getAllHistory() : ArrayList<WikiPage> {
        var pages = ArrayList<WikiPage>()

        val articleRowParser = rowParser { id: Int, title: String, url: String, thumbnailJson: String, categoriesJson:String ->
            val page = WikiPage()
            page.title = title
            page.pageid = id
            page.fullurl = url
            page.thumbnail = Gson().fromJson(thumbnailJson, WikiThumbnail::class.java)

            Log.i("thumbnailJson", thumbnailJson)
            Log.i("categoriesJson", categoriesJson)

            pages.add(page)
        }

        databaseHelper.use {
            select(TABLE_NAME).parseList(articleRowParser)
        }

        return pages
    }
}
package com.example.wikipedia.repositories

import android.util.Log
import com.example.wikipedia.models.WikiPage
import com.example.wikipedia.models.WikiThumbnail
import com.google.gson.Gson
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.rowParser
import org.jetbrains.anko.db.select


class FavoritesRepository(val databaseHelper: ArticleDatabaseOpenHelper) {
    private val TABLE_NAME: String = "Favorites"

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

            return
        }
    }

    fun removeFavoriteById(pageId: Int){
        databaseHelper.use {
            delete(TABLE_NAME, "id = {pageId}", "pageId" to pageId)
        }
    }

    fun isArticleFavorite(pageId: Int) : Boolean{
        // get favorites and filter
        var pages = getAllFavorites()
        return pages.any { page ->
            page.pageid == pageId
        }
    }

    fun getAllFavorites() : ArrayList<WikiPage> {
        var pages = ArrayList<WikiPage>()

        val articleRowParser = rowParser { id: Int, title: String, url: String, thumbnailJson: String, category: String ->
            val page = WikiPage()
            page.title = title
            page.pageid = id
            page.fullurl = url
            page.thumbnail = Gson().fromJson(thumbnailJson, WikiThumbnail::class.java)

            pages.add(page)
        }

        databaseHelper.use {
            select(TABLE_NAME).parseList(articleRowParser)
        }

        return pages
    }
}